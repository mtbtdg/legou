package cn.zxJava.service.impl;

import cn.zxJava.domain.TbSpecificationOption;
import cn.zxJava.domain.TbSpecificationOptionExample;
import cn.zxJava.domain.TbTypeTemplate;
import cn.zxJava.mapper.TbSpecificationOptionMapper;
import cn.zxJava.mapper.TbTypeTemplateMapper;
import cn.zxJava.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Autowired
    private TbTypeTemplateMapper tbTypeTemplateMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    @Override
    public PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<TbTypeTemplate> list = tbTypeTemplateMapper.selectByExample(null);
        return new PageInfo<>(list);
    }

    @Override
    public void add(TbTypeTemplate tbTypeTemplate) {
        tbTypeTemplateMapper.insert(tbTypeTemplate);
    }

    @Override
    public void update(TbTypeTemplate tbTypeTemplate) {
        tbTypeTemplateMapper.updateByPrimaryKey(tbTypeTemplate);
    }

    @Override
    public TbTypeTemplate findOne(Long id) {
        return tbTypeTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbTypeTemplateMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> findSpecList(Long id) {
        //查询模板数据
        TbTypeTemplate tbTypeTemplate = tbTypeTemplateMapper.selectByPrimaryKey(id);
        //取得specIds的值
        String specIds = tbTypeTemplate.getSpecIds();
        //把specIds的json格式的数据转成集合
        List<Map> list = JSON.parseArray(specIds, Map.class);

        //遍历集合，向里面添加数据
        for (Map map : list) {
            //获取单个id值
            String specId = map.get("id") + "";
            //转成Long类型
            long l = Long.parseLong(specId);
            //查询对应的options并添加进map
            TbSpecificationOptionExample optionExample = new TbSpecificationOptionExample();
            optionExample.createCriteria().andSpecIdEqualTo(l);
            List<TbSpecificationOption> options = tbSpecificationOptionMapper.selectByExample(optionExample);
            map.put("options",options);
        }

        return list;
    }
}
