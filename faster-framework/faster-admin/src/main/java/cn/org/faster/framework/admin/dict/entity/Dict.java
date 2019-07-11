package cn.org.faster.framework.admin.dict.entity;

import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author faster-builder
 * 字典表(帮助中心、公告、关于我们、客服电话、ios是否上架、android是否上架、提成比例) 实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class Dict extends BaseEntity {
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