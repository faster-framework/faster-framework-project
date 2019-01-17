package cn.org.faster.framework.grpc.spring.boot.autoconfigure;

import cn.org.faster.framework.grpc.client.annotation.GrpcClientScan;
import cn.org.faster.framework.grpc.client.factory.ClientFactory;
import cn.org.faster.framework.grpc.server.annotation.GrpcServerScan;
import cn.org.faster.framework.grpc.server.processor.GrpcServiceProcessor;
import cn.org.faster.framework.grpc.server.run.GrpcServerApplicationRunner;
import cn.org.faster.framework.grpc.spring.boot.autoconfigure.properties.GrpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
@Configuration
@EnableConfigurationProperties(GrpcProperties.class)
@ConditionalOnProperty(prefix = "faster.grpc", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({GrpcAutoConfiguration.GrpcClientAutoConfiguration.class, GrpcAutoConfiguration.GrpcServerAutoConfiguration.class})
public class GrpcAutoConfiguration {

    /**
     * 客户端配置
     */
    @ConditionalOnProperty(prefix = "faster.grpc.client", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GrpcClientScan
    public static class GrpcClientAutoConfiguration {
        @Autowired
        private GrpcProperties grpcProperties;

        @Bean
        @ConditionalOnMissingBean
        public ClientFactory clientFactory() {
            ClientFactory clientFactory = new ClientFactory();
            clientFactory.setServerChannelMap(grpcProperties.getClient().getServices());
            return clientFactory;
        }
    }

    /**
     * 服务端配置
     */
    @ConditionalOnProperty(prefix = "faster.grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GrpcServerScan
    public static class GrpcServerAutoConfiguration {
        @Autowired
        private GrpcProperties grpcProperties;

        /**
         * 扫描grpcMethod
         *
         * @return GrpcServiceProcessor
         */
        @Bean
        @ConditionalOnMissingBean
        public GrpcServiceProcessor grpcServiceProcessor() {
            return new GrpcServiceProcessor();
        }

        /**
         * grpc 服务
         *
         * @param grpcServiceProcessor grpcServiceProcessor
         * @return 服务
         */
        @Bean
        @ConditionalOnMissingBean
        public GrpcServerApplicationRunner grpcServerApplicationRunner(GrpcServiceProcessor grpcServiceProcessor) {
            return new GrpcServerApplicationRunner(grpcServiceProcessor.getBindServiceAdapterList(), grpcProperties.getServer().getPort());
        }
    }
}
