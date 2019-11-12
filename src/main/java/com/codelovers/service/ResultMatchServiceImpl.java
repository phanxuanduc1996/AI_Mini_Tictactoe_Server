package com.codelovers.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codelovers.model.ResultMatch;
import com.codelovers.repository.ResultMatchRepository;

@Service
public class ResultMatchServiceImpl implements ResultMatchService {

	@Autowired
	ResultMatchRepository repository;


	@Override
	public ResultMatch findLastResultMatchByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResultMatch findMatchId(long matchId) {
		List<ResultMatch> rm = repository.findByMatchId(matchId);
		return (ResultMatch) rm;
	}
	
	@Override
	public ResultMatch findByMatchIdAndRoundNo(Long matchId) {
		return repository.findByMatchIdAndRoundNo(matchId);
	}

	@Override
	public void save(ResultMatch match) {
		// TODO Auto-generated method stub
		repository.save(match);
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		repository.delete(id);
	}

	@Transactional
	@Override
	public void deleteWithMatchID(long matchId) {
		// TODO Auto-generated method stub
		List<ResultMatch> list = repository.findByMatchId(matchId);
		for(ResultMatch r : list){
			repository.delete(r.getId());
		}
		
	}

	@Override
	public List<ResultMatch> findByMatchId(long macthId) {
		// TODO Auto-generated method stub
		return repository.findByMatchId(macthId);
	}
	
	@Override
	public List<ResultMatch> findByResultMatchId(int resultMatchId) {
		// TODO Auto-generated method stub
		return repository.findByResultMatchId(resultMatchId);
	}

}
