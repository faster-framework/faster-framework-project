package cn.org.faster.framework.sms.modules.smsCode.service;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.core.utils.Utils;
import org.springframework.util.StringUtils;

/**
 * @author zhangbowen
 * @since 2018/8/27
 */
public abstract class ISmsCaptchaService {
    //是否为调试环境
    protected boolean debug;
    //超时时间
    protected long expire;

    private static final String SMS_CAPTCHA_PREFIX = "sms-captcha:";

    public ISmsCaptchaService(boolean debug, long expire) {
        this.debug = debug;
        this.expire = expire;
    }

    /**
     * 发送短信验证码，不需要前缀
     *
     * @param phone 手机号
     * @return true/false
     */
    public boolean send(String phone) {
        return send(phone, "");
    }

    /**
     * 发送短信验证码
     *
     * @param phone  手机号
     * @param prefix 前缀
     * @return true/false
     */
    public boolean send(String phone, String prefix) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        if (debug) {
            return true;
        }
        String code = generateCode();
        boolean success = this.sendCode(phone, code);
        if (success) {
            CacheFacade.set(SMS_CAPTCHA_PREFIX + prefix + phone, code, expire);
        }
        return success;
    }

    /**
     * 验证短信验证码，不需要前缀
     *
     * @param phone 手机号
     * @param code  短信验证码
     * @return true/false
     */
    public boolean valid(String phone, String code) {
        return valid(phone, code);
    }

    /**
     * 验证短信验证码
     *
     * @param phone  手机号
     * @param code   短信验证码
     * @param prefix 前缀
     * @return true/false
     */
    public boolean valid(String phone, String code, String prefix) {
        if (debug) {
            return true;
        }
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            return false;
        }
        String existCode = CacheFacade.get(SMS_CAPTCHA_PREFIX + prefix + phone);
        if (code.equals(existCode)) {
            return true;
        }
        return false;
    }

    /**
     * 通过手机号和前缀删除短信验证码,不需要前缀
     *
     * @param phone 手机号
     */
    public void remove(String phone) {
        this.remove(phone, "");
    }

    /**
     * 通过手机号和前缀删除短信验证码
     *
     * @param phone  手机号
     * @param prefix 前缀
     */
    public void remove(String phone, String prefix) {
        CacheFacade.delete(SMS_CAPTCHA_PREFIX + prefix + phone);
    }


    /**
     * @return 短信验证码
     */
    protected String generateCode() {
        return Utils.generateSmsCode();
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return true/false
     */
    protected abstract boolean sendCode(String phone, String code);
}
