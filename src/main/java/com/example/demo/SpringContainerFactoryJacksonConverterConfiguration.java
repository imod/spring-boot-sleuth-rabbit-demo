package com.example.demo;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netshoes.springframework.cloud.sleuth.instrument.amqp.AmqpMessagingSpanManager;

@Configuration
public class SpringContainerFactoryJacksonConverterConfiguration {

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter, AmqpMessagingSpanManager spanManager) {
		final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		// NOK
		factory.setMessageConverter(messageConverter);
		// OK
		// factory.setMessageConverter(new AmqpMessagingSpanSleuthExtractorMessageConverterWrapper(messageConverter, spanManager));
		return factory;
	}

	@Bean
	public ConnectionFactory rabbit() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
		connectionFactory.setChannelCacheSize(5);
		return connectionFactory;
	}

	@Bean
	public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper mapper) {
		return new Jackson2JsonMessageConverter(mapper);
	}
}