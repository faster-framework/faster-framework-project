package cn.org.faster.framework.grpc.spring.boot.autoconfigure;

import cn.org.faster.framework.grpc.client.annotation.GrpcClientScan;
import cn.org.faster.framework.grpc.client.factory.ClientFactory;
import cn.org.faster.framework.grpc.server.annotation.GrpcServerScan;
import cn.org.faster.framework.grpc.spring.boot.autoconfigure.properties.GrpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ConditionalOnProperty(prefix = "faster.grpc.client", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GrpcClientScan
    public static class GrpcClientAutoConfiguration {
        @Autowired
        private GrpcProperties grpcProperties;

        @Bean
        public ClientFactory clientFactory() {
            ClientFactory clientFactory = new ClientFactory();
            clientFactory.setServerChannelMap(grpcProperties.getClient().getServices());
            return clientFactory;
        }
    }

    @ConditionalOnProperty(prefix = "faster.grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GrpcServerScan
    public static class GrpcServerAutoConfiguration {

    }
}
