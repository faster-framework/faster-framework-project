package cn.org.faster.framework.web.exception.handler;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.web.context.model.RequestContext;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Objects;

/**
 * @author zhangbowen
 * @since 2020/3/14
 */
public class ExceptionExecutorHandler {
    @Autowired
    private RequestContext requestContext;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 异常处理主逻辑
     *
     * @param exception 异常
     * @return 页面/json
     */
    public Object exception(Exception exception, ResponseErrorEntityExecutor executor) {
        if (requestContext.isApiRequest()) {
            ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView(objectMapper));
            ResponseErrorEntity responseErrorEntity = executor.execute(exception);
            modelAndView.setStatus(responseErrorEntity.getStatusCode());
            modelAndView.addAllObjects(Utils.beanToMap(responseErrorEntity.getBody()));
            return modelAndView;
        }
        return new ModelAndView("redirect:/error");
    }
}
