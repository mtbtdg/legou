package cn.zxJava.service.impl;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.domain.TbGoodsDesc;
import cn.zxJava.domain.TbItem;
import cn.zxJava.groupentity.Goods;
import cn.zxJava.mapper.*;
import cn.zxJava.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class GoodsServiceImpl implements GoodsService{
    @Autowired
    private TbGoodsMapper tbGoodsMapper;

    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbSellerMapper tbSellerMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public void add(Goods goods) {
        //获取spu对象
        TbGoods tbGoods = goods.getTbGoods();
        //设置tbGoods的默认的属性值
        //设置是否已上架
        tbGoods.setIsMarketable("0");
        //设置是否已审批
        tbGoods.setAuditStatus("0");
        //保存spu对象
        tbGoodsMapper.insert(tbGoods);

        //保存spu描述信息对象
        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        tbGoodsDescMapper.insert(tbGoodsDesc);

        //保存sku信息
        List<TbItem> itemList = goods.getItemList();
        if (itemList != null && itemList.size() > 0){
            //遍历sku对象
            for (TbItem tbItem : itemList) {
                //设置属性值

                //设置商家名称，通过tbGoods获取
                tbItem.setSeller(tbSellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getName());

                //设置商家主键
                tbItem.setSellerId(tbGoods.getSellerId());

                //设置品牌
                tbItem.setBrand(brandMapper.findOne(tbGoods.getBrandId()).getName());

                //设置分类名称
                tbItem.setCategory(tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName());

                //获取图片，只获取一张图片
                String itemImages = tbGoodsDesc.getItemImages();
                if (itemImages != null && !itemImages.isEmpty()){
                    List<Map> list = JSON.parseArray(itemImages, Map.class);
                    Map map = list.get(0);
                    String url = (String) map.get("url");
                    //设置图片
                    tbItem.setImage(url);
                }

                //设置标题 标题=spu的名称+规格项
                //获取spu名称
                String goodsName = tbGoods.getGoodsName();
                //获取spec的值
                String spec = tbItem.getSpec();
                Map map = JSON.parseObject(spec, Map.class);
                //遍历map
                for (Object key : map.keySet()) {
                   goodsName += " " + key + "" +map.get(key) + "";
                }
                tbItem.setTitle(goodsName);

                //设置时间
                tbItem.setCreateTime(new Date());
                tbItem.setUpdateTime(new Date());

                //设置分类id
                tbItem.setCategoryid(tbGoods.getCategory3Id());

                //设置spu的id
                tbItem.setGoodsId(tbGoods.getId());

                //把sku存入数据库
                tbItemMapper.insert(tbItem);
            }
        }
    }

    //分页展示
    @Override
    public PageInfo<TbGoods> findPage(int pageNum, int pageCode) {
        PageHelper.startPage(pageNum,pageCode);
        List<TbGoods> tbGoodsList = tbGoodsMapper.selectByExample(null);
        return new PageInfo<>(tbGoodsList);
    }

    //商品上架
    @Override
    public void updateGoods(String val, Long[] ids) {
        for (Long id : ids) {
            TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
            tbGoods.setIsMarketable(val);
            tbGoodsMapper.updateByPrimaryKey(tbGoods);
        }
    }
}
