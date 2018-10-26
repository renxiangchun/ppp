package com.pinyougou.page.service.listener;

import com.pinyougou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class PageMessageListener implements MessageListener {

	@Autowired
	private PageService pageService;
	@Override
	public void onMessage(Message message) {
		//操作，获取消息
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			//根据商品的ID查询SKU（item）列表
			//导入到索引库
			for (Long goodsId:ids){
				pageService.createHtml(goodsId);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
