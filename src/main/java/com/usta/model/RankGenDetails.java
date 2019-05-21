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
@Table(name = "generation_details")
public class RankGenDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "generated_date")
    private Date generatedDate;

    private int version;

    @Column(name = "age_bracket")
    private int ageBracket;

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

    /**
     * @return the ageBracket
     */
    public int getAgeBracket() {
        return ageBracket;
    }

    /**
     * @param ageBracket
     *            the ageBracket to set
     */
    public void setAgeBracket(int ageBracket) {
        this.ageBracket = ageBracket;
    }
}
