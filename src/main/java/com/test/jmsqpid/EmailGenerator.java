package com.test.jmsqpid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.math.BigDecimal;


@Service
public class EmailGenerator {

    private static final Log LOG = LogFactory.getLog(EmailGenerator.class);

    private JmsTemplate jmsTemplate;

    @Autowired
    public EmailGenerator(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

   @SuppressWarnings("Duplicates")
    @Scheduled(fixedDelay = 5000)
    public void generateEmail() {
        Email email = new Email();
        email.setEmailAddress("abc@test.com");
        email.setName("Grant");
        email.setMessage("This is an example email");
        LOG.info("Sending email " + email);
        jmsTemplate.convertAndSend("Emails", email, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("JMSXGroupID", email.getEmailAddress());
                return message;
            }
        });
    }
}
