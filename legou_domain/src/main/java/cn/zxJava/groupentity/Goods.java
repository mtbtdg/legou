package cn.zxJava.groupentity;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.domain.TbGoodsDesc;
import cn.zxJava.domain.TbItem;

import java.io.Serializable;
import java.util.List;

public class Goods implements Serializable {
    //spu
    private TbGoods tbGoods;
    //spu详细信息
    private TbGoodsDesc tbGoodsDesc;
    //多个sku
    private List<TbItem> tbItemList;

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

    public List<TbItem> getTbItemList() {
        return tbItemList;
    }

    public void setTbItemList(List<TbItem> tbItemList) {
        this.tbItemList = tbItemList;
    }

    public Goods(TbGoods tbGoods, TbGoodsDesc tbGoodsDesc, List<TbItem> tbItemList) {
        this.tbGoods = tbGoods;
        this.tbGoodsDesc = tbGoodsDesc;
        this.tbItemList = tbItemList;
    }
}
