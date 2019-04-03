package cn.org.faster.framework.web.version;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangbowen
 */
@Data
@AllArgsConstructor
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+).*");
    /**
     * 版本注解
     */
    private ApiVersionState apiVersionState;

    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        return new ApiVersionCondition(apiVersionCondition.getApiVersionState());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getRequestURI());
        if (m.find()) {
            int version = Integer.parseInt(m.group(1));
            if (version >= this.apiVersionState.getVersion()) {
                if (this.apiVersionState.isDiscard() && this.apiVersionState.getVersion() == version) {
                    throw new ApiVersionDiscardException("当前版本已停用，请升级到最新版本。");
                }
                return this;
            }
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return apiVersionCondition.getApiVersionState().getVersion() - this.apiVersionState.getVersion();
    }
}
