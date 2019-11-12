package com.codelovers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.codelovers.model.ResultMatch;
@Repository
public interface ResultMatchRepository extends JpaRepository< ResultMatch, Long> {
	@Query(value="select * from result_match where match_id = ? order by id desc limit 1",nativeQuery = true)
	List<ResultMatch> findByMatchId(Long matchId);
	
	@Query(value="select * from result_match where id = ? order by id desc limit 1",nativeQuery = true)
	List<ResultMatch> findByResultMatchId(int resultMatchId);

	@Query(value="select * from result_match where match_id = ?1 order by id desc limit 1", nativeQuery = true)
	ResultMatch findByMatchIdAndRoundNo(Long matchId);
}
