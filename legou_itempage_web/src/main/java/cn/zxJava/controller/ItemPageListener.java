package cn.zxJava.controller;

import cn.zxJava.domain.TbItem;
import cn.zxJava.groupentity.Goods;
import cn.zxJava.service.ItempageService;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPageListener implements MessageListener {
    @Reference
    private ItempageService itempageService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            Long spuId = Long.parseLong(textMessage.getText());
            Goods goods = itempageService.findByGoodsId(spuId);
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
