package com.itheima.spring.topic.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:topic/applicationContext-jms-producer.xml")
public class SpringJMSTopicProducerTest {

	@Autowired
	private SpringJmsTopicProducer springJmsTopicProducer;

	@Test
	public void send(){
		springJmsTopicProducer.send("springjms-topic第4次发送...");
		System.out.println(2);
	}
}
