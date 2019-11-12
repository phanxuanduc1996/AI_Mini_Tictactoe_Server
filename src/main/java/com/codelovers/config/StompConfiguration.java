package com.codelovers.config;

import javax.websocket.MessageHandler;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
import org.springframework.integration.stomp.Reactor2TcpStompSessionManager;
import org.springframework.integration.stomp.StompSessionManager;
import org.springframework.integration.stomp.inbound.StompInboundChannelAdapter;
import org.springframework.integration.stomp.outbound.StompMessageHandler;
import org.springframework.integration.support.converter.PassThruMessageConverter;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.simp.stomp.Reactor2TcpStompClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

//@Configuration
//@EnableIntegration
//@EnableAutoConfiguration
public class StompConfiguration {
	@Bean
	public Reactor2TcpStompClient stompClient() {
		Reactor2TcpStompClient stompClient = new Reactor2TcpStompClient("127.0.0.1", 8004);
		stompClient.setMessageConverter(new PassThruMessageConverter());
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.afterPropertiesSet();
		stompClient.setTaskScheduler(taskScheduler);
		stompClient.setReceiptTimeLimit(5000);
		return stompClient;
	}

	@Bean
	public StompSessionManager stompSessionManager() {
		Reactor2TcpStompSessionManager stompSessionManager = new Reactor2TcpStompSessionManager(stompClient());
		stompSessionManager.setAutoReceipt(true);
		return stompSessionManager;
	}

	@Bean
	public PollableChannel stompInputChannel() {
		return new QueueChannel();
	}

	@Bean
	public StompInboundChannelAdapter stompInboundChannelAdapter() {
		StompInboundChannelAdapter adapter = new StompInboundChannelAdapter(stompSessionManager(), "/topic/");
		adapter.setOutputChannel(stompInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "stompOutputChannel")
	public MessageHandler stompMessageHandler() {
		StompMessageHandler handler = new StompMessageHandler(stompSessionManager());
		handler.setDestination("/topic/");
		return (MessageHandler) handler;
	}

	@Bean
	public PollableChannel stompEvents() {
		return new QueueChannel();
	}

	@Bean
	public ApplicationListener<ApplicationEvent> stompEventListener() {
		ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
		//producer.setEventTypes(StompIntegrationEvent.class);
		producer.setOutputChannel(stompEvents());
		return producer;
	}
}
