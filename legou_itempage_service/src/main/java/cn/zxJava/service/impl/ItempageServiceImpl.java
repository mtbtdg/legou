package cn.zxJava.service.impl;

import cn.zxJava.service.ItempageService;
import cn.zxJava.domain.TbGoods;
import cn.zxJava.domain.TbItem;
import cn.zxJava.domain.TbItemExample;
import cn.zxJava.groupentity.Goods;
import cn.zxJava.mapper.TbGoodsDescMapper;
import cn.zxJava.mapper.TbGoodsMapper;
import cn.zxJava.mapper.TbItemCatMapper;
import cn.zxJava.mapper.TbItemMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItempageServiceImpl implements ItempageService {

    @Autowired
    private TbGoodsMapper tbGoodsMapper;
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public Goods findByGoodsId(Long goodsId) {

        Goods goods = new Goods();

        //spu对象
        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
        goods.setTbGoods(tbGoods);
        //spu描述对象
        goods.setTbGoodsDesc(tbGoodsDescMapper.selectByPrimaryKey(goodsId));
        //sku对象集合
        TbItemExample tbItemExample =  new TbItemExample();
        tbItemExample.createCriteria().andGoodsIdEqualTo(goodsId);
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);
        goods.setItemList(tbItemList);
        //分类的名称
        Map<String,String> categoryMap = new HashMap<>();
        categoryMap.put("category1",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName());
        categoryMap.put("category2",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName());
        categoryMap.put("category3",tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());
        goods.setCategoryMap(categoryMap);

        return goods;
    }

    @Override
    public List<Goods> findGoodsAll() {
        List<Goods> goodsList = new ArrayList<>();

        List<TbGoods> tbGoods = tbGoodsMapper.selectByExample(null);
        for (TbGoods tbGood : tbGoods) {
            Goods goods = findByGoodsId(tbGood.getId());
            goodsList.add(goods);
        }

        return goodsList;
    }
}
