package be.renders.homeproject;

import be.renders.homeproject.repository.AuthenticatieRepository;
import be.renders.homeproject.repository.ConfiguratieRepository;
import be.renders.homeproject.repository.HomeProjectRepository;
import be.renders.homeproject.repository.MetingRepository;
import be.renders.homeproject.repository.domain.Configuratie;
import be.renders.homeproject.repository.domain.Meting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class IOTRestControllerTests {

	@Spy
	private MetingRepository metingRepository;

	@Spy
	private AuthenticatieRepository authenticatieRepository;

	@Spy
	private ConfiguratieRepository configuratieRepository;

	@InjectMocks
	private IOTRestController iotRestController;

	@Before
	public void initMocks(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void registreerMetingGeeftCorrecteResponsTerug() {
		Long sensorId = 1L;
		Double metingWaarde = 2.22;
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.OK).when(metingRepository).registreerMeting(sensorId, metingWaarde);
		doReturn(ResponseCode.OK).when(authenticatieRepository).checkModule(macAdress);

		Respons respons = iotRestController.registreerMeting(sensorId, metingWaarde, macAdress);
		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void registreerMetingGeeftSensorWaardeFoutiefResponsTerugAlsSensorNullIs() {
		Long sensorId = null;
		Double metingWaarde = 2.22;
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.OK).when(authenticatieRepository).checkModule(macAdress);

		Respons respons = iotRestController.registreerMeting(sensorId, metingWaarde, macAdress);
		assertEquals(ResponseCode.SENSOR_WAARDE_FOUTIEF, respons.getResponsCode());
		assertEquals("Sensorwaarde is ongekend (null)", respons.getResponsString());
	}

	@Test
	public void registreerMetingGeeftMeetWaardeFoutiefResponsTerugAlsMeetWaardeNullIs() {
		Long sensorId = 1L;
		Double metingWaarde = null;
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.OK).when(authenticatieRepository).checkModule(macAdress);

		Respons respons = iotRestController.registreerMeting(sensorId, metingWaarde, macAdress);
		assertEquals(ResponseCode.MEETWAARDE_FOUTIEF, respons.getResponsCode());
		assertEquals("Meetwaarde is ongekend (null)", respons.getResponsString());
	}

	@Test
	public void registreerMetingGeeftFoutAlsMacAdressNietGekendIs() {
		Long sensorId = null;
		Double metingWaarde = 2.22;
		String macAdress = "0D-73-ED-0A-27-88";
		doReturn(ResponseCode.MODULE_NIET_GEKEND).when(authenticatieRepository).checkModule(macAdress);

		Respons respons = iotRestController.registreerMeting(sensorId, metingWaarde, macAdress);
		assertEquals(ResponseCode.MODULE_NIET_GEKEND, respons.getResponsCode());
		assertEquals("Module is ongekend (null), fout bij authenticatie", respons.getResponsString());
	}

	@Test
	public void registreerApparaatGeeftOKTerugMetCorrectMACAdress() {
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.MODULE_NIET_GEKEND).when(authenticatieRepository).checkModule(macAdress);
		doReturn(ResponseCode.OK).when(authenticatieRepository).registreerNieuweModule(macAdress);

		Respons respons = iotRestController.registreerApparaat(macAdress);

		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void registreerApparaatGeeftFoutTerugMetCorrectMACAdressWegensProblemen() {
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.MODULE_NIET_GEKEND).when(authenticatieRepository).checkModule(macAdress);
		doReturn(ResponseCode.CONFIGURATIE_FOUTIEF).when(authenticatieRepository).registreerNieuweModule(macAdress);

		Respons respons = iotRestController.registreerApparaat(macAdress);

		assertEquals(ResponseCode.MODULE_KAN_NIET_WEGGESCHREVEN_WORDEN, respons.getResponsCode());
		assertEquals("failed", respons.getResponsString());
	}

	@Test
	public void registreerApparaatGeeftErrorTerugMetLeegMACAdress() {
		String macAdress = null;

		Respons respons = iotRestController.registreerApparaat(macAdress);

		assertEquals(ResponseCode.MACADRES_FOUTIEF, respons.getResponsCode());
		assertEquals("MAC Adres is ongekend (null)", respons.getResponsString());

	}

	@Test
	public void registreerApparaatGeeftOKTerugMetGekendMACAdress() {
		String macAdress = "0D-73-ED-0A-27-44";
		doReturn(ResponseCode.OK).when(authenticatieRepository).checkModule(macAdress);

		Respons respons = iotRestController.registreerApparaat(macAdress);

		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void registreerConfiguratieGeeftOKTerugAlsCorrecteParametersWordenDoorgegeven() {
		String naam = "Temp Liv";
		String waarde = "Temperatuur Living";
		doReturn(ResponseCode.OK).when(configuratieRepository).registreerConfiguratie(naam.toUpperCase(), waarde.toUpperCase());

		Respons respons = iotRestController.registreerConfiguratie(naam, waarde);

		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void registreerConfiguratieGeeftNOKTerugAlsNaamOntbreekt() {
		String naam = "";
		String waarde = "Temperatuur Living";
		doReturn(ResponseCode.OK).when(configuratieRepository).registreerConfiguratie(naam.toUpperCase(), waarde.toUpperCase());

		Respons respons = iotRestController.registreerConfiguratie(naam, waarde);

		assertEquals(ResponseCode.CONFIGURATIE_FOUTIEF, respons.getResponsCode());
		assertEquals("Naam is verplicht mee te geven", respons.getResponsString());
	}

	@Test
	public void registreerConfiguratieGeeftNOKTerugAlsWaardeOntbreekt() {
		String naam = "Temp Liv";
		String waarde = "";
		doReturn(ResponseCode.OK).when(configuratieRepository).registreerConfiguratie(naam.toUpperCase(), waarde.toUpperCase());

		Respons respons = iotRestController.registreerConfiguratie(naam, waarde);

		assertEquals(ResponseCode.CONFIGURATIE_FOUTIEF, respons.getResponsCode());
		assertEquals("Initiele waarde is verplicht bij elke configuratieparameter", respons.getResponsString());
	}

	@Test
	public void haalConfiguratieOpGeeftLijstTerug() {
		List<Configuratie> configuraties = new ArrayList<Configuratie>();
		Configuratie confA = new Configuratie();
		confA.setId(1L);
		confA.setNaam("Temp Liv");
		confA.setValue("Temperatuur Living");
		configuraties.add(confA);

		doReturn(configuraties).when(configuratieRepository).haalOverzichtConfiguratieOp();

		List<Configuratie> result = iotRestController.haalConfiguratieOp();

		assertEquals(configuraties, result);
	}

	@Test
	public void zoekSpecifiekeConfiguratieOpGeeftLijstTerug() {
		String naam = "Temp Liv";
		String waarde = "Temperatuur Living";
		List<Configuratie> configuraties = new ArrayList<Configuratie>();
		Configuratie confA = new Configuratie();
		confA.setId(1L);
		confA.setNaam(naam);
		confA.setValue(waarde);
		configuraties.add(confA);

		doReturn(configuraties).when(configuratieRepository).zoekSpecifiekeConfiguratieOp(naam, waarde);

		List<Configuratie> result = iotRestController.zoekSpecifiekeConfiguratieOp(naam, waarde);

		assertEquals(configuraties, result);
		assertEquals(1, result.size());
	}

	@Test
	public void verwijderConfiguratieGeeftOKTerugIndienWaardeBestaat() {
		String naam = "Temp Liv";
		doReturn(ResponseCode.OK).when(configuratieRepository).verwijderSpecifiekeConfiguratieOp(naam);

		Respons respons = iotRestController.verwijderConfiguratie(naam);
		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void verwijderConfiguratieGeeftFoutTerugIndienWaardeNietBestaat() {
		String naam = "Temp Liv2";
		doReturn(ResponseCode.CONFIGURATIE_VERWIJDEREN_GEEN_RESULTAAT).when(configuratieRepository).verwijderSpecifiekeConfiguratieOp(naam);

		Respons respons = iotRestController.verwijderConfiguratie(naam);
		assertEquals(ResponseCode.CONFIGURATIE_VERWIJDEREN_GEEN_RESULTAAT, respons.getResponsCode());
		assertEquals("failed", respons.getResponsString());
	}

	@Test
	public void verwijderConfiguratieGeeftFoutTerugIndienWaardeMeedereKerenBestaatEnStringDusNietSpecifiekGenoegIs() {
		String naam = "Temp";
		doReturn(ResponseCode.CONFIGURATIE_VERWIJDEREN_MEER_DAN_EEN_RESULTAAT).when(configuratieRepository).verwijderSpecifiekeConfiguratieOp(naam);

		Respons respons = iotRestController.verwijderConfiguratie(naam);
		assertEquals(ResponseCode.CONFIGURATIE_VERWIJDEREN_MEER_DAN_EEN_RESULTAAT, respons.getResponsCode());
		assertEquals("failed", respons.getResponsString());
	}

	@Test
	 public void haalMetingOpGeeftResultatenTerugVanDeLaatsteMinuut() {
		Timestamp now = new Timestamp(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
		long nowLong = now.getTime();
		Meting meting1 = createMeting(0L, now, 123456L, 12.15);
		Meting meting2 = createMeting(1L, now , 123416L, 18.15);
		Meting meting3 = createMeting(2L,now , 122116L, 25.15);

		List<Meting> metingen = new ArrayList<Meting>();
		metingen.addAll(Arrays.asList(meting1, meting2, meting3));
		doReturn(metingen).when(metingRepository).getMeting(nowLong);

		List<Meting> result = iotRestController.haalMetingOp(nowLong, 0);
		assertEquals(metingen, result);
		assertEquals(3, result.size());
	}

	@Test
	public void haalMetingOpGeeftResultatenTerugVanDeLaatsteMinuutStandaard() {
		Timestamp now = new Timestamp(Instant.now().toEpochMilli());
		long nowLong = now.getTime();
		Meting meting1 = createMeting(0L, now, 123456L, 12.15);
		Meting meting2 = createMeting(1L, now , 123416L, 18.15);
		Meting meting3 = createMeting(2L,now , 122116L, 25.15);

		List<Meting> metingen = new ArrayList<Meting>();
		metingen.addAll(Arrays.asList(meting1, meting2, meting3));
		doReturn(metingen).when(metingRepository).getMeting(anyLong());

		List<Meting> result = iotRestController.haalMetingOp(0, 0);
		assertEquals(metingen, result);
		assertEquals(3, result.size());
	}

	@Test
	public void registreerLoggingGeeftOKTerugWanneerLoggingIsWeggeschreven() {
		Respons respons = iotRestController.registreerLogging("logging is de moeder van de porseleinenkast");
		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}



	private Meting createMeting(long id, Timestamp datum, Long sensorSource, Double waarde) {
		Meting result = new Meting();
		result.setId(id);
		result.setDatum(datum);
		result.setSensorsource(sensorSource);
		result.setWaarde(waarde);
		return result;
	}
}
