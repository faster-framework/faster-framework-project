package cn.org.faster.framework.web.upload.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhangbowen
 */
@Data
@AllArgsConstructor
public class UploadToken {
    private String sign;
    private Long timestamp;
}
