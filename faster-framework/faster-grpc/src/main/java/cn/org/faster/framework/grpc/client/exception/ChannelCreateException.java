package cn.org.faster.framework.grpc.client.exception;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
public class ChannelCreateException extends RuntimeException{
    public ChannelCreateException(String message) {
        super(message);
    }
}
