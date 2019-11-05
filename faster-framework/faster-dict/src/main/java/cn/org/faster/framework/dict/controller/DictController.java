package cn.org.faster.framework.dict.controller;

import cn.org.faster.framework.dict.entity.SysDict;
import cn.org.faster.framework.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author faster-builder
 * 字典Controller
 */
@RestController
@RequestMapping("/{version}/basic/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * @param type 字典类型
     * @return 根据字典类型返回字典列表
     */
    @GetMapping("/{type}")
    public ResponseEntity list(@PathVariable String type) {
        SysDict sysDict = new SysDict();
        sysDict.setType(type);
        return ResponseEntity.ok(dictService.listAll(sysDict));
    }


    /**
     * @param type 字典类型
     * @param name 字典名称
     * @return 根据字典类型和字典名称返回字典列表
     */
    @GetMapping("/{type}/{name}")
    public ResponseEntity list(@PathVariable String type, @PathVariable String name) {
        SysDict sysDict = new SysDict();
        sysDict.setType(type);
        sysDict.setName(name);
        return ResponseEntity.ok(dictService.query(sysDict));
    }

}