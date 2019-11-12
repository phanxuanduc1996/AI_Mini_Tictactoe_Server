package com.codelovers.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
	private final Log logger = LogFactory.getLog(PresenceChannelInterceptor.class);
	@Autowired
	WebSocketMessageBrokerStats stats;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
		if (StompCommand.SUBSCRIBE.equals(sha.getCommand())) {
			String process = stats.getStompSubProtocolStatsInfo();
			logger.info("process.charAt(18) : "+process.charAt(18));
			logger.info("StompCommand.SUBSCRIBE :  " + sha.getCommand() + "]");
			logger.info("WebSocketMessageBrokerStats :  " + stats.getStompSubProtocolStatsInfo() + "]");
			logger.info("getDestination ::::" +sha.getDestination());

		}
		return message;
	}

	@Override
	public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

		StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

		// ignore non-STOMP messages like heartbeat messages
		if (sha.getCommand() == null) {
			return;
		}

		String sessionId = sha.getSessionId();

		switch (sha.getCommand()) {
		case CONNECT:
			logger.info("STOMP Connect [sessionId: " + sessionId + "]");
			break;
		case CONNECTED:
			logger.info("STOMP Connected [sessionId: " + sessionId + "]");
			break;
		case DISCONNECT:
			logger.info("STOMP Disconnect [sessionId: " + sessionId + "]");
			break;
		default:
			break;

		}
	}
}
