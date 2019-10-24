package cn.org.faster.framework.admin.dict.controller;

import cn.org.faster.framework.admin.dict.entity.Dict;
import cn.org.faster.framework.admin.dict.service.DictService;
import lombok.AllArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author faster-builder
 * 字典Controller
 */
@RestController
@RequestMapping("/sys/dict")
@AllArgsConstructor
public class DictController {
    private DictService dictService;

    /**
     * 字典分页列表
     *
     * @param dict 字典实体
     * @return ResponseEntity
     */
    @GetMapping
    @RequiresPermissions("dict:list")
    public ResponseEntity list(Dict dict) {
        return ResponseEntity.ok(dictService.list(dict));
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
     *
     * @return ResponseEntity
     */
    @GetMapping("/query")
    @RequiresPermissions("dict:info")
    public ResponseEntity query(Dict dict) {
        return ResponseEntity.ok(dictService.query(dict));
    }

    /**
     * 新增字典
     *
     * @param request 请求参数
     * @return ResponseEntity
     */
    @PostMapping
    @RequiresPermissions("dict:add")
    public ResponseEntity add(@Validated @RequestBody Dict request) {
        return dictService.add(request);
    }

    /**
     * 更新字典
     *
     * @param request 请求参数
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    @RequiresPermissions("dict:modify")
    public ResponseEntity update(@RequestBody Dict request) {
        return dictService.update(request);
    }

    /**
     * 删除字典
     *
     * @param id 主键id
     * @return ResponseEntity
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions("dict:delete")
    public ResponseEntity delete(@PathVariable Long id) {
        return dictService.delete(id);
    }
}