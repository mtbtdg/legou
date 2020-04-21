package cn.zxJava.service;

import cn.zxJava.domain.TbGoods;
import cn.zxJava.groupentity.Goods;

import java.util.List;

public interface ItempageService {

    Goods findByGoodsId(Long goodsId);

    List<Goods> findGoodsAll();
}
