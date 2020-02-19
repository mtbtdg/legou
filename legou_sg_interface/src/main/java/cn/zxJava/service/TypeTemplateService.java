package cn.zxJava.service;


import cn.zxJava.domain.TbTypeTemplate;
import com.github.pagehelper.PageInfo;

public interface TypeTemplateService {

    PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize);

    void add(TbTypeTemplate tbTypeTemplate);

    void update(TbTypeTemplate tbTypeTemplate);

    TbTypeTemplate findOne(Long id);

    void delete(Long[] ids);
}
