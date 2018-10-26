package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillOrder;

public interface SeckillOrderService {

	/**
	 * 生成秒杀订单 id
	 * @param username 用户 id
	 * @param seckillId  秒杀商品 id
	 * @return 秒杀订单 id
	 */
	Long submitOrder(String username, Long seckillId) throws InterruptedException;

	/**
	 * 根据秒杀商品订单号返回秒杀订单
	 * @param outTradeNo
	 * @return 秒杀订单
	 */
	TbSeckillOrder findSeckillOrderInRedisByoutTradeNo(String outTradeNo);


	/**
	 * 将reids中的订单保存到mysql数据库中
	 * @param out_trade_no
	 * @param transaction_id
	 */
	void saveSeckillOrderInRedisToDb(String out_trade_no, String transaction_id);

	/**
	 * 将秒杀商品的库存加1,并删除redis中的订单
	 * @param outTradeNo
	 */
	void deleteSeckillOrderInRedis(String outTradeNo) throws InterruptedException;
}
