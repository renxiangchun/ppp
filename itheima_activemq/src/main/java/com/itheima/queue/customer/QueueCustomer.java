package com.itheima.queue.customer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueCustomer {
	public static void main(String[] args) throws Exception {
		//1、创建工厂连接ActiveMQ
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、获取连接
		Connection connection = activeMQConnectionFactory.createConnection();
		//3、开启连接
		connection.start();
		//4、创建session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、创建Queue，消息类型
		Queue queue = session.createQueue("test-queue");
		//6、创建消费者（角色）
		MessageConsumer consumer = session.createConsumer(queue);
		//7、写一个监听：这个监听是来监听我们的消息的
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					//获取消息并打印
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//8、等待...不要监听完了程序就关闭了，再等待一会；
		System.in.read();
		//9、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
