package com.taoge.framework.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.exception.ShellException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class GenerateServiceFileUtil {
    @Data
    public static class GenerateConfig {
        String targetProject; // 文件输出路径
        String servicePackage; // service包路径
        String entityPackage; // java实体包路径
        String mapperPackage; // mapper文件包路径
        String baseServicePackage; // BaseService文件路径，包含文件名
        String baseControllerPackage; // BaseController文件路径，包含文件名
        String apiControllerPackage; // 前端api包路径
        String controllerMethodPackage; // ControllerMethod路径，包含文件名
        String requestMapping; // controller根地址，"/api"
        List<String> tableNames;
    }

    public static void writeServiceFile(GenerateConfig generateConfig, List<String> tableNames) throws ShellException, IOException {
        List<String> convertTableNameList = convertTableName(tableNames);
        File dir = getDirectory(generateConfig.getTargetProject(), generateConfig.getServicePackage());

        for (String entityName : convertTableNameList) {
            String serviceName = entityName + "Service";
            String mapperName = entityName + "Mapper";
            StringBuilder content = new StringBuilder();
            content.append("package ").append(generateConfig.getServicePackage()).append(";\n\n");
            content.append("import ").append(generateConfig.getBaseServicePackage()).append(";\n");
            content.append("import ").append(generateConfig.getMapperPackage()).append(".").append(mapperName).append(";\n");
            content.append("import ").append(generateConfig.getEntityPackage()).append(".").append(entityName).append(";\n");
            content.append("import org.springframework.stereotype.Service;\n");
            content.append("import javax.annotation.Resource;\n\n");
            content.append("@Service\n");
            content.append("public class ").append(serviceName).append(" extends BaseService<").append(entityName).append(", ").append(mapperName).append("> {\n");
            content.append("\t@Resource\n");
            content.append("\t").append(mapperName).append(" ").append(StringUtils.uncapitalize(mapperName)).append(";\n\n");
            content.append("\t@Override\n");
            content.append("\tpublic ").append(mapperName).append(" getMapper() {\n");
            content.append("\t\treturn ").append(StringUtils.uncapitalize(mapperName)).append(";\n");
            content.append("\t}\n");
            content.append("}");
            File serviceFile = new File(dir.getAbsolutePath() + File.separator + serviceName + ".java");
            FileOutputStream fos = new FileOutputStream(serviceFile, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content.toString());
            bw.close();
        }
    }

    public static void writeApiControllerFile(GenerateConfig generateConfig, List<String> tableNames, boolean isBack) throws ShellException, IOException {
        List<String> convertTableNameList = convertTableName(tableNames);
        File dir = getDirectory(generateConfig.getTargetProject(), generateConfig.getApiControllerPackage());

        for (String entityName : convertTableNameList) {
            String controllerName = entityName + "Controller";
            String serviceName = entityName + "Service";
            StringBuilder content = new StringBuilder();
            content.append("package ").append(generateConfig.getApiControllerPackage()).append(";\n\n");
            if (isBack) {
                content.append("import ").append(generateConfig.getControllerMethodPackage()).append(";\n");
                content.append("import ").append(generateConfig.getBaseControllerPackage()).append(";\n");
                content.append("import ").append(generateConfig.getEntityPackage()).append(".").append(entityName).append(";\n");
            }
            content.append("import ").append(generateConfig.getServicePackage()).append(".").append(serviceName).append(";\n");
            content.append("import org.springframework.stereotype.Controller;\n");
            content.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
            content.append("import javax.annotation.Resource;\n\n");
            content.append("@RequestMapping(\"").append(StringUtils.isNotEmpty(generateConfig.getRequestMapping()) ? generateConfig.getRequestMapping() : "/").append(StringUtils.uncapitalize(entityName)).append("\")\n");
            content.append("@Controller\n");
            if (isBack) {
                content.append("@ControllerMethod\n");
                content.append("public class ").append(controllerName).append(" extends BaseController<").append(entityName).append(", ").append(serviceName).append("> {\n");
                content.append("\t@Resource\n");
                content.append("\t").append(serviceName).append(" ").append(StringUtils.uncapitalize(serviceName)).append(";\n\n");
                content.append("\t@Override\n");
                content.append("\tpublic ").append(serviceName).append(" getService() {\n");
                content.append("\t\treturn ").append(StringUtils.uncapitalize(serviceName)).append(";\n");
                content.append("\t}\n");
            } else  {
                content.append("public class ").append(controllerName).append(" {\n");
                content.append("\t@Resource\n");
                content.append("\t").append(serviceName).append(" ").append(StringUtils.uncapitalize(serviceName)).append(";\n\n");
            }
            content.append("}");

            File serviceFile = new File(dir.getAbsolutePath() + File.separator + controllerName + ".java");
            FileOutputStream fos = new FileOutputStream(serviceFile, false);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content.toString());
            bw.close();
        }
    }

    public static List<String> convertTableName(List<String> tableName) {
        if (null == tableName || tableName.size() == 0) {
            return tableName;
        }
        List<String> convertList = new ArrayList<>();
        for (String name : tableName) {
            StringBuilder newName = new StringBuilder();
            for (String n : name.split("([_\\-])")) {
                newName.append(StringUtils.capitalize(n));
            }
            convertList.add(newName.toString());
        }
        return convertList;
    }

    public static File getDirectory(String targetProject, String targetPackage)
            throws ShellException {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(getString("Warning.9", //$NON-NLS-1$
                    targetProject));
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                        directory.getAbsolutePath()));
            }
        }

        return directory;
    }
}
