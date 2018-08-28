package be.renders.homeproject.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "metingen")
public class Meting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "metingen_sequence")
	private Long id;
	
	@Column(name = "datum")
	private Timestamp datum;
	
	@Column(name = "waarde")
	private Double waarde;
	
	@Column(name = "sensorsource")
	private Long sensorsource;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getDatum() {
		return datum;
	}

	public void setDatum(Timestamp datum) {
		this.datum = datum;
	}

	public Double getWaarde() {
		return waarde;
	}

	public void setWaarde(Double waarde) {
		this.waarde = waarde;
	}

	public Long getSensorsource() {
		return sensorsource;
	}

	public void setSensorsource(Long sensorsource) {
		this.sensorsource = sensorsource;
	}
}