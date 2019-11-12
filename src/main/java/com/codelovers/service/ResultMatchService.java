package com.codelovers.service;

import java.util.List;

import com.codelovers.model.ResultMatch;

public interface ResultMatchService {
	
	ResultMatch findLastResultMatchByUserId(long userId);
	ResultMatch findMatchId(long matchId);
	List<ResultMatch> findByMatchId(long macthId);
	ResultMatch findByMatchIdAndRoundNo(Long matchId);
	void save(ResultMatch match);
	void delete(long id);
	void deleteWithMatchID(long matchId);
	List<ResultMatch> findByResultMatchId(int resultMatchId);

}
