package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

	@Autowired
	private TbSeckillGoodsMapper seckillGoodsMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	//秒杀商品列表在redis中对应的key的名称
	public static final String SECKILL_GOODS = "SECKILL_GOODS";

	/**
	 * 查询已经审核库存大于 0 ，开始但是还未结束的秒杀商品列表
	 * @return
	 */
	@Override
	public List<TbSeckillGoods> findList() {
		List<TbSeckillGoods> seckillGoodsList = null;

		//从Redis中查找
		seckillGoodsList= redisTemplate.boundHashOps(SECKILL_GOODS).values();

		if(seckillGoodsList == null || seckillGoodsList.size() == 0){
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
			//根据开始时间升序
			example.setOrderByClause("start_time");

			seckillGoodsList = seckillGoodsMapper.selectByExample(example);
			//将秒杀商品一个个存入redis中
			if(seckillGoodsList != null && seckillGoodsList.size() > 0){
				for(TbSeckillGoods seckillGoods: seckillGoodsList){
					redisTemplate.boundHashOps(SECKILL_GOODS).put(seckillGoods.getId(),seckillGoods);
				}
			}
		} else {
			System.out.println("从缓存中读取秒杀商品数据123..");
		}
		return seckillGoodsList;
	}

	/**
	 * 根据秒杀商品id在redis中查询对应的商品
	 * @param id
	 * @return
	 */
	@Override
	public TbSeckillGoods findOne(Long id) {
		return (TbSeckillGoods) redisTemplate.boundHashOps(SECKILL_GOODS).get(id);
	}
}
