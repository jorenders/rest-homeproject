package be.renders.homeproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IOTRestControllerTests {

	@Spy
	private MetingRepository metingRepository;

	@InjectMocks
	private IOTRestController iotRestController;

	@Before
	public void initMocks(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void registreerMetingGeeftCorrecteResponsTerug() {
		Mockito.doReturn(ResponseCode.OK).when(metingRepository).registreerMeting(1L, 2.22);
		Respons respons = iotRestController.registreerMeting(1L, 2.22);
		assertEquals(ResponseCode.OK, respons.getResponsCode());
		assertEquals("succes", respons.getResponsString());
	}

	@Test
	public void registreerMetingGeeftSensorWaardeFoutiefResponsTerugAlsSensorNullIs() {
		Respons respons = iotRestController.registreerMeting(null, 2.22);
		assertEquals(ResponseCode.SENSOR_WAARDE_FOUTIEF, respons.getResponsCode());
		assertEquals("Sensorwaarde is ongekend (null)", respons.getResponsString());
	}
}
