package cn.org.faster.framework.grpc.server.run;

import cn.org.faster.framework.grpc.server.adapter.BindServiceAdapter;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author zhangbowen
 * @since 2019/1/17
 */
@Slf4j
public class GrpcServerApplicationRunner implements ApplicationRunner, DisposableBean {
    private final List<BindServiceAdapter> bindServiceAdapterList;
    private final int port;
    private Server server;

    public GrpcServerApplicationRunner(List<BindServiceAdapter> bindServiceAdapterList, int port) {
        this.bindServiceAdapterList = bindServiceAdapterList;
        this.port = port;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (CollectionUtils.isEmpty(bindServiceAdapterList)) {
            log.warn("gRPC server service is empty.gRPC server is not start.");
            return;
        }
        ServerBuilder serverBuilder = ServerBuilder.forPort(port);
        for (BindServiceAdapter bindServiceAdapter : bindServiceAdapterList) {
            serverBuilder.addService(bindServiceAdapter);
        }
        this.server = serverBuilder.build().start();
        startDaemonAwaitThread();
        log.info("gRPC start success, listening on port {}.", this.port);
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> {
            try {
                GrpcServerApplicationRunner.this.server.awaitTermination();
            } catch (InterruptedException e) {
                log.warn("gRPC server stopped." + e.getMessage());
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() {
        log.info("Shutting down gRPC server ...");
        Optional.ofNullable(this.server).ifPresent(Server::shutdown);
        log.info("gRPC server stopped.");
    }
}
