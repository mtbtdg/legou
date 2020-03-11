package cn.tx.controller;

import cn.tx.entity.Result;
import cn.tx.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {

    // 注入
    @Reference
    private ItemSearchService itemSearchService;

    /**
     * 搜索
     * @param paramMap
     * @return
     */
    @RequestMapping("/search")
    public Result search(@RequestBody Map paramMap){
        try {
            // 搜索数据
            Map resultMap = itemSearchService.search(paramMap);
            return new Result(true,"查询成功",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }

}
