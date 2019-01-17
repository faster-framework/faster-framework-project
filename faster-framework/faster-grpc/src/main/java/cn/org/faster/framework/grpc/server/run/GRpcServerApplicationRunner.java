package cn.org.faster.framework.grpc.server.run;

import cn.org.faster.framework.grpc.server.adapter.BindServiceAdapter;
import cn.org.faster.framework.grpc.server.configure.GRpcServerBuilderConfigure;
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
public class GRpcServerApplicationRunner implements ApplicationRunner, DisposableBean {
    private final GRpcServerBuilderConfigure serverBuilderConfigure;
    private Server server;

    public GRpcServerApplicationRunner(GRpcServerBuilderConfigure serverBuilderConfigure) {
        this.serverBuilderConfigure = serverBuilderConfigure;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<BindServiceAdapter> bindServiceAdapterList = serverBuilderConfigure.getBindServiceAdapterList();
        if (CollectionUtils.isEmpty(bindServiceAdapterList)) {
            log.info("GRpc server services empty.GRpc server is not start.");
            return;
        }
        ServerBuilder serverBuilder = serverBuilderConfigure.serverBuilder();
        for (BindServiceAdapter bindServiceAdapter : bindServiceAdapterList) {
            serverBuilder.addService(bindServiceAdapter);
        }
        this.server = serverBuilder.build().start();
        startDaemonAwaitThread();
        log.info("GRpc start success, listening on port {}.", serverBuilderConfigure.getPort());
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> {
            try {
                GRpcServerApplicationRunner.this.server.awaitTermination();
            } catch (InterruptedException e) {
                log.warn("GRpc server stopped." + e.getMessage());
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    @Override
    public void destroy() {
        log.info("Shutting down GRpc server ...");
        Optional.ofNullable(this.server).ifPresent(Server::shutdown);
        log.info("GRpc server stopped.");
    }
}
