package cn.zxJava.service;

import cn.zxJava.domain.TbSpecification;
import cn.zxJava.groupentity.Specification;
import com.github.pagehelper.PageInfo;

public interface SpecificationService {

    PageInfo<TbSpecification> findPage(int pageNum, int pageSize);

    void save(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);

    void delete(Long[] ids);
}
