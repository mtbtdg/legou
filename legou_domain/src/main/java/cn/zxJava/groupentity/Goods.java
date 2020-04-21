package cn.zxJava.groupentity;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.domain.TbGoodsDesc;
import cn.zxJava.domain.TbItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Goods implements Serializable {
    //spu
    private TbGoods tbGoods;
    //spu详细信息
    private TbGoodsDesc tbGoodsDesc;
    //多个sku
    private List<TbItem> itemList;
    //分类
    private Map<String,String> categoryMap;

    public Map<String, String> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, String> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public TbGoods getTbGoods() {
        return tbGoods;
    }

    public void setTbGoods(TbGoods tbGoods) {
        this.tbGoods = tbGoods;
    }

    public TbGoodsDesc getTbGoodsDesc() {
        return tbGoodsDesc;
    }

    public void setTbGoodsDesc(TbGoodsDesc tbGoodsDesc) {
        this.tbGoodsDesc = tbGoodsDesc;
    }

    public List<TbItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<TbItem> itemList) {
        this.itemList = itemList;
    }
}
