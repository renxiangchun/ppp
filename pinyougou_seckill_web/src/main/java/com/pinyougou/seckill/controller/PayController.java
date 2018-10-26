package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference(timeout = 1000000)
	private WeixinPayService weixinPayService;

	@Reference
	private SeckillOrderService seckillOrderService;

	/**
	 * 生成支付二维码
	 * @return
	 */
	@RequestMapping(value = "/createNative",method = RequestMethod.GET)
	public Map<String,String> createNative(String outTradeNo){
		TbSeckillOrder seckillOrder = seckillOrderService.findSeckillOrderInRedisByoutTradeNo(outTradeNo);
		//1.获取订单号
		//IdWorker idWorker = new IdWorker();
		//String out_trade_no = idWorker.nextId() + "";
		String out_trade_no = outTradeNo;

		//2.总金额
		String total_fee = (long)(seckillOrder.getMoney().doubleValue() * 100) + "";
		//3.调用支付系统方法获取支付二维码地址
		return weixinPayService.createNative(out_trade_no,total_fee);
	}

	/**
	 * 根据订单号查询支付状态
	 * @param outTradeNo
	 * @return
	 */
	@RequestMapping(value = "/queryPayStatus",method = RequestMethod.GET)
	public Result queryPayStatus(String outTradeNo){
		Result result = new Result(false,"查询支付状态失败");

		//支付二维码超时时间：15秒
		int count = 0;
		while (true){
			//到支付系统查询支付状态
			Map<String,String> resultMap = weixinPayService.queryPayStatus(outTradeNo);

			if(resultMap == null){
				break;
			}

			if("SUCCESS".equals(resultMap.get("trade_state"))){
				//支付成功
				result = new Result(true,"支付成功");

				//更新订单状态；参数2:微信订单号
				seckillOrderService.saveSeckillOrderInRedisToDb(outTradeNo,resultMap.get("transaction_id"));
				break;
			}

			count++;
			if(count > 5){
				result = new Result(false,"支付超时");

				//到微信支付系统关闭订单
				resultMap = weixinPayService.closeOrder(outTradeNo);
				//如果在关闭订单的过程中，被人支付了则也需要将订单保存到数据库中
				if("ORDERPAID".equals(resultMap.get("err_code"))){
					//更新订单状态
					seckillOrderService.saveSeckillOrderInRedisToDb(outTradeNo,resultMap.get("transaction_id"));

					result = new Result(true,"支付成功");
					break;
				}

				try {
					//将秒杀商品的库存加1,并删除redis中的订单
					seckillOrderService.deleteSeckillOrderInRedis(outTradeNo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
