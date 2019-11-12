package com.codelovers.model;

import java.sql.Timestamp;

import javax.persistence.*;

/**
 * Created by Admin on 1/3/2017.
 */
@Entity
@Table(name = "result_match")
public class ResultMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "match_id")
    private Long matchId;
    
    @Column(name = "round_no")
    private Integer roundNo;
    
    @Column(name = "count1_win")
    private Integer countRoundPlayer1Win;
    
    @Column(name = "count2_win")
    private Integer countRoundPlayer2Win;
    
    @Column(name = "time_start")
    private Timestamp timeStart;
    
    @Column(name = "time_end")
    private Timestamp timeEnd;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Integer getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int matchId) {
        this.roundNo = matchId;
    }
    
    public Integer getCountRoundPlayer1Win() {
    	return countRoundPlayer1Win;
    }
    
    public void setCountRoundPlayer1Win(Integer countRoundPlayer1Win) {
    	this.countRoundPlayer1Win = countRoundPlayer1Win;
    }

    public Integer getCountRoundPlayer2Win() {
    	return countRoundPlayer2Win;
    }
    
    public void setCountRoundPlayer2Win(Integer countRoundPlayer2Win) {
    	this.countRoundPlayer2Win = countRoundPlayer2Win;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }
}
