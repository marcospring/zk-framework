package com.zhangk.datasource.utils;

import com.zhangk.datasource.obj.BasePo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommonBeanUtils {

	private static final Logger log = LoggerFactory.getLogger(CommonBeanUtils.class);

	static {
		// 在封装之前 注册转换器
		ConvertUtils.register(new DateTimeConverter(), Date.class);
	}


	/**
	 * map转换basePO
	 * @param map
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "finally" })
	public static <T extends BasePo> T transMap2BasePO(Map<String, Object> map, Class<? extends BasePo> clazz) {
		log.info("trans Map to " + clazz.getName() + " start ...");
		T instance = null;
		if (map == null) {
			log.warn("map is null. trans failure");
			return instance;
		}
		try {
			instance = (T) T.getInstance(clazz);
			BeanUtils.populate(instance, map);
		} catch (Exception e) {
			log.error("trans Map to " + clazz.getName() + " failure ... " + e);
			throw e;
		} finally {
			log.info("trans Map to " + clazz.getName() + " end ... ");
			return instance;
		}
	}

	/**
	 * map转换po
	 * @param map
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("finally")
	public static <T extends BasePo> T transMap2BasePO(Map<String, Object> map, T po) {
		if (map == null || po == null) {
			log.warn("map is null. trans failure");
			return null;
		}
		T newPo = po;
		try {
			newPo.setUpdateTime(new Date());
			BeanUtils.populate(newPo, map);
		} catch (Exception e) {
			log.error("trans Map to " + po.getClass().getName() + " failure ... " + e);
			throw e;
		} finally {
			log.info("trans Map to " + po.getClass().getName() + " end ... ");
			return newPo;
		}
	}

	/**
	 * 基于反射 将map转换成javaBean
	 * @param map
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("finally")
	public static <T> T transMap2Bean(Map<String, Object> map, Class<T> clazz) {
		log.info("trans Map to " + clazz.getName() + " start ...");
		T instance = null;
		if (map == null) {
			log.warn("map is null. trans failure ...");
			return instance;
		}
		try {
			instance = clazz.newInstance();
			BeanUtils.populate(instance, map);
		} catch (Exception e) {
			log.error("trans Map to " + clazz.getName() + " failure ... " + e);
			throw e;
		} finally {
			log.info("trans Map to " + clazz.getName() + " end ... ");
			return instance;
		}

	}

	/**
	 * 基于反射将javabean 转换成 Map<String, Object>
	 * @param obj
	 * @return
	 */
	@SuppressWarnings({ "unused", "finally" })
	public static Map<String, Object> transBean2Map(Object obj) {
		log.info("trans " + obj.getClass().getName() + " to map start ...");
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj == null) {
			log.warn(obj.getClass().getName() + " instance is null ... ");
			return null;
		}
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
					log.info("property:" + key + " success success ... ");
				}
			}
		} catch (Exception e) {
			log.error("trans" + obj.getClass().getName() + " to  Map failure ... " + e);
			throw e;
		} finally {
			log.info("trans " + obj.getClass().getName() + " to map start ...");
			return map;
		}
	}

	/**
	 * 获取可能为空的字符串的值，如果对象为空则返回空字符串，如果不为空则返回字符串值
	 * @param o
	 * @return
	 * String
	 *
	 */
	public static String getIfNullStringValue(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * 将sourceBean中与T同名的属性 set到T的实例中并返回T的实例
	 * @param clazz
	 * @param sourceBean
	 * @param <T>
	 * @param <S>
	 * @return
	 */
	@SuppressWarnings("finally")
	public static <T, S> T getBeanBySameProperty(Class<T> clazz, S sourceBean) {
		log.debug("method : getBeanBySameProperty start");
		T instance = null;
		try {
			instance = clazz.newInstance();
			BeanUtils.copyProperties(instance, sourceBean);
		} catch (InvocationTargetException e) {
			log.error("复制属性失败：" + e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("类：" + clazz.getName() + "创建实例失败...");
			throw e;
		} finally {
			log.debug("method : getBeanBySameProperty end");
			return instance;
		}
	}

	/**
	 * 将sourceBean中与T同名的属性 set到T的实例中并返回T的实例
	 * @param instance
	 * @param sourceBean
	 * @param <T>
	 * @param <S>
	 * @return
	 */
	@SuppressWarnings("finally")
	public static <T, S> T getBeanBySameProperty(T instance, S sourceBean) {
		log.debug("method : getBeanBySameProperty start");
		try {
			BeanUtils.copyProperties(instance, sourceBean);
		} catch (InvocationTargetException e) {
			log.error("复制属性失败：" + e.getMessage());
			throw e;
		} finally {
			log.debug("method : getBeanBySameProperty end");
			return instance;
		}
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class DateTimeConverter implements Converter {

	private static final String DATE = "yyyy-MM-dd";
	private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

	@Override
	public Object convert(Class type, Object value) {
		return toDate(type, value);
	}

	public static Object toDate(Class type, Object value) {
		if (value == null || "".equals(value))
			return null;
		if (value instanceof String) {
			String dateValue = value.toString().trim();
			int length = dateValue.length();
			if (type.equals(Date.class)) {
				try {
					DateFormat formatter = null;
					if (length <= 10) {
						formatter = new SimpleDateFormat(DATE, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 19) {
						formatter = new SimpleDateFormat(DATETIME, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 23) {
						formatter = new SimpleDateFormat(TIMESTAMP, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}