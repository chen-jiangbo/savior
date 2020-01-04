package com.jiangbo.savior.utils;

import com.jiangbo.savior.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

/**
 * 验证工具类
 */
public class AssertUtils {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ServiceException(message);
        }
    }

    public static void isNotEquals(String src, String reg, String msg) {
        if (!StringUtils.equals(src, reg)) {
            throw new ServiceException(msg);
        }
    }

    public static void isNotEquals(Object src, Object reg, String msg) {
        if (src == null && reg != null) {
            throw new ServiceException(msg);
        }
        if (!src.equals(reg)) {
            throw new ServiceException(msg);
        }
    }

    public static void isNotEqualsIgnoreCase(String src, String reg, String msg) {
        if (!StringUtils.equalsIgnoreCase(src, reg)) {
            throw new ServiceException(msg);
        }
    }

    public static void assertCase(boolean b, String message) {
        if (b) {
            throw new ServiceException(message);
        }
    }

    public static void assertParam(boolean b, String message) throws ServiceException {
        if (b) {
            throw new ServiceException(message);
        }
    }

    public static <T extends Enum<T>> T assertEnumParam(Class<T> clazz, String name, String message) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException var4) {
            throw new ServiceException(message);
        }
    }

    public static void assertCase(boolean b, String message, Throwable cause) throws ServiceException {
        if (b) {
            throw new ServiceException(message, cause);
        }
    }
}
