package cn.org.faster.framework.dict.service;

import cn.org.faster.framework.dict.entity.SysDict;
import cn.org.faster.framework.dict.mapper.DictMapper;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author faster-builder
 * 字典Service
 */
@Transactional
@Service
public class DictService extends ServiceImpl<DictMapper, SysDict> {

    /**
     * 分页查询
     *
     * @param sysDict 请求参数
     * @return 字典分页列表
     */
    public List<SysDict> listAll(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysDict.getName())) {
            queryWrapper.eq(SysDict::getName, sysDict.getName());
        }
        if (!StringUtils.isEmpty(sysDict.getType())) {
            queryWrapper.eq(SysDict::getType, sysDict.getType());
        }
        if (!StringUtils.isEmpty(sysDict.getDictValue())) {
            queryWrapper.eq(SysDict::getDictValue, sysDict.getDictValue());
        }
        if (sysDict.getShowStatus() != null) {
            queryWrapper.eq(SysDict::getShowStatus, sysDict.getShowStatus());
        }
        queryWrapper.orderByAsc(BaseEntity::getSort);
        return super.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据主键id查询详情
     *
     * @param id 字典id
     * @return 字典详情
     */
    public SysDict queryById(Long id) {
        return super.baseMapper.selectById(id);
    }

    /**
     * 根据条件查询详情
     *
     * @param sysDict 请求参数
     * @return 字典详情
     */
    public SysDict query(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(sysDict.getName())) {
            queryWrapper.eq(SysDict::getName, sysDict.getName());
        }
        if (!StringUtils.isEmpty(sysDict.getType())) {
            queryWrapper.eq(SysDict::getType, sysDict.getType());
        }
        if (!StringUtils.isEmpty(sysDict.getDictValue())) {
            queryWrapper.eq(SysDict::getDictValue, sysDict.getDictValue());
        }
        if (sysDict.getShowStatus() != null) {
            queryWrapper.eq(SysDict::getShowStatus, sysDict.getShowStatus());
        }
        return this.getOne(queryWrapper);
    }
}