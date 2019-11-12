package com.codelovers.service;

import java.util.List;

import com.codelovers.model.Match;
import com.codelovers.model.MatchWithPlayerName;

public interface MatchService {
	Match findLastMatchByPlayerId1OrPlayerId2(long playerId1,long playerId2);
	Match findById(long id);
	Match findLastMatchByPlayerWin(long userId);
	List<Match> findAll();
	List<MatchWithPlayerName> findAllMatchWithNamePlayerOnRoundId(int round);
	void save(Match match);
	void delete(long id);
	Match findByPlayerId1OrPlayerId2AndRoundId(long playerId1,long playerId2,int round);
}
