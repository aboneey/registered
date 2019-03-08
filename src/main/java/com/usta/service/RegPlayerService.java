/**
 * 
 */
package com.usta.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;

import com.usta.model.RegisteredPlayer;
import com.usta.util.HibernateUtil;

/**
 * @author anil.bonigala
 *
 */
public class RegPlayerService {

    public void saveRegisteredPlayers(String trnName, List<String> players) {

        // opens a new session from the session factory
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);

        for (Iterator<String> iterator = players.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            RegisteredPlayer regPlayer = new RegisteredPlayer();
            regPlayer.setPlayerName(name);
            regPlayer.setTrnName(trnName);
            regPlayer.setRegDate(date);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<RegisteredPlayer> query = builder.createQuery(RegisteredPlayer.class);
            Root<RegisteredPlayer> root = query.from(RegisteredPlayer.class);
            query.select(root).where(builder.equal(root.get("playerName"), name),
                    builder.equal(root.get("trnName"), trnName));
            Query<RegisteredPlayer> q = session.createQuery(query);
            List<RegisteredPlayer> playerNames = q.getResultList();

            if (playerNames.size() < 1) {
                session.persist(regPlayer);
            }
        }
        // commits the transaction and closes the session
        session.getTransaction().commit();
        session.close();
    }

}
