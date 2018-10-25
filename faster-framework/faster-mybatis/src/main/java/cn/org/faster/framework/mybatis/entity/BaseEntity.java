package cn.org.faster.framework.mybatis.entity;

import cn.org.faster.framework.core.utils.sequence.Sequence;
import cn.org.faster.framework.web.web.context.WebContextFacade;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhangbowen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity implements Serializable {
    private Long id;
    private Long createBy;
    private Long updateBy;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer sort;
    private String remark;
    @TableLogic
    private Integer deleted;
    //页数
    @TableField(exist = false)
    private Integer current = 1;
    //每页数据，默认10条
    @TableField(exist = false)
    private Integer size = 10;

    public <T> IPage<T> toPage() {
        return new Page<>(current, size);
    }


    /**
     * 预插入方法
     */
    public void preInsert() {
        this.id = Sequence.next();
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        Long userId = WebContextFacade.getRequestContext().getUserId();
        this.createBy = userId;
        this.updateBy = userId;
        this.deleted = 0;
        if (this.sort == null) {
            this.sort = 0;
        }
    }

    /**
     * 预更新方法
     */
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
        this.updateBy = WebContextFacade.getRequestContext().getUserId();
    }
}
