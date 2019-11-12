package com.codelovers.service.game;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.codelovers.controller.APIController;
import com.codelovers.controller.GameController;
import com.codelovers.model.ResultMatch;
import com.codelovers.model.caro.XYLocation;
import com.codelovers.service.ChanelService;
import com.codelovers.service.MatchService;
import com.codelovers.service.ResultMatchService;
import com.codelovers.service.UsersService;

@Controller
public class BotAuto {

	@Autowired
	SimpMessagingTemplate webSocket;
	@Autowired
	ChanelService chanelInforService;
	@Autowired
	UsersService usersService;
	@Autowired
	MatchService matchService;
	@Autowired
	ResultMatchService resultMatchService;

	private String statusText = "";
	private String strRender = "";
	private  CaroState currState = APIController.currState;
	public final static String BOT_FIRST = "bot_first";
	
	public String botPlayState(int x, int y ,String gameFlag,long matchID,int roundOfMatch){

		switch (gameFlag) {
		case APIController.END_GAME:
		case APIController.RESET:
			currState = CaroGame.getInitialState();
			break;
		case BOT_FIRST:
			currState = CaroGame.getResult(currState, new XYLocation(x, y));
			System.out.println("bot\n" + currState.toString1());
			updateStatus(currState);
			strRender = "{\"userCode\":\"" + "bot" + "\", \"matchID\":\"" + matchID + "\", \"X\":\"" + x + "\","
					+ " \"Y\":\"" + y + "\"," + " \"gameFlag\":\"forward\",\"roundOfMatch\":\"" + roundOfMatch + "\"}";
			break;
		default:
			botGame(x, y);
			if (CaroGame.action != null){
				strRender = "{\"userCode\":\"" + "bot" + "\", \"matchID\":\"" + matchID + "\", \"X\":\""
						+ CaroGame.action.getX() + "\"," + " \"Y\":\"" + CaroGame.action.getY() + "\","
						+ " \"gameFlag\":\"" + statusText + "\",\"roundOfMatch\":\"" + roundOfMatch + "\"}";
			}else{
				strRender = "{\"userCode\":\"" + "bot" + "\", \"matchID\":\"" + matchID + "\", \"X\":\"" + x + "\","
						+ " \"Y\":\"" + y + "\"," + " \"gameFlag\":\"no_winer\",\"roundOfMatch\":\"" + roundOfMatch
						+ "\"}";
			}
			System.out.println("strRender::  "+strRender);
			break;
		}
		
		return strRender;
	}

	private String updateStatus(CaroState currState) {

		if (CaroGame.isTerminal(currState)) {
			if (CaroGame.getUtility(currState, CaroState.X) == 1) {
				statusText = APIController.X_HAS_WON;
			} else if (CaroGame.getUtility(currState, CaroState.O) == 1) {
				statusText = APIController.O_HAS_WON;
			} else {
				statusText = APIController.NO_WINER;
			}
		} else {
			statusText = APIController.FORWARD;
		}
		return statusText;

	}

	public void botGame(int x, int y) {
		if (!CaroGame.isTerminal(currState)) {

			currState = CaroGame.getResult(currState, new XYLocation(x, y));
			System.out.println("PLAYER\n" + currState.toString1());
			updateStatus(currState);

			if (CaroGame.isTerminal(currState)) {
				System.out.println("PLAYER WIN !");
			} else {
				currState = CaroGame.AIAgentMove(currState);
				System.out.println("AI\n" + currState.toString1());
				if (CaroGame.isTerminal(currState)) {
					System.out.println("AI WIN !");
				}
				updateStatus(currState);
			}

		}

	}
}
