package cn.org.faster.framework.web.version;

import cn.org.faster.framework.web.version.annotation.ApiVersion;
import cn.org.faster.framework.web.version.condition.ApiVersionCondition;
import cn.org.faster.framework.web.version.state.ApiVersionState;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangbowen
 */
@AllArgsConstructor
public class ApiRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    //最小版本
    private int minimumVersion;
    //自动解析包名，获取版本号
    private boolean parsePackageVersion;
    private static final String VERSION_FLAG = "{version}";
    private final static Pattern PACKAGE_VERSION_PREFIX_PATTERN = Pattern.compile("\\.v(\\d+).*");

    private RequestCondition<ApiVersionCondition> createCondition(Class<?> clazz) {
        RequestMapping classRequestMapping = clazz.getAnnotation(RequestMapping.class);
        if (classRequestMapping == null) {
            return null;
        }
        StringBuilder mappingUrlBuilder = new StringBuilder();
        if (classRequestMapping.value().length > 0) {
            mappingUrlBuilder.append(classRequestMapping.value()[0]);
        }
        String mappingUrl = mappingUrlBuilder.toString();
        if (!mappingUrl.contains(VERSION_FLAG)) {
            return null;
        }
        ApiVersion apiVersion = clazz.getAnnotation(ApiVersion.class);
        return new ApiVersionCondition(new ApiVersionState.ApiVersionStateBuilder()
                .apiVersion(apiVersion)
                .packageVersion(parseVersionByPackage(clazz))
                .minimumVersion(minimumVersion)
                .build());
    }

    /**
     * 通过包名解析出版本号
     *
     * @param clazz 类
     * @return 版本号/null
     */
    private Integer parseVersionByPackage(Class<?> clazz) {
        //如果关闭了自动解析包名，直接返回null
        if (!this.parsePackageVersion) {
            return null;
        }
        Matcher m = PACKAGE_VERSION_PREFIX_PATTERN.matcher(clazz.getPackage().getName());
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return null;
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        return createCondition(method.getClass());
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        return createCondition(handlerType);
    }
}
