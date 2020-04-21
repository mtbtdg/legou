package cn.zxJava.service.impl;

import cn.zxJava.domain.TbItem;
import cn.zxJava.service.ItemSearchService;
import com.alibaba.dubbo.config.annotation.Service;
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

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    /* *
    * 从索引库中搜索数据
    * */

    @Override
    public Map search(Map paramMap) {
        Map resultMap = new HashMap();

        //===============================主查询=====================================
        //获取主查询条件
        String keyword = (String) paramMap.get("keyword");
        //创建条件查询对象
        Query query = new SimpleQuery();
        //添加查询条件
        query.addCriteria(new Criteria("item_keywords").is(keyword));
        //添加查询条件的过滤(分类)
        String  category = (String) paramMap.get("category");
        if (category != null && !category.isEmpty()){
            //创建过滤条件
            FilterQuery filterQuery = new SimpleFilterQuery();
            filterQuery.addCriteria(new Criteria("item_category").is(category));
            query.addFilterQuery(filterQuery);
        }
        //添加查询条件的过滤(品牌)
        String  brand = (String) paramMap.get("brand");
        if (brand != null && !brand.isEmpty()){
            //创建过滤条件
            FilterQuery filterQuery = new SimpleFilterQuery();
            filterQuery.addCriteria(new Criteria("item_brand").is(brand));
            query.addFilterQuery(filterQuery);
        }
        //设置分页查询
        int pageNum = (int) paramMap.get("page");
        //设置起始位置
        query.setOffset((pageNum - 1) * 60);
        //设置每页展示条数
        query.setRows(60);
        //分页条件查询
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
        //获取分页查询的内容
        List<TbItem> tbItemList = page.getContent();
        //存入resultMap
        resultMap.put("rows",tbItemList);
        //存储总页数
        resultMap.put("totalPage",page.getTotalPages());
        //存储总条数
        resultMap.put("total",page.getTotalElements());


        //===============================分类分组查询=====================================
        List<String> categoryList = new ArrayList<>();
        //产生类似于SQL:select category from tb_item where item_keywords='手机' group by category的效果
        //主查询
        //创建条件查询对象
        Query categoryQuery = new SimpleQuery();
        //添加查询条件
        categoryQuery.addCriteria(new Criteria("item_keywords").is(keyword));

        //创建分组对象
        GroupOptions groupOptions = new GroupOptions();
        //设置分组依据（通过业务域名）
        groupOptions.addGroupByField("item_category");
        //设置分组
        categoryQuery.setGroupOptions(groupOptions);

        //分组查询
        GroupPage<TbItem> categoryPage = solrTemplate.queryForGroupPage(categoryQuery, TbItem.class);

        //获取分类组的名称(固定的API)
        GroupResult<TbItem> item_category = categoryPage.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> groupEntries = item_category.getGroupEntries();
        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntries.getContent()) {
            String value = tbItemGroupEntry.getGroupValue();
            categoryList.add(value);
        }
        resultMap.put("categoryList",categoryList);


        //===============================品牌分组查询=====================================
        List<String> brandList = new ArrayList<>();
        //产生类似于SQL:select category from tb_item where item_keywords='手机' group by category的效果
        //主查询
        //创建条件查询对象
        Query brandQuery = new SimpleQuery();
        //添加查询条件
        brandQuery.addCriteria(new Criteria("item_keywords").is(keyword));

        //创建分组对象
        GroupOptions brandGroupOptions = new GroupOptions();
        //设置分组依据（通过业务域名）
        brandGroupOptions.addGroupByField("item_brand");
        //设置分组
        brandQuery.setGroupOptions(brandGroupOptions);

        //分组查询
        GroupPage<TbItem> brandPage = solrTemplate.queryForGroupPage(brandQuery, TbItem.class);

        //获取分类组的名称(固定的API)
        GroupResult<TbItem> item_brand = brandPage.getGroupResult("item_brand");
        Page<GroupEntry<TbItem>> brandGroupEntries = item_brand.getGroupEntries();
        for (GroupEntry<TbItem> tbItemGroupEntry : brandGroupEntries.getContent()) {
            String value = tbItemGroupEntry.getGroupValue();
            brandList.add(value);
        }
        resultMap.put("brandList",brandList);




        return resultMap;
    }
}
