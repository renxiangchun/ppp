package com.pinyougou.search.service.listener;

import com.pinoyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class SearchMessageListener implements MessageListener{

	@Autowired
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
		try {
			//操作，获取消息


			ObjectMessage objectMessage = (ObjectMessage) message;
			Long[] ids = (Long[]) objectMessage.getObject();
			//根据商品的ID查询SKU（item）列表
			//导入到索引库
			for(Long goodsId:ids){
				searchService.importItem(goodsId);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
