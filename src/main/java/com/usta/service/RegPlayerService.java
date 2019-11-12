/**
 * 
 */
package com.usta.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Query;
import org.hibernate.Session;

import com.usta.model.PlayerRank;
import com.usta.model.RankGenDetails;
import com.usta.model.RegisteredPlayer;
import com.usta.util.HibernateUtil;

/**
 * @author anil.bonigala
 *
 */
public class RegPlayerService {


	public boolean saveRegisteredPlayers(String trnName, List<String> players, int trnId) {

        // opens a new session from the session factory
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        long time = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(time);
        boolean newPlayerRegsitered = false;
        for (Iterator<String> iterator = players.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            RegisteredPlayer regPlayer = new RegisteredPlayer();
            regPlayer.setPlayerName(name);
            regPlayer.setTrnName(trnName);
            regPlayer.setTrnId(trnId);
            regPlayer.setRegDate(date);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<RegisteredPlayer> query = builder.createQuery(RegisteredPlayer.class);
            Root<RegisteredPlayer> root = query.from(RegisteredPlayer.class);
            query.select(root).where(builder.equal(root.get("playerName"), name),
            		builder.equal(root.get("trnId"), trnId));
                    //builder.equal(root.get("trnName"), trnName));
            Query<RegisteredPlayer> q = session.createQuery(query);
            List<RegisteredPlayer> playerNames = q.getResultList();

            if (playerNames.size() < 1) {
            	newPlayerRegsitered = true;
            	session.persist(regPlayer);
            }
        }
        // commits the transaction and closes the session
        session.getTransaction().commit();
        session.close();
        return newPlayerRegsitered;
    }

	public Map <Integer, String> getTournamentRanks(List<String> players, int ageBracket) {
		
		Map<String, Integer> currentRanks =  getPlayerCurrentRanks (ageBracket);
		Map< Integer, String> trnRanks = new TreeMap<Integer, String>();
		int rankInc = 1000;

		String keyPlayer = "Bonigala, Aryan";
        Integer aRank = currentRanks.get(keyPlayer);

        boolean playerExists = false;

		
		for (Iterator iterator = players.iterator(); iterator.hasNext();) {
			
			String playerName = (String) iterator.next();
			Integer rank = currentRanks.get(playerName);
			if (rank == null) rank = rankInc + 1;
			trnRanks.put(rank, playerName);
			rankInc ++;
			if (playerName.equalsIgnoreCase(keyPlayer)) {
				playerExists = true;
			}
			
		}
		if (!playerExists) 
			trnRanks.put(aRank, keyPlayer);

		return trnRanks;
	}
	
	public Map<String, Integer> getPlayerCurrentRanks(int ageBracket) {
		
		RankGenDetails rgd = this.getPlayerCurrentGenDate(ageBracket);
		
        // opens a new session from the session factory
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PlayerRank> query = builder.createQuery(PlayerRank.class);

        Root<PlayerRank> root = query.from(PlayerRank.class);
        query.select(root).where(builder.equal(root.get("generatedDate"), rgd.getGeneratedDate()), builder.equal(root.get("version"), ageBracket));
        
        Query<PlayerRank> q = session.createQuery(query);
        List<PlayerRank> playerRanks = q.getResultList();

        // commits the transaction and closes the session
        session.getTransaction().commit();
        session.close();

        
        Map<String, Integer> ranks = playerRanks.stream().collect(
                Collectors.toMap(PlayerRank::getName, PlayerRank::getPlayerRank));
		return ranks;
	}

	
	
	public RankGenDetails getPlayerCurrentGenDate(int ageBracket) {
		
		
        // opens a new session from the session factory
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<RankGenDetails> query = builder.createQuery(RankGenDetails.class);

        Root<RankGenDetails> root = query.from(RankGenDetails.class);
        query.select(root).where(builder.equal(root.get("ageBracket"), ageBracket));
        query.orderBy(builder.desc(root.get("generatedDate")));
        
        
        Query<RankGenDetails> q = session.createQuery(query);
        q.setFirstResult(0);
        q.setMaxResults(1); 
        List<RankGenDetails> rankGenDetails = q.getResultList();
        session.getTransaction().commit();
        session.close();

        if (rankGenDetails!=null & !rankGenDetails.isEmpty()) {
        	return rankGenDetails.get(0);
        } else {
        	return null;
        }

	}      	
        	
        
}
