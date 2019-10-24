package cn.org.faster.framework.admin.dict.service;

import cn.org.faster.framework.admin.dict.entity.Dict;
import cn.org.faster.framework.admin.dict.mapper.DictMapper;
import cn.org.faster.framework.mybatis.entity.BaseEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author faster-builder
 * 字典Service
 */
@Service
@Transactional
@AllArgsConstructor
public class DictService extends ServiceImpl<DictMapper, Dict> {

    /**
     * 分页查询
     *
     * @param dict 请求参数
     * @return 字典分页列表
     */
    public IPage<Dict> list(Dict dict) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(dict.getName())) {
            queryWrapper.like(Dict::getName, dict.getName());
        }
        if (!StringUtils.isEmpty(dict.getType())) {
            queryWrapper.like(Dict::getType, dict.getType());
        }
        if (!StringUtils.isEmpty(dict.getDictValue())) {
            queryWrapper.eq(Dict::getDictValue, dict.getDictValue());
        }
        if (dict.getShowStatus() != null) {
            queryWrapper.eq(Dict::getShowStatus, dict.getShowStatus());
        }
        queryWrapper.orderByAsc(BaseEntity::getSort);
        return super.baseMapper.selectPage(dict.toPage(), queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param dict 请求参数
     * @return 字典分页列表
     */
    public List<Dict> listAll(Dict dict) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(dict.getName())) {
            queryWrapper.eq(Dict::getName, dict.getName());
        }
        if (!StringUtils.isEmpty(dict.getType())) {
            queryWrapper.eq(Dict::getType, dict.getType());
        }
        if (!StringUtils.isEmpty(dict.getDictValue())) {
            queryWrapper.eq(Dict::getDictValue, dict.getDictValue());
        }
        if (dict.getShowStatus() != null) {
            queryWrapper.eq(Dict::getShowStatus, dict.getShowStatus());
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
    public Dict queryById(Long id) {
        return super.baseMapper.selectById(id);
    }

    /**
     * 根据条件查询详情
     *
     * @param dict 请求参数
     * @return 字典详情
     */
    public Dict query(Dict dict) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(dict.getName())) {
            queryWrapper.eq(Dict::getName, dict.getName());
        }
        if (!StringUtils.isEmpty(dict.getType())) {
            queryWrapper.eq(Dict::getType, dict.getType());
        }
        if (!StringUtils.isEmpty(dict.getDictValue())) {
            queryWrapper.eq(Dict::getDictValue, dict.getDictValue());
        }
        if (dict.getShowStatus() != null) {
            queryWrapper.eq(Dict::getShowStatus, dict.getShowStatus());
        }
        return this.getOne(queryWrapper);
    }

    /**
     * 添加字典表
     *
     * @param dict 实体
     * @return ResponseEntity
     */
    public ResponseEntity add(Dict dict) {
        dict.preInsert();
        super.baseMapper.insert(dict);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 修改字典表
     *
     * @param dict 实体
     * @return ResponseEntity
     */
    public ResponseEntity update(Dict dict) {
        dict.preUpdate();
        super.baseMapper.updateById(dict);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    /**
     * 删除字典表
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    public ResponseEntity delete(Long id) {
        super.baseMapper.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}