package be.renders.homeproject;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import be.renders.homeproject.domain.Meting;

@Repository
public class HomeProjectRepository {

	private static final int MS_IN_ONE_MINUUT = 60000;

	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public ResponseCode registreerMeting(long sensorId, Double metingWaarde) {
		Timestamp vandaag = new Timestamp(System.currentTimeMillis());
		
		Meting meting = new Meting();
		meting.setDatum(vandaag);
		meting.setWaarde(metingWaarde);
		meting.setSensorsource(sensorId);

		try {
			em.persist(meting);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseCode.MEETWAARDE_FOUTIEF;
		}
		
		em.flush();
		
		return ResponseCode.OK;
		
		

/*		int returnCode = jdbcTemplate.update(
			    "INSERT INTO Metingen (datum, waarde, sensorSource) VALUES (?, ?, ?)",
			    vandaag, sensorValue, sensorId);
		if (returnCode == 1) {
			return ResponseCode.OK; 
		} else {
			return ResponseCode.MEETWAARDE_FOUTIEF;
		}	*/    		
	}
	
	public ResponseCode storeSensorInfo(String sensorCode, String sensorOmschrijving) {
		return null;
		/*
		int returnCode = jdbcTemplate.update(
			    "INSERT INTO Sensoren (sensorCode ,sensorOmschrijving) VALUES (?, ?)",
			    sensorCode, sensorOmschrijving);
		if (returnCode == 1) {
			return ResponseCode.OK; 
		} else {
			return ResponseCode.SENSOR_NIET_KUNNEN_OPSLAAN;
		}*/	    		
	}

	public List<Meting> getMeting(long miliseconds) {
		Timestamp now = new Timestamp(miliseconds);
		Timestamp oneMinuteAgo = new Timestamp(now.getTime() - (1 * MS_IN_ONE_MINUUT));

		Query q = em.createQuery("SELECT meting from Meting meting " 
		    + "where meting.datum < :now and meting.datum > :oneMinuteAgo");
		q.setParameter("now", now); 
		q.setParameter("oneMinuteAgo", oneMinuteAgo);

		@SuppressWarnings("unchecked")
		List<Meting> results = (List<Meting>) q.getResultList();
		return results;
	}
}