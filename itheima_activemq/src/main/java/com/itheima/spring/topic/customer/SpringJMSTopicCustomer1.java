package com.itheima.spring.topic.customer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class SpringJMSTopicCustomer1 implements MessageListener {
    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {
            System.err.println("第1个监听类："+textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
