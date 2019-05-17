package be.renders.homeproject.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import be.renders.homeproject.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import be.renders.homeproject.repository.domain.Module;

@Repository
public class AuthenticatieRepository {
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@PersistenceContext
	private EntityManager em;	
	
	@Transactional
	public ResponseCode checkModule(String macAdres) {
		Module module = new Module();
		Query q = em.createQuery("SELECT module from Module module where module.macAdres = :macAdres");
		q.setParameter("macAdres", macAdres);
		int aantal = q.getResultList().size();
		if (aantal == 1) {
			module = (Module) q.getSingleResult();
			return ResponseCode.MODULE_GEKEND;
		} else if (aantal == 0) {
			return ResponseCode.MODULE_NIET_GEKEND;
		}
		return ResponseCode.MODULE_DUBBEL_GEREGISTREERD;
	}
	
	@Transactional
	public ResponseCode registreerNieuweModule(String macAdres) {
		Module module = new Module();
		module.setMacAdres(macAdres);
		
		try {
			em.persist(module);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseCode.MODULE_KAN_NIET_WEGGESCHREVEN_WORDEN;
		}
		
		em.flush();		
		return ResponseCode.OK;
	}
}