package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

	@Reference
	private WeixinPayService weixinPayService;

	@Reference
	private OrderService orderService;

	/**
	 * 生成支付二维码
	 * @return
	 */
	@RequestMapping(value = "/createNative",method = RequestMethod.GET)
	public Map<String,String> createNative(){
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		TbPayLog payLog = orderService.findPayLogInRedis(userId);
		//1.获取订单号
		//IdWorker idWorker = new IdWorker();
		//String out_trade_no = idWorker.nextId() + "";
		String out_trade_no = payLog.getOutTradeNo();

		//2.总金额
		String total_fee = payLog.getTotalFee().toString();
		//3.调用支付系统方法获取支付二维码地址
		return weixinPayService.createNative(out_trade_no,total_fee);
	}

	/**
	 * 根据订单号查询支付状态
	 * @param out_trade_no
	 * @return
	 */
	@RequestMapping(value = "/queryPayStatus",method = RequestMethod.GET)
	public Result queryPayStatus(String out_trade_no){
		Result result = new Result(false,"查询支付状态失败");

		//支付二维码超时时间：15秒
		int count = 0;
		while (true){
			//到支付系统查询支付状态
			Map<String,String> resultMap = weixinPayService.queryPayStatus(out_trade_no);

			if(resultMap == null){
				break;
			}

			if("SUCCESS".equals(resultMap.get("trade_state"))){
				//支付成功
				result = new Result(true,"支付成功");

				//更新订单状态；参数2:微信订单号
				orderService.updateOrderStatus(out_trade_no,resultMap.get("transaction_id"));
				break;
			}

			count++;
			if(count > 6000){
				result = new Result(false,"支付二维码超时");
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
