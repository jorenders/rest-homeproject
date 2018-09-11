package be.renders.homeproject;

public enum ResponseCode {
	OK(0),
	SENSOR_NIET_GECONFIGUREERD(1),
	SENSOR_NIET_KUNNEN_OPSLAAN(3),
	MEETWAARDE_FOUTIEF(2),
	CONFIGURATIE_FOUTIEF(3),
	SENSOR_WAARDE_FOUTIEF(4),
	CONFIGURATIE_VERWIJDEREN_MEER_DAN_EEN_RESULTAAT(5),
	CONFIGURATIE_VERWIJDEREN_GEEN_RESULTAAT(6),
	KAN_LOGGING_NIET_WEGSCHRIJVEN(7);
	
	private int code;

	private ResponseCode(int code) {
		this.code = code;
	}
}
