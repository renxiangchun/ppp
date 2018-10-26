package com.pinoyougou.search.service;

import java.util.Map;

public interface SearchService {

	/**
	 * 根据Map参数查询搜索库数据
	 * @param map
	 * @return
	 */
	Map search(Map map);

	/**
	 *根据商品ID查询Item的SKU列表并导入索引库
	 * @param goodsId
	 */
	void importItem(Long goodsId);

	/**
	 * 根据商品ID删除索引库数据
	 * @param goodsId
	 */
	void deleteItem(Long goodsId);
}
