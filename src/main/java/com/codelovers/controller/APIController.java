package com.codelovers.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codelovers.model.Chanel;
import com.codelovers.model.Match;
import com.codelovers.model.MatchWithPlayerName;
import com.codelovers.model.ResultMatch;
import com.codelovers.model.Users;
import com.codelovers.model.caro.XYLocation;
import com.codelovers.service.ChanelService;
import com.codelovers.service.MatchService;
import com.codelovers.service.ResultMatchService;
import com.codelovers.service.UsersService;
import com.codelovers.service.game.BotAuto;
import com.codelovers.service.game.CaroGame;
import com.codelovers.service.game.CaroState;

/**
 * Created by P.X.Duc on 19/4/2017.
 */
@Controller
public class APIController {
	
	private final Log logger = LogFactory.getLog(GameController.class);
	public final static String X_HAS_WON = "10";
	public final static String O_HAS_WON = "01";
	public final static String NO_WINER = "00";

	public final static String END_ROUND = "end_round";
	public final static String END_GAME = "end_game";
	public final static String FORWARD = "forward";
	public final static String SURRENDER = "surrender";
	public final static String RESET = "reset";
	public final static String BOT_FIRST = "bot_first";

	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	ChanelService chanelInforService;
	@Autowired
	UsersService usersService;

	public static CaroState currState = CaroGame.getInitialState();
	String statusText = "";
	String strRender = "";
	Long matchID;
	int round;
	String player1;
	String player2;
	int countRoundPlayer1Win=0;
	int countRoundPlayer2Win=0;

	String strPlayerWin = "";
	String strRenderResult = "";
	String gameStatus = "";
	BotAuto botAuto = new BotAuto();
//	MatchController MatchController =  new MatchController();

	@Autowired
	MatchService matchService;
	@Autowired
	ResultMatchService resultMatchService;

	@MessageMapping("/api/go")
	public void matchGO(String message) throws Exception {

		JSONObject obj = new JSONObject(message);
		String username = obj.get("username").toString();
		String password = obj.get("password").toString();
		String strMatchID = obj.get("matchID").toString();
		int x = Integer.parseInt(obj.get("X").toString());
		int y = Integer.parseInt(obj.get("Y").toString());
		String surrender = obj.get("surrender").toString();
		int roundOfMatch = Integer.parseInt(obj.get("roundOfMatch").toString());
		strRender = "{\"username\":\"" + username + "\", \"password\":\"" + password +  "\",\"X\":\"" + x
				+ "\", \"Y\":\"" + y + "\", \"surrender\":\"" + surrender + "\"}";
		webSocket.convertAndSend("/topic/matchGo/", strRender);

		switch (round) {
		case 1:

			gameWithBOT(message);
			break;

		default:

			gamePlayerAndPlayer(message);
			break;
		}

	}

	private void gameWithBOT(String message) {

		JSONObject obj = new JSONObject(message);
		String strMatchID = obj.get("matchID").toString();
		int x = Integer.parseInt(obj.get("X").toString());
		int y = Integer.parseInt(obj.get("Y").toString());
		int roundOfMatch = Integer.parseInt(obj.get("roundOfMatch").toString());

		String messageSend = botAuto.botPlayState(x, y, "", matchID, roundOfMatch);
		webSocket.convertAndSend("/topic/matchGo/" + matchID, messageSend);
		JSONObject objBOT = new JSONObject(messageSend);
		statusText = objBOT.get("gameFlag").toString();

		if (statusText.equals(X_HAS_WON) || statusText.equals(O_HAS_WON)) {
			if (statusText.equals(X_HAS_WON)) {
				if (roundOfMatch == 2)
				{
					strPlayerWin = player1;
				}
				else
				{
					strPlayerWin = player2;
				}
			} else if (statusText.equals(O_HAS_WON)) {
				if (roundOfMatch == 2)
				{
					strPlayerWin = player2;
				}
				else
				{
					strPlayerWin = player1;
				}
			}
			// reset match state
			botAuto.botPlayState(x, y, RESET, matchID, roundOfMatch);

			if (roundOfMatch <= 3) {
				// next round save db
				gameStatus = END_ROUND;
				if (matchID != null) {
					if (roundOfMatch == 1) {
						resultMatchService.deleteWithMatchID(matchID);
					}
					ResultMatch resultMatch = resultMatchService.findByMatchIdAndRoundNo(this.matchID);
//					resultMatch.setMatchId(this.matchID);
					resultMatch.setRoundNo(roundOfMatch);
					if (strPlayerWin.equals(player1))
						countRoundPlayer1Win++;
					resultMatch.setCountRoundPlayer1Win(countRoundPlayer1Win);
					if (strPlayerWin.equals(player2))
						countRoundPlayer2Win++;
					resultMatch.setCountRoundPlayer2Win(countRoundPlayer2Win);
					resultMatch.setTimeEnd(new Timestamp(new Date().getTime()));
					resultMatch.setUserId(
					strPlayerWin.equals("bot") ? 0L : usersService.findByUsername(strPlayerWin).getId());
					resultMatchService.save(resultMatch);
				}
				
				currState = CaroGame.getInitialState();
				strRenderResult = "{\"userCode\":\"" + strPlayerWin + "\", \"matchID\":\"" + strMatchID + "\",\"X\":\""
						+ x + "\", \"Y\":\"" + y + "\", \"gameFlag\":\"" + gameStatus + "\",\"roundOfMatch\":\""
						+ roundOfMatch + "\"}";
				webSocket.convertAndSend("/topic/matchGo/" + matchID, strRenderResult);

			}

			if (roundOfMatch == 1) { // sau khi ket thuc round 1 -> tiep round 2

					String botFirstMess = botAuto.botPlayState(9, 9, BOT_FIRST, matchID, roundOfMatch + 1);
					webSocket.convertAndSend("/topic/matchGo/" + matchID, botFirstMess);

			} else if (roundOfMatch == 3) {
				gameStatus = END_GAME;
				String winerOfMatch = player1;
				long playerWin = player1.equals("bot") ? 0 : usersService.findByUsername(player1).getId();
				if (countRoundPlayer1Win < countRoundPlayer2Win) {
					playerWin = usersService.findByUsername(player2).getId();
					winerOfMatch = player2;
				}

				Match m = matchService.findById(matchID);
				m.setPlayerWin(playerWin);
				m.setStatus(1);
//				m.setTimeEnd(new Timestamp(new Date().getTime()));
				matchService.save(m);

				currState = CaroGame.getInitialState();
				strRenderResult = "{\"userCode\":\"" + winerOfMatch + "\", \"matchID\":\"" + strMatchID + "\",\"X\":\""
						+ x + "\", \"Y\":\"" + y + "\", \"gameFlag\":\"" + gameStatus + "\",\"roundOfMatch\":\""
						+ roundOfMatch + "\"}";
				webSocket.convertAndSend("/topic/matchGo/" + matchID, strRenderResult);
			}

		}

	}

	private void gamePlayerAndPlayer(String message) {

		JSONObject obj = new JSONObject(message);
		String strMatchID = obj.get("matchID").toString();
		int x = Integer.parseInt(obj.get("X").toString());
		int y = Integer.parseInt(obj.get("Y").toString());
		int roundOfMatch = Integer.parseInt(obj.get("roundOfMatch").toString());

		playerRenderMove(x, y);

		if (statusText.equals(X_HAS_WON) || statusText.equals(O_HAS_WON)) {
			if (statusText.equals(X_HAS_WON)) {
				if (roundOfMatch == 2)
					strPlayerWin = player2;
				else
					strPlayerWin = player1;
			} else if (statusText.equals(O_HAS_WON)) {
				if (roundOfMatch == 2)
					strPlayerWin = player1;
				else
					strPlayerWin = player2;
			}
			// reset match state
			currState = CaroGame.getInitialState();

			if (roundOfMatch <= 3) {
				// next round save db
				gameStatus = END_ROUND;
				if (matchID != null) {
					if (roundOfMatch == 1) {
						resultMatchService.deleteWithMatchID(matchID);
					}
					ResultMatch resultMatch = resultMatchService.findByMatchIdAndRoundNo(this.matchID);
					
//					ResultMatch rm = ResultMatchService.findById(resultmatchId);
//					resultMatch.setMatchId(this.matchID);
					resultMatch.setRoundNo(roundOfMatch);
					if (strPlayerWin.equals(player1))
						countRoundPlayer1Win++;
					resultMatch.setCountRoundPlayer1Win(countRoundPlayer1Win);
					if (strPlayerWin.equals(player2))
						countRoundPlayer2Win++;
					resultMatch.setCountRoundPlayer2Win(countRoundPlayer2Win);
					resultMatch.setTimeEnd(new Timestamp(new Date().getTime()));
					resultMatch.setUserId(usersService.findByUsername(strPlayerWin).getId());
					resultMatchService.save(resultMatch);
				}
				currState = CaroGame.getInitialState();
				strRenderResult = "{\"userCode\":\"" + strPlayerWin + "\", \"matchID\":\"" + strMatchID + "\",\"X\":\""
						+ x + "\", \"Y\":\"" + y + "\", \"gameFlag\":\"" + gameStatus + "\",\"roundOfMatch\":\""
						+ roundOfMatch + "\"}";
				webSocket.convertAndSend("/topic/matchGo/" + matchID, strRenderResult);

			}

			if (roundOfMatch == 3) {
				gameStatus = END_GAME;
				String winerOfMatch = player1;
				long playerWin = usersService.findByUsername(player1).getId();
				if (countRoundPlayer1Win < countRoundPlayer2Win) {
					playerWin = usersService.findByUsername(player2).getId();
					winerOfMatch = player2;
				}

				Match m = matchService.findById(matchID);
				m.setPlayerWin(playerWin);
				m.setStatus(1);
//				m.setTimeEnd(new Timestamp(new Date().getTime()));
				matchService.save(m);

				currState = CaroGame.getInitialState();
				strRenderResult = "{\"userCode\":\"" + winerOfMatch + "\", \"matchID\":\"" + strMatchID + "\",\"X\":\""
						+ x + "\", \"Y\":\"" + y + "\", \"gameFlag\":\"" + gameStatus + "\",\"roundOfMatch\":\""
						+ roundOfMatch + "\"}";
				webSocket.convertAndSend("/topic/matchGo/" + matchID, strRenderResult);
			}

		}
	}

	@Transactional
	@SubscribeMapping("/queue/active/{matchId}")
	@MessageMapping("/active/{matchId}")
	private void sub(String message, @DestinationVariable String matchId) throws Exception {
		String chanelMatch = "";
		JSONObject obj = new JSONObject(message);
		String userActive = obj.get("userCode").toString();// user code
		int totalUserInChanel = Integer.parseInt(obj.get("totalUserInChanel").toString());

		int isNext = 0;

		Users user = usersService.findByUsername(userActive);

		if (user != null) {
			long userid = user.getId();
			Match match = matchService.findLastMatchByPlayerId1OrPlayerId2(userid, userid);
			if (match != null) {
				matchID = match.getId();
				round = match.getRoundId();
				if (match.getStatus().equals(0) && match.getPlayerId2() != null
						&& match.getId() == Long.parseLong(matchId)) {

					isNext = 1;
					player1 = match.getPlayerId1() == 0 ? "bot"
							: usersService.findById(match.getPlayerId1()).getUsername();
					player2 = usersService.findById(match.getPlayerId2()).getUsername();
					chanelMatch = round + "/" + player1 + "/" + player2;

				} else {
					isNext = 0;
				}
			}
		}

		if (isNext == 1) {
			Chanel chanelInfo = chanelInforService.findByChanelName(chanelMatch);
			if (chanelInfo != null) {
				totalUserInChanel = chanelInfo.getCountUsers() + 1;
				chanelInfo.setCountUsers(totalUserInChanel);
				chanelInforService.save(chanelInfo);
			} else {
				Chanel nChanel = new Chanel();
				logger.info("chanel" + chanelMatch);
				nChanel.setChanelName(chanelMatch);
				nChanel.setCountUsers(1);
				chanelInforService.save(nChanel);
			}

			webSocket.convertAndSend("/queue/active/" + matchId,
					"{\"userCode\":\"" + userActive + "\", \"chanelMatch\":\"" + chanelMatch
							+ "\", \"totalUserInChanel\":\"" + totalUserInChanel + "\"}");

		}

	}

	private String updateStatus(CaroState currState) {

		if (CaroGame.isTerminal(currState)) {
			if (CaroGame.getUtility(currState, CaroState.X) == 1) {
				statusText = X_HAS_WON;
			} else if (CaroGame.getUtility(currState, CaroState.O) == 1) {
				statusText = O_HAS_WON;
			} else {
				statusText = NO_WINER;
			}
		} else {
			statusText = FORWARD;
		}
		return statusText;

	}

	public void playerRenderMove(int x, int y) {
		currState = CaroGame.getResult(currState, new XYLocation(x, y));
		System.out.println("Player\n" + currState.toString1());
		updateStatus(currState);

	}
	

}
