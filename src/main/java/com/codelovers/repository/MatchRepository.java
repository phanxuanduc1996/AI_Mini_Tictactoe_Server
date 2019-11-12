package com.codelovers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.codelovers.model.Match;
@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{
	@Query(value="select * from match_game where player_id1 = ?1 or player_id2 = ?2 order by id desc limit 1",nativeQuery = true)
	Match findLastMatchByPlayerId1OrPlayerId2(long playerId1,long playerId2);
	
	@Query(value="select * from match_game where player_win = ? order by id desc limit 1",nativeQuery = true)
	Match findLastMatchByPlayerWin(long userId);
	
	@Query(value="select * from match_game where  round_id = ?3  and (player_id1 = ?1 or player_id2 = ?2)",nativeQuery = true)
	Match findByPlayerId1OrPlayerId2AndRoundId(long playerId1,long playerId2,int round);
	
}
