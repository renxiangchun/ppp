package com.itheima.spring.queue.customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:queue/applicationContext-jms-customer.xml")
public class SpringJMSQueueCustomerTest {

	@Test
	public void message(){
		try {
			System.out.println("00");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
