package com.topic.publisher;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import com.topic.util.AsynchTopicExample;
import com.topic.util.MQ;

 /** 
     * The Pulisher class publishes several message to a topic.  
     * 
     * @author Kim Haase 
     * @version 1.6, 08/18/00 
     */  
public class Publisher extends Thread {  
  
	
        /** 
         * Runs the thread. 
         */  
        public void run() {  
            ConnectionFactory  topicConnectionFactory = null;  
            Connection         topicConnection = null;  
            Session            topicSession = null;  
            Topic              topic = null;  
            MessageProducer    topicPublisher = null;  
            TextMessage        message = null;  
            final int          NUMMSGS = 20;  
            final String       MSG_TEXT = new String("Here is a message");  
            MQ				   mq = new MQ();
            try {  
                topicConnectionFactory = AsynchTopicExample.getJmsConnectionFactory();  
                topicConnection = topicConnectionFactory.createConnection();  
                topicSession = topicConnection.createSession(false,Session.AUTO_ACKNOWLEDGE);  
                topic = topicSession.createTopic(mq.getTopicName());  
            } catch (Exception e) {  
                System.out.println("Connection problem: " + e.toString());  
                if (topicConnection != null) {  
                    try {  
                        topicConnection.close();  
                    } catch (JMSException ee) {}  
                }  
                System.exit(1);  
            }   
  
            try {                  
                topicPublisher = topicSession.createProducer(topic);  
                message = topicSession.createTextMessage();  
                for (int i = 0; i < NUMMSGS; i++) {  
                	Thread.sleep(3000);
                    message.setText(MSG_TEXT + " " + (i + 1));  
                    System.out.println("发布者: 发布消息: " + message.getText());  
                    topicPublisher.send(message);  
                }  
  
                // Send a non-text control message indicating end of messages.  
                topicPublisher.send(topicSession.createMessage());  
            } catch (Exception e) {  
                System.out.println("Exception occurred: " + e.toString());  
//                exitResult = 1;  
            } finally {  
                if (topicConnection != null) {  
                    try {  
                        topicConnection.close();  
                    } catch (JMSException e) {  
//                        exitResult = 1;  
                    }  
                }  
            }  
        }  
    }  
      
   