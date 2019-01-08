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

    public static class ApiVersionStateBuilder {
        //注解版本
        private ApiVersion apiVersion;
        //包版本
        private Integer packageVersion;
        //最小支持的版本
        private int minimumVersion;

        public ApiVersionStateBuilder apiVersion(ApiVersion apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public ApiVersionStateBuilder packageVersion(Integer packageVersion) {
            this.packageVersion = packageVersion;
            return this;
        }

        public ApiVersionStateBuilder minimumVersion(int minimumVersion) {
            this.minimumVersion = minimumVersion;
            return this;
        }

        public ApiVersionState build() {
            ApiVersionState apiVersionState = new ApiVersionState();
            if (apiVersion == null) {
                apiVersionState.setVersion(1);
            } else {
                apiVersionState.setVersion(apiVersion.value());
                apiVersionState.setDiscard(apiVersion.discard());
            }
            if (this.packageVersion != null) {
                apiVersionState.setVersion(this.packageVersion);
            }
            if (apiVersionState.getVersion() < minimumVersion) {
                apiVersionState.setDiscard(true);
            }
            return apiVersionState;
        }
    }
}
