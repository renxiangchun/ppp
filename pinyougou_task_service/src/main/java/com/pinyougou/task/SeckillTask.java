package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillTask {

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 每分钟执行查询秒杀商品数据库表，将审核通过的，库存大于 0，开始时间小于
	 * 等于当前时间，结束时间大于当前时间并且缓存中不存在的秒杀商品存入缓存。
	 * 并且不包含已经在缓存中的秒杀商品
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void refreshSeckillGoods(){

		//查询在redis中的秒杀商品id集合
		Set<Long> set = redisTemplate.boundHashOps("SECKILL_GOODS").keys();
		ArrayList<Long> seckillGoodsIds = new ArrayList<>(set);

		//创建查询对象
		TbSeckillGoodsExample example = new TbSeckillGoodsExample();
		TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
		//已经审核
		criteria.andStatusEqualTo("1");
		//库存大于0
		criteria.andStockCountGreaterThan(0);
		//已经开始；
		criteria.andStartTimeLessThanOrEqualTo(new Date());
		// 末结束
		criteria.andEndTimeGreaterThan(new Date());

		//不包含在缓存中的秒杀商品
		criteria.andIdNotIn(seckillGoodsIds);

		//查询
		List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);

		if(seckillGoodsList != null && seckillGoodsList.size() > 0){
			for (TbSeckillGoods seckillGoods: seckillGoodsList){
				redisTemplate.boundHashOps("SECKILL_GOODS").put(seckillGoods.getId(),seckillGoods);
			}
			System.out.println("加入了最新的 " + seckillGoodsList.size() + " 条秒杀商品到缓存中...");
		}

	}

	/**
	 * 将redis的那些结束时间小于当前时间的秒杀商品从redis删除并更新回mysql。
	 */
	@Scheduled(cron = "* * * * * ?")
	public void removeSeckillGoods(){

		//查询在redis中的秒杀商品集合
		List<TbSeckillGoods> seckill_goodsList = redisTemplate.boundHashOps("SECKILL_GOODS").values();
		if (seckill_goodsList != null && seckill_goodsList.size() > 0 ){
			for (TbSeckillGoods seckillGoods: seckill_goodsList){
				if(seckillGoods.getEndTime().getTime() < System.currentTimeMillis()){

					//更新到mysql
					seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);

					//删除redis中对应的商品
					redisTemplate.boundHashOps("SECKILL_GOODS").delete(seckillGoods.getId());

					System.out.println("移除了id为 " + seckillGoods.getId() + " 的秒杀商品...");
				}
			}
		}

	}
}
