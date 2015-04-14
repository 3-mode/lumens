/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.logsys;

import java.io.InputStream;
import java.util.Properties;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class JmsClientApplication implements MessageListener {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream in = LogApplication.class.getResourceAsStream("/conf/jndi.properties")) {
            props.load(in);
        }
        InitialContext ctx = new InitialContext(props);
        // lookup the connection factory
        javax.jms.QueueConnectionFactory factory = (javax.jms.QueueConnectionFactory) ctx.lookup("ConnectionFactory");
        javax.jms.QueueConnection conn = factory.createQueueConnection();
        javax.jms.QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        Destination adminQueue = (Destination) ctx.lookup("MyQueue");
        //Set up a consumer to consume messages off of the admin queue
        MessageConsumer consumer = session.createConsumer(adminQueue);
        consumer.setMessageListener(new JmsClientApplication());
        conn.start();
    }

    public static void produce() throws NamingException, JMSException {
        // create a new intial context, which loads from jndi.properties file
        javax.naming.Context ctx = new javax.naming.InitialContext();
        // lookup the connection factory
        javax.jms.QueueConnectionFactory factory = (javax.jms.QueueConnectionFactory) ctx.lookup("ConnectionFactory");
        javax.jms.QueueConnection conn = factory.createQueueConnection();
        javax.jms.Queue myqueue = (javax.jms.Queue) ctx.lookup("MyQueue");
        System.out.println("Queue name: " + myqueue.getQueueName());
        javax.jms.QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        javax.jms.QueueSender queueSender = session.createSender(myqueue);
        TextMessage message = session.createTextMessage();
        message.setText("test");
        System.out.println("Send message");
        queueSender.send(message);
        System.out.println("Complete sending");
        session.close();
        conn.close();
    }

    @Override
    public void onMessage(Message msg) {
        if (msg instanceof TextMessage) {
            TextMessage text = (TextMessage) msg;
            try {
                System.out.println("Get message: " + text.getText());
            } catch (JMSException ex) {
            }
        }
    }
}
