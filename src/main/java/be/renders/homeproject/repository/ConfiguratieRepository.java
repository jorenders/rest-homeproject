package be.renders.homeproject.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import be.renders.homeproject.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import be.renders.homeproject.repository.domain.Configuratie;

@Repository
public class ConfiguratieRepository {
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public ResponseCode registreerConfiguratie(String naam, String value) {
		Configuratie configuratie = new Configuratie();
		configuratie.setNaam(naam);
		configuratie.setValue(value);

		try {
			em.persist(configuratie);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseCode.CONFIGURATIE_FOUTIEF;
		}
		
		em.flush();
		
		return ResponseCode.OK;
	}
	
	public List<Configuratie> haalOverzichtConfiguratieOp() {
		Query q = em.createQuery("SELECT configuratie from Configuratie configuratie");
		@SuppressWarnings("unchecked")
		List<Configuratie> results = (List<Configuratie>) q.getResultList();
		return results;
	}

	public List<Configuratie> zoekSpecifiekeConfiguratieOp(String naam, String omschrijving) {
		Query q = em.createQuery("SELECT configuratie from Configuratie configuratie "
			    + "where upper(configuratie.naam) like :naam and upper(configuratie.value) like :omschrijving");
			q.setParameter("naam", "%" + naam.toUpperCase() + "%");
			q.setParameter("omschrijving", "%" + omschrijving.toUpperCase() + "%");
		@SuppressWarnings("unchecked")
		List<Configuratie> results = (List<Configuratie>) q.getResultList();
		return results;
	}

	@Transactional
	public ResponseCode verwijderSpecifiekeConfiguratieOp(String naam) {
		Query q = em.createQuery("SELECT configuratie from Configuratie configuratie "
			    + "where upper(configuratie.naam) like :naam");
			q.setParameter("naam", "%" + naam.toUpperCase() + "%");
		@SuppressWarnings("unchecked")
		List<Configuratie> results = (List<Configuratie>) q.getResultList();
		if (results.size() > 1) {
			return ResponseCode.CONFIGURATIE_VERWIJDEREN_MEER_DAN_EEN_RESULTAAT;
		}
		
		if (results.size() == 0) {
			return ResponseCode.CONFIGURATIE_VERWIJDEREN_GEEN_RESULTAAT;
		}
		
		em.remove(results.get(0));
		return ResponseCode.OK;
	}
}