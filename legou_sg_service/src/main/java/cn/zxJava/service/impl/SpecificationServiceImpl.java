package cn.zxJava.service.impl;


import cn.zxJava.domain.TbSpecification;
import cn.zxJava.domain.TbSpecificationOption;
import cn.zxJava.groupentity.Specification;
import cn.zxJava.mapper.TbSpecificationMapper;
import cn.zxJava.mapper.TbSpecificationOptionMapper;
import cn.zxJava.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;

    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    //分页查询
    @Override
    public PageInfo<TbSpecification> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //传入null代表没有条件，即查所有
        List<TbSpecification> list = tbSpecificationMapper.selectByExample(null);
        return new PageInfo<>(list);
    }

    /*
    * 先保存规格数据，再保存规格项数据（存储规格项时要往外键值中存入规格的主键）
    * */
    @Override
    public void save(Specification specification) {
        //存储规格数据
        TbSpecification tbSpecification = specification.getTbSpecification();
        tbSpecificationMapper.insert(tbSpecification);
        //存储规格项
        List<TbSpecificationOption> list = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : list) {
            //设置外键值
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }
}
