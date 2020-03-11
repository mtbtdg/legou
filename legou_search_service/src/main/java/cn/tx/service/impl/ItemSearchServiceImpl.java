package cn.tx.service.impl;

import cn.tx.domain.TbItem;
import cn.tx.domain.TbItemCat;
import cn.tx.domain.TbItemCatExample;
import cn.tx.domain.TbTypeTemplate;
import cn.tx.mapper.TbItemCatMapper;
import cn.tx.mapper.TbTypeTemplateMapper;
import cn.tx.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拓薪教育 -- 腾讯课程认证机构
 * 樱木老师
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    // 注入分类
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    // 注入模板对象
    @Autowired
    private TbTypeTemplateMapper tbTypeTemplateMapper;

    /**
     * 搜索
     * @param paramMap
     * @return
     */
    @Override
    public Map search(Map paramMap) {
        // 封装结果
        Map resultMap = new HashMap();
        // 获取查询条件
        String keyword = (String) paramMap.get("keyword");
        // 创建查询对象
        Query query = new SimpleQuery();
        // 创建条件对象
        Criteria criteria = new Criteria("item_keywords").is(keyword);
        // 添加查询条件
        query.addCriteria(criteria);

        // 添加过滤条件查询
        // 先获取到品牌数据
        String brand = (String) paramMap.get("brand");
        // 判断是否有值
        if(brand != null && !"".equals(brand)){
            // 设置过滤条件
            FilterQuery filterQuery = new SimpleFilterQuery();
            // 设置查询条件
            filterQuery.addCriteria(new Criteria("item_brand").is(brand));
            // 设置查询
            query.addFilterQuery(filterQuery);
        }

        // 先获取到分类数据
        String category = (String) paramMap.get("category");
        // 判断是否有值
        if(category != null && !"".equals(category)){
            // 设置过滤条件
            FilterQuery filterQuery = new SimpleFilterQuery();
            // 设置查询条件
            filterQuery.addCriteria(new Criteria("item_category").is(category));
            // 设置查询
            query.addFilterQuery(filterQuery);
        }

        // 设置分页查询
        // 获取当前页
        int page = (int) paramMap.get("page");
        // 设置起始位置
        query.setOffset((page-1)*60);
        // 设置每页查询的60条数
        query.setRows(60);

        // 查询
        ScoredPage<TbItem> tbItems = solrTemplate.queryForPage(query, TbItem.class);

        // 设置总记录数
        resultMap.put("total",tbItems.getTotalElements());
        // 设置总页数
        resultMap.put("totalPages",tbItems.getTotalPages());
        // 封装数据，返回
        resultMap.put("rows",tbItems.getContent());


        // =============================查询商品分类数据============================================
        // 需要使用solr的分组查询
        Query groupQuery = new SimpleQuery();
        // 设置查询条件
        groupQuery.addCriteria(new Criteria("item_keywords").is(keyword));

        // 设置分组的域
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        groupQuery.setGroupOptions(groupOptions);

        // 查询
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(groupQuery, TbItem.class);
        // 获取查询的字段结果
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
        List<GroupEntry<TbItem>> groupEntryList = groupEntries.getContent();

        // 创建list集合，存储结果
        List<String> categoryList = new ArrayList<>();

        // 遍历数据
        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntryList) {
            // 获取到想要的数据
            String value = tbItemGroupEntry.getGroupValue();
            // 添加到list集合
            categoryList.add(value);
        }
        // 存储到map集合
        resultMap.put("categoryList",categoryList);

        // ======================根据第一个商品分类数据，查询对应的品牌数据=====================================
        // 先判断分类集合是否不为空
        if(categoryList != null && categoryList.size() > 0){
            // 说明不为空，获取第一个分类名称
            String categoryName = categoryList.get(0);

            TbItemCatExample itemCatExample = new TbItemCatExample();
            TbItemCatExample.Criteria criteria1 = itemCatExample.createCriteria();
            criteria1.andNameEqualTo(categoryName);
            // 通过分类的名称查询到该分类对象
            TbItemCat itemCat = tbItemCatMapper.selectByExample(itemCatExample).get(0);
            // 获取到模板id
            Long typeId = itemCat.getTypeId();

            // 根据模板id查询出模板对象
            TbTypeTemplate tbTypeTemplate = tbTypeTemplateMapper.selectByPrimaryKey(typeId);
            // 根据模板对象获取到品牌数据
            // {"id":6,"text":"360"},{"id":10,"text":"VIVO"},{"id":11,"text":"诺基亚"},{"id":12,"text":"锤子"}]
            String brandIds = tbTypeTemplate.getBrandIds();
            // 解析
            List<Map> brandList = JSON.parseArray(brandIds, Map.class);
            // 存入到resultMap
            resultMap.put("brandList",brandList);
        }

        // 返回
        return resultMap;
    }

}
