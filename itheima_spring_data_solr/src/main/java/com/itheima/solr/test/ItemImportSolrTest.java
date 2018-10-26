package com.itheima.solr.test;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class ItemImportSolrTest {

	@Autowired
	private SolrTemplate solrTemplate;

	@Autowired
	private TbItemMapper itemMapper;

	@Test
	public void importSolr(){
		TbItemExample example = new TbItemExample();
		example.createCriteria().andStatusEqualTo("1");
		List<TbItem> itemList = itemMapper.selectByExample(example);

		//动态域的映射
		for (TbItem item:itemList){
			if (item.getSpec() != null){
				//将spc字段中的json字符串转换为map
				Map map = JSON.parseObject(item.getSpec(), Map.class);
				//给带注解的字段赋值（动态域）
				item.setSpecMap(map);
				System.out.println(item.getTitle() +" ==== "+ item.getSpec());
			}
		}
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}
}
