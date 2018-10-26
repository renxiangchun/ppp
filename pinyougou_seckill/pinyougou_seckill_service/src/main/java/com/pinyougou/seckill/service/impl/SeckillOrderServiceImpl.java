package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.common.IdWorker;
import com.pinyougou.common.RedisLock;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

	//秒杀订单在redis中的key的名称
	private static final String SECKILL_ORDERS = "SECKILL_ORDERS";

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private TbSeckillOrderMapper seckillOrderMapper;

	/**
	 * 生成秒杀订单 id
	 * @param username 用户 id
	 * @param seckillId  秒杀商品 id
	 * @return
	 */
	@Override
	public Long submitOrder(String username, Long seckillId) throws InterruptedException {
		//获取分布式锁
		RedisLock redisLock = new RedisLock(redisTemplate);
		if(redisLock.lock(seckillId.toString())){
			//1、获取redis中的秒杀商品
			TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).get(seckillId);
			//2、判断商品是否存在并库存大于0
			if(seckillGoods == null){
				throw new RuntimeException("商品不存在");
			}
			if(seckillGoods.getStockCount() < 1){
				throw new RuntimeException("商品已秒杀完");
			}
			//3、将秒杀商品的库存减1；如果库存减之后为0的话，则需要将该秒杀商品同步更新到mysql中，并删除redis该秒杀商品；
			// 如果库存减了之后是大于0的则直接将最新的秒杀商品更新到redis中
			seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
			if(seckillGoods.getStockCount() == 0){
				seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);

				//删除redis中的秒杀商品
				redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).delete(seckillId);
			} else {
				redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).put(seckillId,seckillGoods);
			}
			//释放分布式锁
			redisLock.unlock(seckillId.toString());
			//4、生成秒杀订单并保存到redis
			TbSeckillOrder seckillOrder = new TbSeckillOrder();
			//未支付
			seckillOrder.setStatus("0");
			seckillOrder.setUserId(username);
			seckillOrder.setSeckillId(seckillId);
			seckillOrder.setSellerId(seckillGoods.getSellerId());
			seckillOrder.setCreateTime(new Date());
			//秒杀价
			seckillOrder.setMoney(seckillGoods.getCostPrice());
			IdWorker idWorker = new IdWorker();
			seckillOrder.setId(idWorker.nextId());

			//将订单存入redis
			redisTemplate.boundHashOps(SECKILL_ORDERS).put(seckillOrder.getId().toString(),seckillOrder);

			//5、返回秒杀订单id
			return seckillOrder.getId();
		}
		return null;
	}

	/**
	 * 根据秒杀商品订单号返回秒杀订单
	 * @param outTradeNo
	 * @return
	 */
	@Override
	public TbSeckillOrder findSeckillOrderInRedisByoutTradeNo(String outTradeNo) {
		return (TbSeckillOrder) redisTemplate.boundHashOps(SECKILL_ORDERS).get(outTradeNo);
	}

	/**
	 * 将reids中的订单保存到mysql数据库中，并删除redis中的订单
	 * @param out_trade_no
	 * @param transaction_id
	 */
	@Override
	public void saveSeckillOrderInRedisToDb(String out_trade_no, String transaction_id) {
		//1.获取订单
		TbSeckillOrder seckillOrder = this.findSeckillOrderInRedisByoutTradeNo(out_trade_no);
		//已支付
		seckillOrder.setStatus("1");
		seckillOrder.setPayTime(new Date());
		seckillOrder.setTransactionId(transaction_id);

		//2.保存到数据库中
		seckillOrderMapper.insertSelective(seckillOrder);
		//3.删除redis中的订单
		redisTemplate.boundHashOps(SECKILL_ORDERS).delete(out_trade_no);
	}

	/**
	 * 将秒杀商品的库存加1,并删除redis中的订单
	 * @param outTradeNo
	 */
	@Override
	public void deleteSeckillOrderInRedis(String outTradeNo) throws InterruptedException {
		//1、查询订单获取秒杀商品
		TbSeckillOrder seckillOrder = this.findSeckillOrderInRedisByoutTradeNo(outTradeNo);
		//添加分布式锁
		RedisLock redisLock = new RedisLock(redisTemplate);
		//2、将秒杀商品库存加1
		if(redisLock.lock(seckillOrder.getSeckillId().toString())){
			TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).get(seckillOrder.getSeckillId());
			if(seckillGoods == null){
				//从mysql中查询
				seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getSeckillId());
			}
			//2、将秒杀商品库存加1
			seckillGoods.setStockCount(seckillGoods.getStockCount() + 1);

			//3、将秒杀商品存入redis
			redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).put(seckillGoods.getId(),seckillGoods);

			//释放分布式锁
			redisLock.unlock(seckillOrder.getSeckillId().toString());

			//4、删除redis中的秒杀订单
			redisTemplate.boundHashOps(SECKILL_ORDERS).delete(outTradeNo);
		}
	}
}
