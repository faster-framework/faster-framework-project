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
        private ApiVersionState apiVersionState = new ApiVersionState();
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

        private void initVersion() {
            //如果当前没有注解
            if (apiVersion == null) {
                //判断当前package的版本是否存在，如果存在设置
                if (this.packageVersion != null) {
                    apiVersionState.setVersion(this.packageVersion);
                } else {
                    //如果package的版本不存在，设置成默认1
                    apiVersionState.setVersion(1);
                }
            } else {//如果当前存在注解
                //如果当前版本号为0，说明没有设置版本
                if (apiVersion.value() == 0) {
                    //判断当前package的版本是否存在，如果存在设置。
                    if (this.packageVersion != null) {
                        apiVersionState.setVersion(this.packageVersion);
                    } else {//如果package的版本不存在，注解的版本也不存在，设置默认为1
                        apiVersionState.setVersion(1);
                    }
                } else {
                    //如果当前版本号不为0，说明设置了版本，设置即可
                    apiVersionState.setVersion(apiVersion.value());
                }
            }
        }

        private void initDiscard() {
            //如果当前存在注解，并且允许覆盖父级废弃功能
            if (apiVersion != null && apiVersion.overrideDiscard()) {
                apiVersionState.setDiscard(apiVersion.discard());
            } else {
                //如果不允许覆盖，使用最低版本判断是否应该废弃
                if (apiVersionState.getVersion() < minimumVersion) {
                    apiVersionState.setDiscard(true);
                }
            }
        }

        public ApiVersionState build() {
            //初始化接口版本
            initVersion();
            //初始化废弃接口
            initDiscard();
            return apiVersionState;
        }
    }
}
