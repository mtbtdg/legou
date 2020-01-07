package cn.zxJava.service.impl;

import cn.zxJava.domain.Brand;
import cn.zxJava.mapper.BrandMapper;
import cn.zxJava.service.BrandService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

    //分页查询
    @Override
    public PageInfo<Brand> findPage(int pageNum, int pageSize) {
        //设置当前页和分页条数
        PageHelper.startPage(pageNum,pageSize);
        //查询所有，自动分页
        List<Brand> list = brandMapper.findAll();
        //封装数据
        PageInfo<Brand> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public void save(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public Brand findOne(Long id) {
        return brandMapper.findOne(id);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.update(brand);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            brandMapper.delete(id);
        }
    }
}
