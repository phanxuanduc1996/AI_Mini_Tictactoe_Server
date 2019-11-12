package com.codelovers.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Subselect;
@Entity
@Subselect("select * from match_game, result_match")
public class MatchWithPlayerName {
	@Id
    private Long id;

    private Integer roundId;

    private Long playerId1;

    private Long playerId2;
    
    private Long playerWin;
    
    private Integer status;
    
    private String playerName1;
    
    private String playerName2;
    
    private Integer roundNo;
    
    private Integer count1Win;
    
    private Integer count2Win;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoundId() {
        return roundId;
    }

    public void setRoundId(Integer roundId) {
        this.roundId = roundId;
    }

    public Long getPlayerId1() {
        return playerId1;
    }

    public void setPlayerId1(Long playerId1) {
        this.playerId1 = playerId1;
    }

    public Long getPlayerId2() {
        return playerId2;
    }

    public void setPlayerId2(Long playerId2) {
        this.playerId2 = playerId2;
    }

    public Long getPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(Long playerWin) {
        this.playerWin = playerWin;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlayerName1() {
        return playerName1;
    }

    public void setPlayerName1(String playerName1) {
        this.playerName1 = playerName1;
    }

    public String getPlayerName2() {
        return playerName2;
    }

    public void setPlayerName2(String playerName2) {
        this.playerName2 = playerName2;
    }

    public Integer getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(Integer roundNo) {
        this.roundNo = roundNo;
    }
    
    public Integer getCount1Win() {
    	return count1Win;
    }
    
    public void setCount1Win(Integer count1Win) {
    	this.count1Win = count1Win;
    }

    public Integer getCount2Win() {
    	return count2Win;
    }
    
    public void setCount2Win(Integer count2Win) {
    	this.count2Win = count2Win;
    }
    
}
