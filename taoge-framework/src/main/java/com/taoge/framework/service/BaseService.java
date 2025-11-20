package com.taoge.framework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taoge.ApplicationContextUtil;
import com.taoge.framework.annotation.PrimaryKey;
import com.taoge.framework.common.ErrorCode;
import com.taoge.framework.controller.BaseParam;
import com.taoge.framework.controller.PageParam;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.exception.DatabaseException;
import com.taoge.framework.util.BeanMapUtil;
import com.taoge.framework.util.ServiceUtil;
import com.taoge.framework.util.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuejingtao
 * 公用service
 *
 * @param <Entity> 实体类类型
 * @param <Mapper> Mapper类型
 */
public abstract class BaseService<Entity, Mapper> {
    protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * 记录[类:方法]对应关系
     */
    private static HashMap<String, Method> methodHashMap = new HashMap<>();
    /**
     * 记录[类:主键字段]对应关系
     */
    private static HashMap<String, Field> primaryKeyFieldMap = new HashMap<>();

    private Mapper mapper;

    /**
     * 记录泛型的class
     */
    private Class<Entity> entityClass = null;
    private Class<Mapper> mapperClass = null;

    public BaseService() {
        try {
            Type genType = getClass().getGenericSuperclass();
            if (genType instanceof ParameterizedType) {
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                entityClass = (Class) params[0];
                mapperClass = (Class) params[1];
            }
        } catch (Exception e) {
            logger.error("BaseService load class error: {} , e:", this.getClass().getName(), e);
        }
    }

    protected Class<Entity> getEntityClass() {
        return entityClass;
    }

    protected Class<Mapper> getMapperClass() {
        return mapperClass;
    }

    /**
     * 需要重写的方法,返回业务Mapper
     *
     * @return mapper
     */
    protected Mapper getMapper() {
        if (mapper == null) {
            mapper = ApplicationContextUtil.getBean(getMapperClass());
        }
        return mapper;
    }

    /**
     * 保存方法,有id执行更新,没有id执行添加
     *
     * @param entity 实体类
     * @return 影响行数, -1:没找到getId()主键方法
     */
    public int save(Entity entity) {
        try {
            Method m = getPrimaryKeyGetMethod(entity.getClass());
            if (null == m) {
                m = getMethodByMethodName(entity.getClass(), "getId");
            }
            if (null != m) {
                Object primaryKey = m.invoke(entity);
                if (null == primaryKey) {
                    return insertSelective(entity);
                } else {
                    return updateByPrimaryKeySelective(entity);
                }
            }
            return -1;
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    private int insert0(Entity entity, String methodName) {
        try {

            Method getMethod = getPrimaryKeyGetMethod(entity.getClass());
            if (null == getMethod) {
                getMethod = getMethodByMethodName(entity.getClass(), "getId");
            }
            if (getMethod != null && getMethod.invoke(entity) == null) {
                Method m = getPrimaryKeySetMethod(entity.getClass());
                if (null == m) {
                    m = getMethodByMethodName(entity.getClass(), "setId");
                }
                PrimaryKey primaryKey = getPrimaryKeyFile(entity.getClass()).getAnnotation(PrimaryKey.class);
                if (null != m) {
                    Class<?> paramClass = m.getParameterTypes()[0];
                    Object id = generateId();
                    if (paramClass == String.class) {
                        m.invoke(entity, String.valueOf(id));
                    } else if (paramClass == Long.class) {
                        if (null != primaryKey && !primaryKey.autoIncrement()) {
                            m.invoke(entity, Long.parseLong(id.toString()));
                        }
                    } else if (paramClass == Integer.class) {
                        if (null != primaryKey && !primaryKey.autoIncrement()) {
                            m.invoke(entity, Integer.parseInt(id.toString()));
                        }
                    }
                }
            }

            return invoke(entity, methodName);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    protected Object generateId() {
        return SnowFlake.nextId();
    }

    public int insert(Entity entity) {
        return insert0(entity, "insert");
    }

    public int insertSelective(Entity entity) {
        return insert0(entity, "insertSelective");
    }

    public int updateByPrimaryKey(Entity entity) {
        return invoke(entity, "updateByPrimaryKey");
    }

    public int updateByPrimaryKeySelective(Entity entity) {
        return invoke(entity, "updateByPrimaryKeySelective");
    }

    /**
     * 按名称执行方法
     *
     * @param entity 实体对象
     * @param method 方法名
     * @return
     */
    public int invoke(Entity entity, String method) {
        try {
            return Integer.parseInt(getMapperMethod(method, entity.getClass()).invoke(getMapper(), entity).toString());
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 按主键查询
     *
     * @param primaryKey
     * @return
     */
    public Entity get(Object primaryKey) {
        try {
            if (null == primaryKey) {
                return null;
            }
            Method m = getMethodByMethodName(getMapper().getClass(), "selectByPrimaryKey");
            if (null != m) {
                Class<?> paramClass = m.getParameterTypes()[0];
                if (paramClass == String.class) {
                    return (Entity) m.invoke(getMapper(), primaryKey.toString());
                } else if (paramClass == Integer.class) {
                    return (Entity) m.invoke(getMapper(), Integer.parseInt(primaryKey.toString()));
                } else if (paramClass == Long.class) {
                    return (Entity) m.invoke(getMapper(), Long.parseLong(primaryKey.toString()));
                }
            }
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 按主键查询，并校验记录是否存在
     *
     * @param primaryKey 主键
     * @return
     */
    public Entity getAndExists(Object primaryKey) {
        Entity entity = get(primaryKey);
        if (null == entity) {
            throw new BusinessException("未找到记录");
        }
        return entity;
    }

    /**
     * 按主键查询，并转换为指定类型
     *
     * @param primaryKey 主键
     * @param voClass    转换的类型
     * @param <T>        转换的类型
     * @return
     */
    public <T> T getVO(Object primaryKey, Class<T> voClass) {
        try {
            Entity entity = get(primaryKey);
            if (null == entity) {
                return null;
            }
            T vo = voClass.newInstance();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 按主键删除
     *
     * @param primaryKey 主键
     * @return
     */
    public int delete(Object primaryKey) {
        try {
            if (null == primaryKey) {
                return -1;
            }
            Method m = getMethodByMethodName(getMapper().getClass(), "deleteByPrimaryKey");
            if (null != m) {
                Class<?> paramClass = m.getParameterTypes()[0];
                if (paramClass == String.class) {
                    return Integer.parseInt(m.invoke(getMapper(), primaryKey.toString()).toString());
                } else if (paramClass == Integer.class) {
                    return Integer.parseInt(m.invoke(getMapper(), Integer.parseInt(primaryKey.toString())).toString());
                } else if (paramClass == Long.class) {
                    return Integer.parseInt(m.invoke(getMapper(), Long.parseLong(primaryKey.toString())).toString());
                }
            }
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public Long count(Map<?, ?> paramMap) {
        try {
            return (Long) getMapperMethod("count", HashMap.class).invoke(getMapper(), paramMap);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<Entity> all() {
        return list(new HashMap<>());
    }

    /**
     * all方法返回vo对象
     *
     * @param voClass vo对象class
     * @return
     */
    public <T> List<T> voAll(Class<T> voClass) {
        return voList(new HashMap<>(), voClass);
    }

    /**
     * 按参数查询列表
     *
     * @param param 参数
     * @param <P>   参数类型
     * @return
     */
    public <P extends BaseParam> List<Entity> list(P param) {
        return list(BeanMapUtil.toHashMap(param));
    }

    /**
     * 按参数查询列表
     *
     * @param paramMap 参数
     * @return
     */
    public List<Entity> list(Map<?, ?> paramMap) {
        try {
            return (List<Entity>) getMapperMethod("list", HashMap.class).invoke(getMapper(), paramMap);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 返回list数据时转换vo数据方法
     *
     * @param param   查询参数
     * @param voClass vo对象class
     * @param <T>     要转换的class类型
     * @param <P>     参数类型
     * @return
     */
    public <T, P extends BaseParam> List<T> voList(P param, Class<T> voClass) {
        return voList(BeanMapUtil.toHashMap(param), voClass);
    }

    /**
     * 返回list数据时转换vo数据方法
     *
     * @param paramMap 查询参数
     * @param voClass  vo对象class
     */
    public <T> List<T> voList(HashMap<?, ?> paramMap, Class<T> voClass) {
        try {
            List<Entity> list = list(paramMap);
            return ServiceUtil.convertListToVO(list, voClass);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 分页查询
     *
     * @param pageParam 查询参数
     * @return
     */
    public <Page extends PageParam> PageInfo<Entity> pageList(Page pageParam) {
        return pageList(pageParam.getPageNum(), pageParam.getPageSize(), pageParam);
    }

    /**
     * 分页查询
     *
     * @param pageNum  当前页号
     * @param pageSize 每页显示条数
     * @param param    查询参数
     * @param <P>      参数类型
     * @return
     */
    public <P extends BaseParam> PageInfo<Entity> pageList(Integer pageNum, Integer pageSize, P param) {
        return pageList(pageNum, pageSize, BeanMapUtil.toHashMap(param));
    }

    /**
     * 分页查询
     *
     * @param pageParam 分页参数
     * @param paramMap  查询参数
     * @return
     */
    public <Page extends PageParam> PageInfo<Entity> pageList(Page pageParam, HashMap<?, ?> paramMap) {
        return pageList(pageParam.getPageNum(), pageParam.getPageSize(), paramMap);
    }

    /**
     * 分页查询
     *
     * @param pageNum  当前页号
     * @param pageSize 每页显示条数
     * @param paramMap 查询参数
     * @return
     */
    public PageInfo<Entity> pageList(Integer pageNum, Integer pageSize, HashMap<?, ?> paramMap) {
        try {
            PageHelper.startPage(pageNum, pageSize);
            return new PageInfo<>((List<Entity>) getMapperMethod("list", HashMap.class).invoke(getMapper(), paramMap));
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 分页查询，将list结果转换成指定的类型
     *
     * @param param   查询参数
     * @param voClass 需要转换的class类型
     * @param <Page>  参数类型
     * @param <T>     要转换的class类型
     * @return
     */
    public <Page extends PageParam, T> PageInfo<T> pageVOList(Page param, Class<T> voClass) {
        return pageVOList(param.getPageNum(), param.getPageSize(), BeanMapUtil.toHashMap(param), voClass);
    }

    public <P extends BaseParam, T> PageInfo<T> pageVOList(Integer pageNum, Integer pageSize, P param, Class<T> voClass) {
        return pageVOList(pageNum, pageSize, BeanMapUtil.toHashMap(param), voClass);
    }

    /**
     * 分页查询，将list结果转换成指定的类型
     *
     * @param pageNum  当前页号
     * @param pageSize 每页显示条数
     * @param paramMap 查询参数
     * @param voClass  需要转换的class类型
     * @param <T>      要转换的class类型
     * @return
     */
    public <T> PageInfo<T> pageVOList(Integer pageNum, Integer pageSize, HashMap<?, ?> paramMap, Class<T> voClass) {
        try {
            PageInfo<Entity> pageInfo = pageList(pageNum, pageSize, paramMap);
            PageInfo<T> resultPageInfo = new PageInfo<>();
            BeanUtils.copyProperties(pageInfo, resultPageInfo, "list");
            if (null != pageInfo.getList()) {
                resultPageInfo.setList(ServiceUtil.convertListToVO(pageInfo.getList(), voClass));
            }
            return resultPageInfo;
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 按主键id集合查找
     *
     * @param ids 主键id集合
     * @return
     */
    public List<Entity> selectByIds(List<?> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>(0);
        }
        try {
            return (List<Entity>) getMapperMethod("selectByIds", List.class).invoke(getMapper(), ids);
        } catch (Exception e) {
            logger.error("{}, e:", ErrorCode.DATA_ERROR_MSG, e);
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

    /**
     * 按类型获取主键get方法
     *
     * @param c 类型
     * @return
     */
    protected Method getPrimaryKeyGetMethod(Class<?> c) {
        Field primaryKeyField = getPrimaryKeyFile(c);
        if (null != primaryKeyField) {
            String getPrimaryKeyFieldMethod = "get" + StringUtils.capitalize(primaryKeyField.getName());
            return getMethodByMethodName(c, getPrimaryKeyFieldMethod);
        }
        return null;
    }

    /**
     * 按类型获取主键set方法
     *
     * @param c 类型
     * @return
     */
    protected Method getPrimaryKeySetMethod(Class<?> c) {
        Field primaryKeyField = getPrimaryKeyFile(c);
        if (null != primaryKeyField) {
            String getPrimaryKeyFieldMethod = "set" + StringUtils.capitalize(primaryKeyField.getName());
            return getMethodByMethodName(c, getPrimaryKeyFieldMethod);
        }
        return null;
    }

    /**
     * 按类型获取主键字段
     *
     * @param c 类型
     * @return
     */
    protected Field getPrimaryKeyFile(Class<?> c) {
        String fieldKey = "PrimaryKey-" + c.getName();
        Field field = primaryKeyFieldMap.get(fieldKey);
        if (null == field) {
            for (Field declaredField : c.getDeclaredFields()) {
                if (declaredField.getAnnotation(PrimaryKey.class) != null) {
                    field = declaredField;
                    break;
                }
            }
            if (null != field) {
                primaryKeyFieldMap.put(fieldKey, field);
            }
        }
        return field;
    }

    /**
     * 按名称查询方法
     *
     * @param c          类型
     * @param methodName 方法名
     * @return
     */
    protected Method getMethodByMethodName(Class<?> c, String methodName) {
        String methodKey = "SimpleMethod-" + c.getName() + methodName;
        Method m = methodHashMap.get(methodKey);
        if (null == m) {
            for (Method method : c.getMethods()) {
                if (method.getName().equals(methodName)) {
                    m = method;
                    break;
                }
            }
            if (null != m) {
                methodHashMap.put(methodKey, m);
            }
        }
        return m;
    }

    /**
     * 按名称查询当前mapper的方法
     *
     * @param methodName  方法名
     * @param paramsClass 参数类型
     * @return
     */
    protected Method getMapperMethod(String methodName, Class<?>... paramsClass) {
        return getMapperMethod(getMapper().getClass(), methodName, paramsClass);
    }

    /**
     * /**
     * 按名称查询的方法
     *
     * @param c           类型
     * @param methodName  方法名
     * @param paramsClass 参数类型
     * @return
     */
    protected Method getMapperMethod(Class<?> c, String methodName, Class<?>... paramsClass) {
        try {
            Class<?>[] paramTypeClass = new Class[paramsClass.length];
            StringBuilder methodKey = new StringBuilder(c.getName() + "." + methodName + "(");
            if (paramsClass.length > 0) {
                for (int i = 0; i < paramsClass.length; i++) {
                    paramTypeClass[i] = paramsClass[i];
                    methodKey.append(paramsClass[i].getName()).append(",");
                }
                methodKey.delete(methodKey.length() - 1, methodKey.length());
            }
            methodKey.append(")");
            Method m = methodHashMap.get(methodKey.toString());
            if (null == m) {
                m = c.getMethod(methodName, paramTypeClass);
            }
            if (null == m) {
                throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, "未找到" + methodName + "方法,class:" + c);
            } else {
                methodHashMap.put(methodKey.toString(), m);
            }
            return m;
        } catch (Exception e) {
            throw new DatabaseException(ErrorCode.DATA_ERROR_CODE, ErrorCode.DATA_ERROR_MSG);
        }
    }

}
