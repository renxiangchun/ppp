package com.pinyougou.page.service;

/**
 *  商品详细页接口
 */
public interface PageService {

	/**
	 * 根据商品ID生成静态页面
	 * @param goodsId
	 */
	void createHtml(Long goodsId);

	/**
	 * 根据商品ID删除静态页面
	 * @param goodsId
	 */
	void deleteHtml(Long goodsId);
}
