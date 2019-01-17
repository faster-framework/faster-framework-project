package cn.org.faster.framework.grpc.client.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GRpcChannelCreateException extends RuntimeException{
    public GRpcChannelCreateException(String message) {
        super(message);
    }
}
