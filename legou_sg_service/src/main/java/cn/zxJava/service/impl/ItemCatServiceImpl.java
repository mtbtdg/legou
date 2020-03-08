package cn.zxJava.service.impl;

import cn.zxJava.domain.TbItemCat;
import cn.zxJava.domain.TbItemCatExample;
import cn.zxJava.mapper.TbItemCatMapper;
import cn.zxJava.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;


    @Override
    public List<TbItemCat> findByParentId(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return tbItemCatMapper.selectByExample(example);
    }

    @Override
    public List<TbItemCat> findAll() {
        return tbItemCatMapper.selectByExample(null);
    }

    @Override
    public void add(TbItemCat tbItemCat) {
        tbItemCatMapper.insert(tbItemCat);
    }

    @Override
    public void update(TbItemCat tbItemCat) {
        tbItemCatMapper.updateByPrimaryKey(tbItemCat);
    }

    @Override
    public TbItemCat findOne(Long id) {
        return tbItemCatMapper.selectByPrimaryKey(id);
    }
}
