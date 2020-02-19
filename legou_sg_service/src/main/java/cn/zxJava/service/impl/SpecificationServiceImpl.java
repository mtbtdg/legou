package cn.zxJava.service.impl;


import cn.zxJava.domain.TbSpecification;
import cn.zxJava.domain.TbSpecificationOption;
import cn.zxJava.domain.TbSpecificationOptionExample;
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

    @Override
    public Specification findOne(Long id) {
        //先查询规格数据
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);
        //再查询规格项数据 逆向工程的sql语句的查询
        //创建条件对象
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        example.createCriteria().andSpecIdEqualTo(tbSpecification.getId());
        List<TbSpecificationOption> list = tbSpecificationOptionMapper.selectByExample(example);
        //封装到组合类
        Specification specification = new Specification();
        specification.setTbSpecification(tbSpecification);
        specification.setTbSpecificationOptionList(list);
        return specification;
    }

    @Override
    public void update(Specification specification) {
        //先修改规格数据
        TbSpecification tbSpecification = specification.getTbSpecification();
        tbSpecificationMapper.updateByPrimaryKey(tbSpecification);
        //删除规格项数据
        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        example.createCriteria().andSpecIdEqualTo(tbSpecification.getId());
        tbSpecificationOptionMapper.deleteByExample(example);
        //再新增规格项数据
        for (TbSpecificationOption tbSpecificationOption : specification.getTbSpecificationOptionList()) {
            //设置外键值
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            //保存
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    @Override
    public void delete(Long[] ids) {
        //遍历主键值
        for (Long id : ids) {
            //先删除规格数据
            tbSpecificationMapper.deleteByPrimaryKey(id);
            //再删除规格项数据
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            example.createCriteria().andSpecIdEqualTo(id);
            tbSpecificationOptionMapper.deleteByExample(example);
        }
    }

    @Override
    public List<TbSpecification> findAll() {
        return tbSpecificationMapper.selectByExample(null);
    }
}
