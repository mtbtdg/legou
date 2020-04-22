package cn.zxJava.controller;

import cn.zxJava.service.ItempageService;
import cn.zxJava.domain.TbItem;
import cn.zxJava.entity.Result;
import cn.zxJava.groupentity.Goods;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itempage")
public class ItempageController {
    @Reference
    private ItempageService itempageService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /*
    *传入spu的主键值 生成商品详情页（可能有多个sku）
    * */
    @RequestMapping("/genHtmlByGoodsId/{tbGoodsId}")
    public Result genHtmlByGoodsId(@PathVariable("tbGoodsId") Long tbGoodsId){
        try {
            Goods goods = itempageService.findByGoodsId(tbGoodsId);
            //生成详情页
            //获取配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //从goods组合类中获取sku的集合
            List<TbItem> itemList = goods.getItemList();
            //遍历集合
            for (TbItem tbItem : itemList) {
                Map map = new HashMap();
                map.put("goods",goods);
                map.put("tbItem",tbItem);
                //创建输出流
                FileWriter fileWriter = new FileWriter(new File("E:\\htmls\\"+tbItem.getId()+".html"));
                //输出文件
                template.process(map,fileWriter);
                //关闭流
                fileWriter.close();
            }

            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }

    /*
    * 初始化所有的页面详情页
    * */
    @RequestMapping("/genHtmls")
    public Result genHtmls(){
        try {
            //获取配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //查询所有的spu
            List<Goods> goodsList =  itempageService.findGoodsAll();
            for (Goods goods : goodsList) {
                //生成详情页
                //从goods组合类中获取sku的集合
                List<TbItem> itemList = goods.getItemList();
                //遍历集合
                for (TbItem tbItem : itemList) {
                    Map map = new HashMap();
                    map.put("goods",goods);
                    map.put("tbItem",tbItem);
                    //创建输出流
                    FileWriter fileWriter = new FileWriter(new File("E:\\htmls\\"+tbItem.getId()+".html"));
                    //输出文件
                    template.process(map,fileWriter);
                    //关闭流
                    fileWriter.close();
                }
            }


            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }

}
