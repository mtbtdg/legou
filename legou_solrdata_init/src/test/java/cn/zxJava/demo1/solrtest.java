package cn.zxJava.demo1;

import cn.zxJava.domain.TbItem;
import cn.zxJava.mapper.TbItemMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext*.xml")
public class solrtest {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper tbItemMapper;

    //删除solr库中的所有的数据
    @Test
    public void testDeleteAll(){
        //创建查询对象
        //第一个*表示所有的业务域，第二个*表示所有的值
        SolrDataQuery query = new SimpleQuery("*:*");
        //删除多条数据
        solrTemplate.delete(query);
        //提交事务
        solrTemplate.commit();
    }

    //向solr库中添加或修改数据
    @Test
    public void testInsert(){
        TbItem tbItem = new TbItem();
        //如果添加的数据id值solr库中存在，则会走修改方法，与其他值无关
        tbItem.setId(2L);
        tbItem.setCategory("手机");
        tbItem.setTitle("华为牛逼");
        tbItem.setImage("图片");
        tbItem.setBrand("华为");
        tbItem.setSeller("翔哥华为专卖");
        tbItem.setPrice(new BigDecimal(3999.99));

        //保存一条数据
        solrTemplate.saveBean(tbItem);
        //批量保存数据
        //solrTemplate.saveBeans();

        //提交事务
        solrTemplate.commit();
    }

    //从solr库中删除指定数据
    @Test
    public void testDeleteOne(){

        solrTemplate.deleteById("2");
        //提交事务
        solrTemplate.commit();
    }

    //
    @Test
    public void tbItemDataInit(){

        List<TbItem> tbItems = tbItemMapper.selectByExample(null);

        solrTemplate.saveBeans(tbItems);

        //提交事务
        solrTemplate.commit();
    }

}
