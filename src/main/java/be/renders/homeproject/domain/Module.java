package be.renders.homeproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MODULE")
public class Module {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MODULEID")
	private Long id;
	
	@Column(name = "MACADRES")
	private String macAdres;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMacAdres() {
		return macAdres;
	}

	public void setMacAdres(String macAdres) {
		this.macAdres = macAdres;
	}
}