package com.pinyougou.search.service.listener;

import com.pinoyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class SearchDeleteMessageListener implements MessageListener {

	@Autowired
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
	//操作，获取消息
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			Long[] ids = (Long[]) objectMessage.getObject();
			//根据商品的ID
			//删除索引库数据
			for(Long goodsId:ids){
				searchService.deleteItem(goodsId);

			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
