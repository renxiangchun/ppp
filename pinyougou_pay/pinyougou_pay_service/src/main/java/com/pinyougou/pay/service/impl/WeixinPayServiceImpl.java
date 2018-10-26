package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.HttpClient;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
@Service(timeout = 1000000)
public class WeixinPayServiceImpl implements WeixinPayService {

	//App的唯一标识
	@Value("${appid}")
	private String appid;
	//商家号
	@Value("${partner}")
	private String partner;
	//商家密钥
	@Value("${partnerkey}")
	private String partnerkey;
	//通知地址
	@Value("${notifyurl}")
	private String notifyurl;

	//根据商户订单号和交易总金额到微信支付系统生成支付二维码等信息
	@Override
	public Map<String, String> createNative(String out_trade_no, String total_fee) {
		//返回的Map
		Map<String,String> resultMap = new HashMap<>();

		//发送到微信支付系统的参数集合
		Map<String,String> param = new HashMap<>();
		//公众账号ID
		param.put("appid",appid);
		//商户号
		param.put("mch_id",partner);
		//随机字符串
		param.put("nonce_str", WXPayUtil.generateNonceStr());
		//签名;转换的时候自动生成
		//param.put("sign",);
		//商品描述
		param.put("body","品优购");
		//商户订单号
		param.put("out_trade_no",out_trade_no);
		//标价金额
		param.put("total_fee",total_fee);
		//终端IP
		param.put("spbill_create_ip","127.0.0.1");
		//通知地址
		param.put("notify_url",notifyurl);
		//交易类型
		param.put("trade_type","NATIVE");

		try {
			//转换成xml
			String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);
			System.out.println("发送到微信 统一下单 的内容："+signedXml);
			//发送统一下单，到微信支付系统
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
			client.setHttps(true);
			client.setXmlParam(signedXml);
			client.post();
			//2.获取微信返回的结果
			String result = client.getContent();
			System.out.println("调用微信 统一下单 的返回内容："+result);
			Map<String, String> map = WXPayUtil.xmlToMap(result);
			//支付地址
			resultMap.put("code_url",map.get("code_url"));
			//总支付
			resultMap.put("total_fee",total_fee);
			//订单号
			resultMap.put("out_trade_no",out_trade_no);
			//下单成功、失败
			resultMap.put("result_code",map.get("result_code"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 根据订单号查询支付状态
	 * @param out_trade_no
	 * @return
	 */
	@Override
	public Map<String, String> queryPayStatus(String out_trade_no) {

		//发送到微信支付系统的参数集合
		Map<String,String> param = new HashMap<>();
		//公众账号ID
		param.put("appid",appid);
		//商户号
		param.put("mch_id",partner);
		//随机字符串
		param.put("nonce_str", WXPayUtil.generateNonceStr());
		//签名;转换的时候自动生成
		//param.put("sign",);
		//商户订单号
		param.put("out_trade_no",out_trade_no);

		try {
			//转换成xml
			String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);
			System.out.println("发送到微信 查询状态 的内容："+signedXml);
			//发送统一下单，到微信支付系统
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			client.setHttps(true);
			client.setXmlParam(signedXml);
			client.post();
			//2.获取微信返回的结果
			String result = client.getContent();
			System.out.println("调用微信 查询状态 的返回内容："+result);
			Map<String, String> map = WXPayUtil.xmlToMap(result);
			return  map;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 到微信支付系统关闭订单
	 * @param outTradeNo
	 * @return
	 */
	@Override
	public Map<String, String> closeOrder(String outTradeNo) {
		//发送到微信支付系统的参数集合
		Map<String,String> param = new HashMap<>();
		//公众账号ID
		param.put("appid",appid);
		//商户号
		param.put("mch_id",partner);
		//随机字符串
		param.put("nonce_str", WXPayUtil.generateNonceStr());
		//签名;转换的时候自动生成
		//param.put("sign",);
		//商户订单号
		param.put("out_trade_no",outTradeNo);

		try {
			//转换成xml
			String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);
			System.out.println("发送到微信 关闭订单 的内容："+signedXml);
			//发送统一下单，到微信支付系统
			HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
			client.setHttps(true);
			client.setXmlParam(signedXml);
			client.post();
			//2.获取微信返回的结果
			String result = client.getContent();
			System.out.println("调用微信 关闭订单 的返回内容："+result);
			Map<String, String> map = WXPayUtil.xmlToMap(result);
			return  map;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
