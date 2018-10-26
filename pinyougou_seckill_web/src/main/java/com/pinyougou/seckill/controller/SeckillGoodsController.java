package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

	@Reference
	private SeckillGoodsService seckillGoodsService;

	/**
	 * 查询已经审核库存大于 0 ，开始但是还未结束的秒杀商品列表
	 * @return
	 */
	@RequestMapping("/findList")
	public List<TbSeckillGoods> findList(){
		return seckillGoodsService.findList();
	}

	@RequestMapping("/findOne")
	public TbSeckillGoods findOne(Long id){
		return seckillGoodsService.findOne(id);
	}


}
