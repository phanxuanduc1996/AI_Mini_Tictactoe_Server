package com.codelovers.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Admin on 1/3/2017.
 */
@Entity
@Table(name = "match_game")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "round_id")
    private Integer roundId;

    @Column(name = "player_id1")
    private Long playerId1;

    @Column(name = "player_id2")
    private Long playerId2;
    
    @Column(name = "player_win")
    private Long playerWin;
    
    @Column(name = "status")
    private Integer status;

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
    
}
