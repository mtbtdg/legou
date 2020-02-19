package cn.zxJava.service;

import cn.zxJava.domain.TbItemCat;

import java.util.List;

public interface ItemCatService {

    List<TbItemCat> findByParentId(Long parentId);

    List<TbItemCat> findAll();

    void add(TbItemCat tbItemCat);

    void update(TbItemCat tbItemCat);
}
