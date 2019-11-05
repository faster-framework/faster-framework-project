package cn.org.faster.framework.dict.facade;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.dict.entity.SysDict;
import cn.org.faster.framework.dict.service.DictService;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 * @since 2019-11-05
 */
public class DictFacade {
    
    public DictFacade(boolean cache, DictService dictService) {
        DictFacade.cache = false;
        DictFacade.dictService = dictService;
    }

    public static boolean cache = false;
    public static DictService dictService;
    private static final String CACHE_DICT_KEY = "dict:";
    private static final long CACHE_TIME = 60 * 60 * 24;


    /**
     * @return 全部字典
     */
    public static List<SysDict> listAll() {
        if (cache) {
            List<SysDict> list = CacheFacade.get(CACHE_DICT_KEY, new TypeReference<List<SysDict>>() {
            });
            if (CollectionUtils.isEmpty(list)) {
                list = dictService.listAll(new SysDict());
                CacheFacade.set(CACHE_DICT_KEY, list, CACHE_TIME);
            }
            return list;
        }
        return dictService.listAll(new SysDict());
    }

    /**
     * @param id 根据主键id获取字典
     * @return 获取单个字典
     */
    public static SysDict byId(Long id) {
        return listAll().stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * @param type 类型
     * @param name 名称
     * @return 根据类型、名称获取字典
     */
    public static SysDict byTypeAndName(String type, String name) {
        return listAll().stream().filter(item -> item.getType().equals(type) && item.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * @param type 类型
     * @return 根据类型获取字典列表
     */
    public static List<SysDict> byType(String type) {
        return listAll().stream().filter(item -> item.getType().equals(type)).collect(Collectors.toList());
    }
}
