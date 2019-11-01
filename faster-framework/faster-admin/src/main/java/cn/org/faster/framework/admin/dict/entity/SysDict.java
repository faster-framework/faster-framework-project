package cn.org.faster.framework.admin.dict.entity;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author faster-builder
 * 字典实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class SysDict extends BaseEntity {
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
     * 展示状态（0.不展示1.展示）
     */
    private Integer showStatus;
}