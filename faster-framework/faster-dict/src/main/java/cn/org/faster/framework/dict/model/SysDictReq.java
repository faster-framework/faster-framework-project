package cn.org.faster.framework.dict.model;

import lombok.Data;

/**
 * @author zhangbowen
 * @since 2019-10-31
 */
@Data
public class SysDictReq {
    /**
     * 字典key
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 备注
     */
    private String remark;
}
