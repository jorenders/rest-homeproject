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
}
