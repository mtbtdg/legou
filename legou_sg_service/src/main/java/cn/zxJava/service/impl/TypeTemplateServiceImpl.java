package cn.zxJava.service.impl;

import cn.zxJava.domain.TbTypeTemplate;
import cn.zxJava.mapper.TbTypeTemplateMapper;
import cn.zxJava.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper tbTypeTemplateMapper;

    @Override
    public PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<TbTypeTemplate> list = tbTypeTemplateMapper.selectByExample(null);
        return new PageInfo<>(list);
    }
}
