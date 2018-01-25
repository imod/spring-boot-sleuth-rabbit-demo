package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import com.netshoes.springframework.cloud.sleuth.instrument.amqp.AmqpMessagingSpanManager;

/**
 * a workaround implementation to fix:
 * https://github.com/netshoes/spring-cloud-sleuth-amqp/issues/13
 * 
 * @author domi
 *
 */
public class AmqpMessagingSpanSleuthExtractorMessageConverterWrapper implements MessageConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AmqpMessagingSpanSleuthExtractorMessageConverterWrapper.class);

	private final MessageConverter wrappedConverter;
	private final AmqpMessagingSpanManager spanManager;

	public AmqpMessagingSpanSleuthExtractorMessageConverterWrapper(MessageConverter wrappedConverter, AmqpMessagingSpanManager spanManager) {
		this.wrappedConverter = wrappedConverter;
		this.spanManager = spanManager;
	}

	@Override
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
		return wrappedConverter.toMessage(object, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		try {
			spanManager.beforeHandle(message, new String[] {});
		} catch (Exception e) {
			LOGGER.error("failed to handle sleuth 'span' before converting message!", e);
		}
		return wrappedConverter.fromMessage(message);
	}

}
