package cn.zxJava.service;

import cn.zxJava.domain.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();

    PageInfo<Brand> findPage(int pageNum, int pageSize);

    void save(Brand brand);

    Brand findOne(Long id);

    void update(Brand brand);

    void delete(Long[] ids);
}
