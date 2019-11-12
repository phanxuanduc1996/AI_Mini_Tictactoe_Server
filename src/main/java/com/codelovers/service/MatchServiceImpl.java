package com.codelovers.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codelovers.model.Match;
import com.codelovers.model.MatchWithPlayerName;
import com.codelovers.repository.MatchRepository;

@Service
public class MatchServiceImpl implements MatchService {
	@Autowired
	MatchRepository repository;
	@PersistenceContext
	private EntityManager em;

	@Override
	public Match findLastMatchByPlayerId1OrPlayerId2(long playerId1, long playerId2) {
		// TODO Auto-generated method stub
		return repository.findLastMatchByPlayerId1OrPlayerId2(playerId1, playerId2);
	}

	@Override
	public Match findById(long id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}

	@Override
	public Match findLastMatchByPlayerWin(long userId) {
		// TODO Auto-generated method stub
		return repository.findLastMatchByPlayerWin(userId);
	}

	@Override
	public void save(Match match) {
		// TODO Auto-generated method stub
		repository.saveAndFlush(match);
	}

	@Override
	public List<Match> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Match findByPlayerId1OrPlayerId2AndRoundId(long playerId1, long playerId2, int round) {
		// TODO Auto-generated method stub
		return repository.findByPlayerId1OrPlayerId2AndRoundId(playerId1, playerId2, round);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MatchWithPlayerName> findAllMatchWithNamePlayerOnRoundId(int round) {
		// TODO Auto-generated method stub
		String sql = "select m.id,m.player_id1,m.player_id2,"
				+ " ifnull(tb_Sub.count1_win, 0) as count1win, "
				+ " ifnull(tb_Sub.count2_win, 0) as count2win, "
				+ " ifnull(tb_Sub.round_no, 0) as round_no, "
				+ " ifnull(m.player_win,-1) as player_win, "
				+ " m.round_id,m.status, "
				+ " ifnull(u1.username,'BOT') as player_name1, "
				+ " u2.username as player_name2 from match_game as m"
				+ " left join users as u1 on  u1.id=m.player_id1 "
				+ " left join ( SELECT r.* from result_match as r where r.round_no =  (SELECT max(round_no)   FROM result_match where  result_match.match_id = r.match_id)"
//				+ " 	(SELECT r.* from result_match as r join  "
//				+ " 		(SELECT max(round_no) as maxr,match_id  FROM result_match  group by match_id) "
//				+ "			 as tb on tb.match_id = r.match_id and r.round_no = tb.maxr "
				+ "		) as tb_Sub on tb_Sub.match_id = m.id "
				+ " left join users as u2 on u2.id=m.player_id2 where m.round_id = "+round + " order by m.id asc, tb_Sub.round_no asc";
		return em.createNativeQuery(sql, MatchWithPlayerName.class).getResultList();
	}

	@Transactional
	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		repository.delete(id);
		
	}

}
