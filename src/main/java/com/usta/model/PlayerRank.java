/**
 * 
 */
package com.usta.model;

import java.io.Serializable;
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
@Table(name = "player_rank")
public class PlayerRank implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @SequenceGenerator(name = "playerrnk_generator", sequenceName =
    // "playerrnk_seq", allocationSize = 50)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "player_rank")
    private int playerRank;

    private String name;

    private int points;

    @Column(name = "generated_date")
    private Date generatedDate;

    private int version;

    /**
     * @return the playerRank
     */
    public int getPlayerRank() {
        return playerRank;
    }

    /**
     * @param playerRank
     *            the playerRank to set
     */
    public void setPlayerRank(int playerRank) {
        this.playerRank = playerRank;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points
     *            the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the generatedDate
     */
    public Date getGeneratedDate() {
        return generatedDate;
    }

    /**
     * @param generatedDate
     *            the generatedDate to set
     */
    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(int version) {
        this.version = version;
    }

}
