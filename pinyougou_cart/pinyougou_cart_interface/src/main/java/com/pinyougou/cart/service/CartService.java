package com.pinyougou.cart.service;

import com.pinyougou.pojo.vo.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

	/**
	 * 根据商品信息把商品添加到购物车集合中
	 * @param cartList
	 * @param itemId
	 * @param num
	 * @return 购物车集合
	 */
	List<Cart> addGoodsCartList(List<Cart> cartList,Long itemId,Integer num);

	/**
	 * 根据key获取购物车集合数据
	 * @param key
	 * @return
	 */
	List<Cart> queryCartListByRedis(String key);

	/**
	 * 根据key把购物车集合数据放在redis中
	 * @param key
	 * @param cartList
	 */
	void addCartListToRedis(String key, List<Cart> cartList);

	/**
	 * 把未登录的购物车集合和登录的购物车集合合并
	 * @param cartList
	 * @param cartList_session
	 * @return
	 */
	List<Cart> mergeCartList(List<Cart> cartList, List<Cart> cartList_session);

	/**
	 * 登录后将redis中的购物车集合删除
	 * @param key
	 */
	void delCartListRedis(String key);
}
