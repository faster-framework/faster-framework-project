package cn.org.faster.framework.web.version.exception;

/**
 * @author zhangbowen
 * @since 2019/1/1
 */
public class ApiVersionDiscardException extends RuntimeException {
    public ApiVersionDiscardException(String message) {
        super(message);
    }
}
