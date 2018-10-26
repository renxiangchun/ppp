package com.itheima.solr.test;
import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class SpringDataSolrTest {

	@Autowired
	private SolrTemplate solrTemplate;

	//添加/修改
	@Test
	public void add(){
		//SpringDataSolr简化了了Solrj操作的solr服务器，SpringDataSolr是操作对象来操作solr服务器的
		//SpringDataSolr操作对象：是因为可以把对象的属性名和业务域之间映射起来；
		//那么SpringDataSolr就可以只对对象进行操作了！

		TbItem item = new TbItem();
		item.setId(100L);
		item.setTitle("华为Mate9");
		item.setPrice(new BigDecimal(1000));
		item.setGoodsId(1000L);
		item.setCategory("手机");
		item.setBrand("华为");
		item.setSeller("华为");

		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}

	//按主键查询
	@Test
	public void testFindOne(){
		TbItem item = solrTemplate.getById(1000, TbItem.class);
		System.out.println(item.getTitle());
	}
	// 按主键删除
	@Test
	public void dele(){
		//删除所有；在企业中不用；肾用！
		solrTemplate.delete(new SimpleQuery("item_goodsid:149187842867980"));
		//solrTemplate.deleteById("1000");
		solrTemplate.commit();
	}
	//分页查询
	//1.1添加100条数据
	@Test
	public void testAddList(){
		List<TbItem> itemList = new ArrayList<>();
		for (int i =0;i < 100;i++){
			TbItem item = new TbItem();
			item.setId(1000l+i);
			item.setTitle("小米"+i);
			item.setPrice(new BigDecimal(1000+i));
			item.setSeller("xioami");
			item.setCategory("手机");
			item.setBrand("小米");
			item.setGoodsId(1000l+i);

			itemList.add(item);
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}

	//2.2编写分页查询测试代码
	@Test
	public void testPageQuery(){
		Query query = new SimpleQuery("*:*");
		query.setOffset(20);//开始索引（默认 0）
		query.setRows(30);//每页记录数(默认 10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("总记录数:"+page.getTotalElements()  + "; 总页数=" + page.getTotalPages());
		List<TbItem> itemList = page.getContent();
		for (TbItem item:itemList){
			System.out.println(item.getTitle() + "---" + item.getPrice());
		}
	}

	//条件查询
	@Test
	public void testPageQueryMutil(){
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_title").contains("2");
		criteria = criteria.and("item_title").contains("5");
		query.addCriteria(criteria);
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query,TbItem.class);
		System.out.println("总记录数:"+page.getTotalElements());
		List<TbItem> itemList = page.getContent();
		for (TbItem item:itemList){
			System.out.println(item.getTitle() + "---" + item.getPrice());
		}

	}
}
