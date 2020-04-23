package cn.zxJava.service;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.groupentity.Goods;
import com.github.pagehelper.PageInfo;

public interface GoodsService {
    //保存
    void add(Goods goods);

    PageInfo<TbGoods> findPage(int pageNum, int pageCode);

    void updateGoods(String val, Long[] ids);
}
