package com.itheima.spring.topic.customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:topic/applicationContext-jms-customer.xml")
public class SpringJMSTopicCustomerTest {

	@Test
	public void message(){
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}