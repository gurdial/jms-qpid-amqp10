package com.test.jmsqpid;

import javax.jms.ConnectionFactory;


import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
public class JmsConfigurer {

  private Logger logger = LoggerFactory.getLogger(JmsConfigurer.class);

  @Autowired
  private AmqpProperties properties;


  @Bean
  @Primary
  public ConnectionFactory qpidConnectionFactory( ) {
    ConnectionFactory connectionFactory = new JmsConnectionFactory(properties.getUsername(), properties.getPassword(),properties.getUrl());
    return connectionFactory;
  }

  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter;
  }

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(@Qualifier("qpidConnectionFactory")
                                                                                ConnectionFactory connectionFactory){
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setDestinationResolver(new DynamicDestinationResolver());
    factory.setConcurrency("3-10");

    factory.setMessageConverter(jacksonJmsMessageConverter());

    return factory;

  }

}
