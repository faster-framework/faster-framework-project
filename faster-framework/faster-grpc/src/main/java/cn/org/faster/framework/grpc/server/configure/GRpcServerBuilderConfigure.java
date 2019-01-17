package cn.org.faster.framework.grpc.server.configure;

import cn.org.faster.framework.grpc.server.adapter.BindServiceAdapter;
import io.grpc.ServerBuilder;
import lombok.Data;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/1/17
 */
@Data
public abstract class GRpcServerBuilderConfigure {
    private final ServerBuilder serverBuilder;
    private final List<BindServiceAdapter> bindServiceAdapterList;
    private final int port;

    public GRpcServerBuilderConfigure(List<BindServiceAdapter> bindServiceAdapterList, int port) {
        this.bindServiceAdapterList = bindServiceAdapterList;
        this.port = port;
        this.serverBuilder = ServerBuilder.forPort(port);
    }

    public ServerBuilder serverBuilder() {
        configure(serverBuilder);
        return this.serverBuilder;
    }

    public abstract void configure(ServerBuilder serverBuilder);
}
