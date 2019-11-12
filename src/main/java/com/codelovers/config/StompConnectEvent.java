package com.codelovers.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
@Component
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent>{
	private final Log logger = LogFactory.getLog(StompConnectEvent.class);

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		// TODO Auto-generated method stub
//		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//		System.out.println("test");
//		System.out.println(sha.getNativeHeader("Company"));
//      String  company = sha.getNativeHeader("Company").get(0);
//      logger.info("Connect event [sessionId: " + sha.getDestination() +"; Company: "+ company + " ]");
        
		
	}
	 
    
}
