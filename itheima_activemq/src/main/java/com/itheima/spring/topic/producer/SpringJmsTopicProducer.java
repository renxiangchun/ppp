package com.itheima.spring.topic.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class SpringJmsTopicProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination  topicTextDestination;

	public void send(String message){
		jmsTemplate.send(topicTextDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});

	}
}
