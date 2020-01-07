package cn.zxJava.mapper;

import cn.zxJava.domain.Brand;

import java.util.List;

public interface BrandMapper {

    List<Brand> findAll();

    void insert(Brand brand);

    Brand findOne(Long id);

    void update(Brand brand);

    void delete(Long id);
}
