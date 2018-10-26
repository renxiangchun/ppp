package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillGoodsService {

	/**
	 * 查询已经审核库存大于 0 ，开始但是还未结束的秒杀商品列表
	 * @return 秒杀商品列表
	 */
	List<TbSeckillGoods> findList();

	/**
	 * 根据秒杀商品id在redis中查询对应的商品
	 * @param id
	 * @return
	 */
	TbSeckillGoods findOne(Long id);
}
