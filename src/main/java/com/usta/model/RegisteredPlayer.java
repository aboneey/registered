/**
 * 
 */
package com.usta.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author anil.bonigala
 *
 */
@Entity
@Table(name = "reg_player")
public class RegisteredPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "trn_name")
    private String trnName;

    @Column(name = "reg_date")
    private Date regDate;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName
     *            the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return the trnName
     */
    public String getTrnName() {
        return trnName;
    }

    /**
     * @param trnName
     *            the trnName to set
     */
    public void setTrnName(String trnName) {
        this.trnName = trnName;
    }

    /**
     * @return the regDate
     */
    public Date getRegDate() {
        return regDate;
    }

    /**
     * @param regDate
     *            the regDate to set
     */
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}
