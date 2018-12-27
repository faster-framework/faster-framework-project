package cn.org.faster.framework.web.secret.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * 3DES加密解密
 */
@Slf4j
public class DesCbcUtil {
    // 加解密统一使用的编码方式
    private final static String encoding = "UTF-8";

    /**
     *
     * @param plainText 普通文本
     * @param secretKey 密钥
     * @param iv 向量
     * @return 加密后的文本，失败返回null
     */
    public static String encode(String plainText, String secretKey, String iv) {
        String result = null;
        try {
            DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("desede");
            Key desKey = secretKeyFactory.generateSecret(deSedeKeySpec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            result = Base64Utils.encodeToString(encryptData);
        } catch (Exception e) {
            log.error("DesCbcUtil encode error : {}", e);
        }
        return result;
    }

    /**
     *  3DES解密
     * @param encryptText 加密文本
     * @param secretKey 密钥
     * @param iv 向量
     * @return 解密后明文，失败返回null
     */
    public static String decode(String encryptText, String secretKey, String iv) {
        String result = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("desede");
            Key desKey = secretKeyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, desKey, ips);
            byte[] decryptData = cipher.doFinal(Base64Utils.decodeFromString(encryptText));
            result = new String(decryptData, encoding);
        } catch (Exception e) {
            log.error("DesCbcUtil decode error : {}", e.getMessage());
        }
        return result;
    }

}