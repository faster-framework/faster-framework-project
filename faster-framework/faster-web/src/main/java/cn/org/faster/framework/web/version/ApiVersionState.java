package cn.org.faster.framework.web.version;

import lombok.Data;

/**
 * @author zhangbowen
 * @since 2019/1/8
 */
@Data
public class ApiVersionState {
    /**
     * 版本号
     */
    private int version;
    /**
     * 版本是否废弃
     */
    private boolean discard;

    /**
     * 创建
     *
     * @param apiVersion
     * @param minimumVersion
     * @return
     */
    public static ApiVersionState build(ApiVersion apiVersion, int minimumVersion) {
        ApiVersionState apiVersionState = new ApiVersionState();
        if (apiVersion == null) {
            apiVersionState.setVersion(1);
        } else {
            apiVersionState.setVersion(apiVersion.value());
            apiVersionState.setDiscard(apiVersion.discard());
        }
        if (apiVersionState.getVersion() < minimumVersion) {
            apiVersionState.setDiscard(true);
        }
        return apiVersionState;
    }
}
