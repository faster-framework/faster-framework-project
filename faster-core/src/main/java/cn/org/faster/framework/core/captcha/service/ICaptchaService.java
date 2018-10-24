package cn.org.faster.framework.core.captcha.service;

import cn.org.faster.framework.core.captcha.bean.CaptchaBean;
import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.core.web.service.JwtService;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.service.Captcha;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @author zhangbowen
 */
public abstract class ICaptchaService {
    @Autowired
    private JwtService jwtService;
    protected ConfigurableCaptchaService configurableCaptchaService;
    private static final String CAPTCHA_TOKEN_PREFIX = "captcha:";


    /**
     * 验证图形验证码是否有效
     *
     * @param captcha 验证码
     * @param token   token
     * @return true or false
     */
    public boolean valid(String captcha, String token) {
        Claims claims = jwtService.parseToken(token);
        return claims != null && (CAPTCHA_TOKEN_PREFIX + Utils.md5(captcha)).equalsIgnoreCase(claims.getAudience());
    }

    /**
     * 生成图形验证码
     *
     * @return 图形验证码bean
     */
    public CaptchaBean generateCaptcha() {
        Captcha captcha = configurableCaptchaService.getCaptcha();
        String captchaBase64 = imgToBase64(captcha.getImage());
        String token = jwtService.createToken(CAPTCHA_TOKEN_PREFIX + Utils.md5(captcha.getWord()), Utils.MINUTE);
        return new CaptchaBean(token, "data:image/png;base64," + captchaBase64);
    }

    /**
     * 图片转base64
     *
     * @param img 图片
     * @return base64
     */
    protected String imgToBase64(RenderedImage img) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", os);
            return Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (final IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}
