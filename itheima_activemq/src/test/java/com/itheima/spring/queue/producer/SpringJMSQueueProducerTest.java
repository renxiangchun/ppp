package com.itheima.spring.queue.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:queue/applicationContext-jms-producer.xml")
public class SpringJMSQueueProducerTest {

	@Autowired
	private SpringJMSProducer springJMSProducer;

	@Test
	public void send(){
		springJMSProducer.send("springJMS-queue第7次发送...");
		System.out.println("发送成功");
	}
}
