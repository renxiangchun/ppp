package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinoyougou.search.service.SearchService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(timeout = 1000000)
public class SearchServiceImpl implements SearchService{

	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private TbItemMapper itemMapper;


	@Override
	public Map	search(Map map) {

		//1、创建搜索条件对象
		//SimpleQuery query = new SimpleQuery("*:*");
		//设置高亮的条件对象
		SimpleHighlightQuery query = new SimpleHighlightQuery();

		//2、设置参数
		//2.1主条件查询
		String keywords = (String) map.get("keywords");
		//初始化条件参数对象
		Criteria criteria = null;
		if(keywords != null && !"".equals(keywords)){
			//在创建条对象的时候，直接指定什么域条件
			//如果is:那么是根据分词来搜索的，找的是分词后的数据
			//包含：搜索的时候，是不分词搜索，只要title或者域中包含就可以了；
			criteria = new Criteria("item_title").is(keywords);
		} else {
			//查询所有
			criteria = new Criteria().expression("*:*");
		}
		//添加查询条件
		query.addCriteria(criteria);

		//2.2设置高亮
		HighlightOptions options = new HighlightOptions();
		options.addField("item_title");//设置高亮的域
		options.setSimplePrefix("<em style='color:red'>");//高亮前缀
		options.setSimplePostfix("</em>");//高亮后缀
		query.setHighlightOptions(options);//设置高亮选项

		//2.3 设置分类过滤条件的
		String category = (String) map.get("category");
		if( category != null && !"".equals(category)){
			//创建一个过滤条件对象
			SimpleFilterQuery filterQuery = new SimpleFilterQuery();
			criteria = new Criteria("item_category");
			criteria.is(category);
			filterQuery.addCriteria(criteria);
			query.addFilterQuery(filterQuery);
		}
		//2.4 设置品牌过滤条件
		String brand = (String) map.get("brand");
		if( brand != null && !"".equals(brand)){
			//创建一个过滤条件对象
			SimpleFilterQuery filterQuery = new SimpleFilterQuery();
			criteria = new Criteria("item_brand");
			criteria.is(brand);
			filterQuery.addCriteria(criteria);
			query.addFilterQuery(filterQuery);
		}
		//2.5 设置价格过滤条件
		String prices = (String) map.get("price");
		if(prices != null && !"".equals(prices)){
			String[] price = prices.split("-");
			if(!"0".equals(price[0])){
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				criteria = new Criteria("item_price");
				criteria.greaterThanEqual(price[0]);
				filterQuery.addCriteria(criteria);
				query.addFilterQuery(filterQuery);
			}
			if(!"*".equals(price[1])){
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				criteria = new Criteria("item_price");
				criteria.lessThanEqual(price[1]);
				filterQuery.addCriteria(criteria);
				query.addFilterQuery(filterQuery);
			}
		}
		//2.6 设置规格过滤条件
		//spec:{"网络":"移动3G"}//根据动态域来查询
		Map<String,String> m = (Map<String,String>)map.get("spec");
		if ( m != null){
			for (String key:m.keySet()){
				SimpleFilterQuery filterQuery = new SimpleFilterQuery();
				//创建条件
				 criteria = new Criteria("item_spec_"+key);
				criteria.is(m.get(key));
				//加入条件对象
				filterQuery.addCriteria(criteria);
				//加入过滤条件
				query.addFilterQuery(filterQuery);
			}
		}
		//2.7 设置排序
		String sort = (String) map.get("sort");
		String sortField = (String) map.get("sortField");
		if( sortField != null && !"".equals(sortField)){
			if("ASC".equalsIgnoreCase(sort)){
				Sort sort1 = new Sort(Sort.Direction.ASC,"item_"+sortField);
				query.addSort(sort1);
			}
			if("DESC".equalsIgnoreCase(sort)){
				Sort sort1 = new Sort(Sort.Direction.DESC,"item_"+sortField);
				query.addSort(sort1);
			}
		}
		//注意：咱们在这里只写了价格，其实还有新品，销量等等，新品举例：1、加入业务域；2、把item中的time映射到时间业务域中；3、重新导入索引库



		//2.8最好是设置分页
		Integer pages = (Integer) map.get("page");
		Integer pageSize = (Integer) map.get("pageSize");
		if(pages == null){
			pages = 1;
		}
		if(pageSize == null){
			pageSize = 20;
		}
		query.setOffset((pages - 1) * pageSize);
		query.setRows(pageSize);

		//3、执行查询
		//ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//获取高亮域的数据
		for (HighlightEntry<TbItem> h:page.getHighlighted()){//循环高亮入口集合
			TbItem item = h.getEntity();//获取原实体类
			if(h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0){
				item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果
			}
		}
		//4.获取结果集
		List<TbItem> itemList = page.getContent();
		//5、封装返回值
		Map resultMap = new HashMap();
		resultMap.put("rows",itemList);
		resultMap.put("total",page.getTotalElements());
		resultMap.put("totalPages",page.getTotalPages());

		return resultMap;
	}

	@Override
	public void importItem(Long goodsId) {
		//1、根据goodsId获取itemList
		TbItemExample example = new TbItemExample();
		TbItemExample.Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goodsId);
		criteria.andStatusEqualTo("1");
		List<TbItem> itemList = itemMapper.selectByExample(example);
		//2、itemList遍历了把item.spec设置到specMap属性中：因为这个属性已经和业务域映射了
		for (TbItem item:itemList){
			Map map = JSON.parseObject(item.getSpec(), Map.class);
			item.setSpecMap(map);
		}
		//3、使用solrTemplate把SKU集合到索引库
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}

	@Override
	public void deleteItem(Long goodsId) {
		SimpleQuery query = new SimpleQuery("item_goodsid:" + goodsId);
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
