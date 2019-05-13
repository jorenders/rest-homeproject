package be.renders.homeproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IOTRestControllerTests {

	@Spy
	private HomeProjectRepository metingRepository;

	@Spy
	private AuthenticatieRepository authenticatieRepository;

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
}
