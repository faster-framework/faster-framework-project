package cn.org.faster.framework.core.captcha.service.impl;

import cn.org.faster.framework.core.captcha.service.ICaptchaService;
import com.github.bingoohuang.patchca.color.SingleColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.word.AdaptiveRandomWordFactory;
import com.github.bingoohuang.patchca.word.RandomWordFactory;

import javax.annotation.PostConstruct;
import java.awt.*;

public class CaptchaService extends ICaptchaService {

    @PostConstruct
    public void init() {
        configurableCaptchaService = new ConfigurableCaptchaService();
        configurableCaptchaService.setColorFactory(new SingleColorFactory(new Color(26, 68, 195)));
        configurableCaptchaService.setFilterFactory(new CurvesRippleFilterFactory(configurableCaptchaService.getColorFactory()));
        RandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
        wordFactory.setMinLength(4);
        wordFactory.setMaxLength(4);
        configurableCaptchaService.setWordFactory(wordFactory);
    }


}
