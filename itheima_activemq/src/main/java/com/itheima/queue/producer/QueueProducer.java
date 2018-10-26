package com.itheima.queue.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducer {
	public static void main(String[] args) throws JMSException {
		//1、创建工厂：连接ActiveMQ的URL;61616是ActiveMQ客户端连接端口号
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616");
		//2、获取连接
		Connection connection = activeMQConnectionFactory.createConnection();
		//3、开启连接
		connection.start();
		//4、通过这个连接获取Session
		//参数说明：1、是否启动事物；2、指定消息的发布形式：是自动提交还是手动;咱们用的是1：自动提交
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、根据这个session对象创建队列类型Queue/Topic
		//Topic topic = session.createTopic("test-queue");
		Queue queue = session.createQueue("test-queue");
		//6、根据这个session来创建生产者/消费者
		MessageProducer producer = session.createProducer(queue);
		//7、创建消息体对象：TextMessage/MapMessage/ObjectMessage
		TextMessage textMessage = session.createTextMessage();
		//8、设置消息内容
		textMessage.setText("queue的第3次...");
		//9、发送消息
		producer.send(textMessage);
		//10、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
}
