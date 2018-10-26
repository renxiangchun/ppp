package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/seckillOrder")
@RestController
public class SeckillOrderController {

	// 设置远程服务调用超时时间 ( 毫秒 ) ；默认 1000
	@Reference
	private SeckillOrderService seckillOrderService;

	/**
	 * 提交秒杀订单
	 * @param seckillId 秒杀商品 id
	 * @return 成功：秒杀商品id
	 */
	@RequestMapping("/submitOrder")
	public Result submitOrder(Long seckillId){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if(!"anonymousUser".equals(username)){
				Long orderId = seckillOrderService.submitOrder(username,seckillId);

				if(orderId != null){
					return new Result(true,orderId.toString());
				}
			} else {
				return new Result(false, "请先登录！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "提交订单失败！");
	}


}
