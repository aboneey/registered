/**
 * 
 */
package com.usta.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.usta.model.Tournament;
import com.usta.util.HibernateUtil;

/**
 * @author anil.bonigala
 *
 */
public class TournamentService {

    public Map<String, String> getTrnList() {

        Map<String, String> trns = new TreeMap<String, String>();

        // trns.put("Atlantic Club L5",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225600#&&s=1");
        // trns.put("HPTA Riverside L5",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225596#&&s=1");
        // trns.put("Greensburg L5",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225604#&&s=1");
        // trns.put("Bala Cynwyd L6",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225609#&&s=1A2");
        trns.put("Birchwood L6",
                "https://tennislink.usta.com/Tournaments/TournamentHome/Tournament.aspx?T=225668#&&s=1A2");

        trns.put("Wisehaven L4",
                "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225663#&&s=1");

        // trns.put("L3",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=225050#&&s=2");
        // trns.put("L4 2018",
        // "https://tennislink.usta.com/tournaments/TournamentHome/Tournament.aspx?T=208697#&&s=2");

        return trns;

    }

    public List<Tournament> getTournaments() {

        Transaction transaction = null;
        Date now = new Date();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Tournament> query = builder.createQuery(Tournament.class);
            Root<Tournament> root = query.from(Tournament.class);
            query.select(root)
                    .where(builder.greaterThanOrEqualTo(root.get("regEndDate"), new java.sql.Date(now.getTime())));
            Query<Tournament> q = session.createQuery(query);
            List<Tournament> ranks = q.getResultList();
            transaction.commit();
            return ranks;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return new ArrayList<Tournament>();
    }

}
