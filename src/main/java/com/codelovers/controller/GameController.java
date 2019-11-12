package com.codelovers.controller;

import java.sql.Timestamp;
import java.util.Date;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codelovers.model.Chanel;
import com.codelovers.model.Match;
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
 * Created by Admin on 1/6/2017.
 */
@Controller
public class GameController {

	@Autowired
	ResultMatchService resultMatchService;
	
	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "/startRoundMatch/{matchId}", method = RequestMethod.POST)
	private ResponseEntity<String> startRoundMatch(@PathVariable long matchId, Model model) {
			ResultMatch resultMatch = new ResultMatch();
			resultMatch.setMatchId(matchId);
			resultMatch.setTimeStart(new Timestamp(new Date().getTime()));
			resultMatchService.save(resultMatch);
			model.addAttribute("matchId", matchId);
			return new ResponseEntity<String>(HttpStatus.OK);		
	}

}
