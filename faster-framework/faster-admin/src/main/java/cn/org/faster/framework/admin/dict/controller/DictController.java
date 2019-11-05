package cn.org.faster.framework.admin.dict.controller;

import cn.org.faster.framework.admin.dict.entity.SysDict;
import cn.org.faster.framework.admin.dict.model.SysDictReq;
import cn.org.faster.framework.admin.dict.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author faster-builder
 * 字典Controller
 */
@RestController
@RequestMapping("/sys/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 字典分页列表
     *
     * @param sysDict 字典实体
     * @return ResponseEntity
     */
    @GetMapping
    @RequiresPermissions("dict:list")
    public ResponseEntity list(SysDict sysDict) {
        return ResponseEntity.ok(dictService.list(sysDict));
    }

    /**
     * 字典根据id查询详情
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    @RequiresPermissions("dict:info")
    public ResponseEntity queryById(@PathVariable Long id) {
        return ResponseEntity.ok(dictService.queryById(id));
    }

    /**
     * 字典根据条件查询详情
     * @param sysDict 请求
     * @return ResponseEntity
     */
    @GetMapping("/query")
    @RequiresPermissions("dict:info")
    public ResponseEntity query(SysDict sysDict) {
        return ResponseEntity.ok(dictService.query(sysDict));
    }

    /**
     * 新增字典
     *
     * @param request 请求参数
     * @return ResponseEntity
     */
    @PostMapping
    @RequiresPermissions("dict:add")
    public ResponseEntity add(@Validated @RequestBody SysDictReq request) {
        SysDict insert = new SysDict();
        BeanUtils.copyProperties(request, insert);
        return dictService.add(insert);
    }

    /**
     * 更新字典
     *
     * @param request 请求参数
     * @param id 主键id
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    @RequiresPermissions("dict:modify")
    public ResponseEntity update(@RequestBody SysDictReq request, @PathVariable Long id) {
        SysDict update = new SysDict();
        BeanUtils.copyProperties(request, update);
        update.setId(id);
        return dictService.update(update);
    }

    /**
     * 删除字典
     *
     * @param ids 主键id列表
     * @return ResponseEntity
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("dict:delete")
    public ResponseEntity delete(@RequestBody List<Long> ids) {
        ids.forEach(item -> {
            if (item == 0L) {
                return;
            }
            dictService.delete(item);
        });
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}