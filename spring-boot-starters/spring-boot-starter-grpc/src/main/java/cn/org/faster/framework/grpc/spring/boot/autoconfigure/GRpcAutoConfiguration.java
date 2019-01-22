package cn.org.faster.framework.grpc.spring.boot.autoconfigure;

import cn.org.faster.framework.grpc.client.annotation.GRpcClientScan;
import cn.org.faster.framework.grpc.client.factory.ClientFactory;
import cn.org.faster.framework.grpc.core.exception.CreateMarshallerException;
import cn.org.faster.framework.grpc.core.factory.JacksonMarshallerFactory;
import cn.org.faster.framework.grpc.core.factory.MarshallerFactory;
import cn.org.faster.framework.grpc.server.adapter.DefaultServerBuilderConfigureAdapter;
import cn.org.faster.framework.grpc.server.annotation.GRpcServerScan;
import cn.org.faster.framework.grpc.server.configure.GRpcServerBuilderConfigure;
import cn.org.faster.framework.grpc.server.processor.GRpcServiceProcessor;
import cn.org.faster.framework.grpc.server.run.GRpcServerApplicationRunner;
import cn.org.faster.framework.grpc.spring.boot.autoconfigure.properties.GRpcProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@EnableConfigurationProperties(GRpcProperties.class)
@ConditionalOnProperty(prefix = "faster.grpc", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({GRpcAutoConfiguration.GrpcClientAutoConfiguration.class, GRpcAutoConfiguration.GrpcServerAutoConfiguration.class})
public class GRpcAutoConfiguration {

    /**
     *
     * @param objectMapper jackson操作工具类
     * @return jackson序列化工厂
     */
    @Bean
    @ConditionalOnMissingBean
    public MarshallerFactory marshallerFactory(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            throw new CreateMarshallerException("Object mapper is no inject in spring.Please check your configuration.");
        }
        return new JacksonMarshallerFactory(objectMapper);
    }

    /**
     * 客户端配置
     */
    @ConditionalOnProperty(prefix = "faster.grpc.client", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GRpcClientScan
    public static class GrpcClientAutoConfiguration {
        @Autowired
        private GRpcProperties grpcProperties;

        @Bean
        @ConditionalOnMissingBean
        public ClientFactory clientFactory(MarshallerFactory marshallerFactory) {
            ClientFactory clientFactory = new ClientFactory(marshallerFactory);
            clientFactory.setServerChannelMap(grpcProperties.getClient().getServices());
            return clientFactory;
        }
    }

    /**
     * 服务端配置
     */
    @ConditionalOnProperty(prefix = "faster.grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GRpcServerScan
    public static class GrpcServerAutoConfiguration {
        @Autowired
        private GRpcProperties grpcProperties;

        /**
         * server builder 配置
         *
         * @param grpcServiceProcessor grpcServiceProcessor
         * @return server builder 配置
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServerBuilderConfigure grpcServerBuilderConfigure(GRpcServiceProcessor grpcServiceProcessor) {
            return new DefaultServerBuilderConfigureAdapter(grpcServiceProcessor.getBindServiceAdapterList(), grpcProperties.getServer().getPort());
        }

        /**
         * 扫描grpc server 服务
         *
         * @param marshallerFactory 序列化处理器
         * @return GRpcServiceProcessor
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServiceProcessor grpcServiceProcessor(MarshallerFactory marshallerFactory) {
            return new GRpcServiceProcessor(marshallerFactory);
        }

        /**
         * grpc 服务
         *
         * @param grpcServerBuilderConfigure grpcServerBuilderConfigure
         * @return 服务
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServerApplicationRunner grpcServerApplicationRunner(GRpcServerBuilderConfigure grpcServerBuilderConfigure) {
            return new GRpcServerApplicationRunner(grpcServerBuilderConfigure);
        }
    }
}
