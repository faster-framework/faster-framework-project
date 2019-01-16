package cn.org.faster.framework.grpc.client.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class GrpcChannelCreateException extends RuntimeException{
    public GrpcChannelCreateException(String message) {
        super(message);
    }
}
