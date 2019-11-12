package com.codelovers.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.codelovers.model.Match;
import com.codelovers.model.MatchWithPlayerName;
import com.codelovers.model.ResultMatch;
import com.codelovers.model.Users;
import com.codelovers.service.MatchService;
import com.codelovers.service.ResultMatchService;
import com.codelovers.service.UsersService;

@Controller
@EnableTransactionManagement
public class MatchController {
	@Autowired
	MatchService matchService;
	@Autowired
	UsersService usersService;
	@Autowired
	ResultMatchService resultMatchService;
	
/*	ResultMatch round;
	
	public ResultMatch getRound(){
		return round;
	}
	public void setRound(ResultMatch round){
		this.round = round;
	}
*/

	@RequestMapping("/match")
	public String match(Model model) throws Exception {
		return "public/match";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping("/matchBot/{matchId}")
	public String matchBot(Model model,@PathVariable String matchId) throws Exception {
		model.addAttribute("matchId",matchId);
		return "matchBot";
	}

	@RequestMapping("/match/{matchId}")
	public String matchViewer(@PathVariable String matchId, Model model) throws Exception {
		model.addAttribute("matchId",matchId);
		return "public/match";
	}

	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "/admin/match/addBot", method = RequestMethod.POST)
	public ResponseEntity<String> createMatchWithBot() {
		List<Users> lUsers = usersService.findAll();
		for (Users u : lUsers) {
			if (u.getRole() != 1) {
				Match exsitM = matchService.findByPlayerId1OrPlayerId2AndRoundId(u.getId(), u.getId(), 1);
				if (exsitM == null) {
					Match matchWithBOT = new Match();
					matchWithBOT.setPlayerId1(0L);
					matchWithBOT.setPlayerId2(u.getId());
					matchWithBOT.setRoundId(1);
					matchWithBOT.setStatus(0);
					matchService.save(matchWithBOT);
				}
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@Transactional
	@RequestMapping(value = "/admin/match/{round}", method = RequestMethod.POST)
	public ResponseEntity<String> createMatch(@PathVariable int round) {
		// chi khi da end round1
		// neu nguoi choi chua choi vs round 1 vs bot truoc do thi mac dinh se
		// thua va khong cho vao vong 2
		// random nguoi se bi le neu so nguoi choi la le, nguoi nay se truc tiep
		// vao vong trong
		List<MatchWithPlayerName> lMatchs = matchService.findAllMatchWithNamePlayerOnRoundId(round - 1);
		ArrayList<Long> arrUserId = new ArrayList<>();
		for (MatchWithPlayerName m : lMatchs) {
			if (m.getStatus() == 1 && m.getPlayerWin()!=0) {
				arrUserId.add(m.getPlayerWin());
			}
		}
		// delete het ac match trong round neu chay set match cua round lai lan
		// nua
		List<MatchWithPlayerName> lMatchsDelete = matchService.findAllMatchWithNamePlayerOnRoundId(round);
		for (MatchWithPlayerName mD : lMatchsDelete) {
			matchService.delete(mD.getId());
		}
		int countUserNextRound = arrUserId.size();
		if (countUserNextRound % 2 == 1) {
			int LuckyUserNo = (int) (Math.random() * countUserNextRound);
			long LuckyUserId = arrUserId.get(LuckyUserNo);

			Match luckyMatch = new Match();
			luckyMatch.setPlayerId1(LuckyUserId);
			luckyMatch.setPlayerId2(LuckyUserId);
			luckyMatch.setPlayerWin(LuckyUserId);
			luckyMatch.setRoundId(round);
			luckyMatch.setStatus(1);
			matchService.save(luckyMatch);
			arrUserId.remove(LuckyUserNo);
		}
		int countUserNextRoundAfterRandom = arrUserId.size();
		for (int i = 0; i <= countUserNextRoundAfterRandom / 2; i++) {
			Match m = new Match();
			m.setPlayerId1(arrUserId.get(i));
			m.setPlayerId2(arrUserId.get(countUserNextRoundAfterRandom - 1));
			m.setRoundId(round);
			m.setStatus(0);
			countUserNextRoundAfterRandom--;
			matchService.save(m);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/match/viewMatch", method = RequestMethod.GET)
	public String getAllMatch(Model model) {
		int countRound = 1;
		int countUser = 0;
		List<Users> lUsers = usersService.findAll();
		for (Users u : lUsers) {
			if (u.getRole() != 1) {
				countUser++;
			}
		}
		int temp = countUser;
		while (temp >= 2) {
			temp = temp / 2 + temp % 2;
			countRound++;
		}

		ArrayList<List<MatchWithPlayerName>> arr = new ArrayList<>();
		for (int i = 1; i <= countRound; i++) {
			arr.add(matchService.findAllMatchWithNamePlayerOnRoundId(i));
		}

		model.addAttribute("aMatchs", arr);

		return "public/matchViewAll";
	}

}
