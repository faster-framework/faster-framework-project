package cn.org.faster.framework.core.utils;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 */
public class Utils {
    public static final int MINUTE = 60;
    public static final int HOUR = MINUTE * 60;

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 根据手机号生成用户名
     *
     * @param phone 手机号
     * @return 用户名
     */
    public static String generateNameByPhone(String phone) {
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 生成短信验证码
     *
     * @return 短信验证码
     */
    public static String generateSmsCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    public static Class getSuperClassGenericType(final Class clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 生成md5
     *
     * @param str 要加密的内容
     * @return 加密后内容
     */
    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     * bean转map
     *
     * @param bean 原始bean
     * @param <T>  泛型
     * @return map
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 排序签名
     *
     * @param o      要签名的对象
     * @param <T>    泛型
     * @param secret 秘钥
     * @return 加密后字符串
     */
    public static <T> String signWithSort(T o, String secret) {
        Map<String, Object> map = beanToMap(o);
        String signStr = map.keySet().stream().sorted()
                .map(key -> {
                    Object value = map.get(key);
                    if (value == null) {
                        return null;
                    }
                    return key.concat("=").concat(value + "");
                }).filter(Objects::nonNull)
                .collect(Collectors.joining("&"));
        return md5(signStr + secret);
    }

    /**
     * inputstream 转 byte[]数组
     *
     * @param input 输入流
     * @return byt[]数组
     * @throws IOException 异常
     */
    public static byte[] inputStreamToByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 获取文件名后缀，携带小数点
     *
     * @param fileName 文件名称
     * @return 后缀名称
     */
    public static String fileSuffixWithPoint(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > -1) {
            return fileName.substring(suffixIndex);
        }
        return "";
    }

    /**
     * 获取文件名后缀，不携带小数点
     *
     * @param fileName 文件名称
     * @return 后缀名称
     */
    public static String fileSuffix(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > -1) {
            return fileName.substring(suffixIndex + 1);
        }
        return "";
    }
}
