package cn.org.faster.framework.core.upload.error;

import cn.org.faster.framework.core.exception.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangbowen
 */
@Getter
@AllArgsConstructor
public enum UploadError implements ErrorCode {
    SIGN_ERROR(1010, "签名错误"),
    SIGN_TIME_OUT(1011, "签名超时");
    private int value;
    private String description;
}
