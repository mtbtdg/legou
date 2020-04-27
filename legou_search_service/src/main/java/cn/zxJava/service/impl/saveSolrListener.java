package cn.zxJava.service.impl;

import cn.zxJava.domain.TbItem;
import cn.zxJava.domain.TbItemExample;
import cn.zxJava.mapper.TbItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

public class saveSolrListener implements MessageListener {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
            Long tbGoodsId = Long.parseLong(msg.getText());
            TbItemExample tbItemExample = new TbItemExample();
            tbItemExample.createCriteria().andGoodsIdEqualTo(tbGoodsId);
            List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
            solrTemplate.saveBeans(tbItemList);
            solrTemplate.commit();
            System.out.println("sku数据存入solr库");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
