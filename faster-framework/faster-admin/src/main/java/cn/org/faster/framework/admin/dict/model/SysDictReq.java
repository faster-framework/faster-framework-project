package cn.org.faster.framework.admin.dict.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 * @since 2019-10-31
 */
@Data
public class SysDictReq {
    /**
     * 字典key
     */
    @NotBlank(message = "请输入字典名称")
    private String name;
    /**
     * 类型
     */
    @NotBlank(message = "请输入字典类型")
    private String type;
    /**
     * 字典值
     */
    @NotBlank(message = "请输入字典值")
    private String dictValue;

    private String remark;
}
