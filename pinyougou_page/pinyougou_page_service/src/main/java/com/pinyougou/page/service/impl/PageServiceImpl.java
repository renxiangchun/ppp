package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PageServiceImpl implements PageService {

	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;


	@Override
	public void createHtml(Long goodsId) {

		try( FileWriter fileWriter = new FileWriter("D:\\item\\"+goodsId+".html")) {
			Map map = new HashMap();
			//1、只根据freemarkerConfig获取一个对象就可以了
			Configuration configuration = freemarkerConfig.getConfiguration();
			//2、获取模板
			Template template = configuration.getTemplate("item.ftl");
			//3、根据商品Id获取商品的信息
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			//根据商品的ID查询商品详细信息
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			//根据goods对象中的categoryID查询分类的对象name
			String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			//根据商品ID查询SKU列表
			TbItemExample example = new TbItemExample();
			TbItemExample.Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(goodsId);
			criteria.andStatusEqualTo("1");
			example.setOrderByClause("is_default desc");
			List<TbItem> itemList = itemMapper.selectByExample(example);

			//4、用Map对象封装商品的信息给静态页面传递
			map.put("goods", goods);
			map.put("goodsDesc", goodsDesc);
			map.put("itemCat1Name",itemCat1Name);
			map.put("itemCat2Name",itemCat2Name);
			map.put("itemCat3Name",itemCat3Name);
			map.put("itemList",itemList);

			template.process(map,fileWriter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteHtml(Long goodsId) {
		new File("D:\\item\\"+goodsId+".html").delete();
	}
}
