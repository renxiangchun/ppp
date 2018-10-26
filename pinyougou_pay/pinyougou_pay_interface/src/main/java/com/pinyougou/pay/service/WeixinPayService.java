package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {

	/**
	 * 根据商户订单号和交易总金额到微信支付系统生成支付二维码等信息
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 */
	Map<String,String> createNative(String out_trade_no, String total_fee);

	/**
	 * 根据订单号查询支付状态
	 * @param out_trade_no
	 * @return
	 */
	Map<String,String> queryPayStatus(String out_trade_no);

	/**
	 * 到微信支付系统关闭订单
	 * @param outTradeNo
	 * @return
	 */
	Map<String,String> closeOrder(String outTradeNo);
}
