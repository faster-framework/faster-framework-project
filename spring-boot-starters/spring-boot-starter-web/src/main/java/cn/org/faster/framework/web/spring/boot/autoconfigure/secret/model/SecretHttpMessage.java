package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;

/**
 * @author zhangbowen
 * @since 2018/12/13
 */
@AllArgsConstructor
@NoArgsConstructor
public class SecretHttpMessage implements HttpInputMessage {
    private InputStream body;
    private HttpHeaders httpHeaders;

    @Override
    public InputStream getBody() {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
