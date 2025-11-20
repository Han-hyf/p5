package com.taoge.framework.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * service工具类
 */
public class ServiceUtil {

    public static <T> List<T> convertListToVO(List<?> list, Class<T> voClass) throws Exception {
        return convertListToVO(list, voClass, new String[0]);
    }

    public static <T> List<T> convertListToVO(List<?> list, Class<T> voClass, String... ignoreProperties) throws Exception {
        List<T> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Object o : list) {
                T vo = voClass.newInstance();
                BeanUtils.copyProperties(o, vo, ignoreProperties);
                resultList.add(vo);
            }
        }
        return resultList;
    }
}
