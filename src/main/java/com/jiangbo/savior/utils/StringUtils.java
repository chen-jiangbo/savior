package com.jiangbo.savior.utils;

import java.util.*;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 下滑线转驼峰命名
     *
     * @param str
     * @return
     */
    public static String camelCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        if (!str.contains("_")) {
            return str;
        }
        str = str.replace("T_", "");
        str = str.toLowerCase();
        String[] strs = str.split("_");
        if (strs.length == 1) {
            return allLower(str);
        } else {
            String convertedStr = "";
            for (int i = 1; i < strs.length; i++) {
                convertedStr += firstLetterUpper(strs[i]);
            }
            return strs[0] + convertedStr;
        }
    }

    /**
     * 首字母大写的方法
     *
     * @param str
     * @return
     */
    public static String firstLetterUpper(String str) {
        if (isNotBlank(str)) {
            str = str.replace("T_", "");
            str = str.toLowerCase();
            return str.substring(0, 1).toUpperCase()
                    + str.substring(1, str.length());
        }
        return str;
    }

    /**
     * 转换成小写的方法
     *
     * @param str
     * @return
     */
    public static String allLower(String str) {
        if (isNotBlank(str)) {
            str = str.replace("T_", "");
            str = str.toLowerCase();
            String[] strs = str.split("_");
            if (strs.length == 1) {
                return str.toLowerCase();
            } else {
                String convertedStr = "";
                for (int i = 0; i < strs.length; i++) {
                    convertedStr += strs[i].toLowerCase();
                }
                return convertedStr;
            }
        }
        return str;
    }

    /**
     * 判断对象是否为空
     *
     * @param param
     * @return
     */
    public static boolean isNotNull(Object param) {
        if (param == null) {
            return false;
        }
        if (param instanceof CharSequence) {
            return isNotBlank((CharSequence) param);
        }
        return true;
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成编号工具类
     *
     * @param code (1:订单,2-出库单)
     * @return
     */
    public static String buildCode(Integer code) {
        return new StringBuffer("BDD").append(code == 1 ? "IN" : "OUT").append((int) (new Random().nextDouble() * (9000) + 1000)).append(System.currentTimeMillis()).toString();
    }

    public static String buildPaydollarRef(){
        return new StringBuffer("PAYDOLLAR-").append((int) (new Random().nextDouble() * (9000) + 1000)).append(System.currentTimeMillis()).toString();
    }


    /**
     * map转str
     * @param map
     * @return
     */
    public static String getMapToString(Map<String,String> map){
        Set<String> keySet = map.keySet();
        //将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        //给数组排序(升序)
        Arrays.sort(keyArray);
        //因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(map.get(keyArray[i]).trim());
            }
            if(i != keyArray.length-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public static String generateSalt(){
        return org.apache.commons.lang3.StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    /**
     * 生成指定位数的随机数
     * @param length
     * @return
     */
    public static String getRandom(int length){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
