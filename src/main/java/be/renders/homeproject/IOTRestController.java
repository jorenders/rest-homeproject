package be.renders.homeproject;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;

import be.renders.homeproject.domain.Configuratie;
import be.renders.homeproject.domain.Meting;
import io.swagger.annotations.ApiOperation;

@RestController
public class IOTRestController {
	
	@Autowired
	MetingRepository metingRepository;

	@Autowired
	ConfiguratieRepository configuratieRepository;

	Logger logger = LoggerFactory.getLogger(IOTRestController.class);
	
	@ApiOperation(value = "Registreer een meting voor een bepaalde sensor")
    @RequestMapping(value = "/registreerMeting", produces = "application/json", method = RequestMethod.GET)
    public Respons registreerMeting(@RequestParam(value="sensor", defaultValue = "") Long sensor,
    						@RequestParam(value="metingWaarde", defaultValue = "") Double metingWaarde) {    	
    	if (sensor == null) {
    		return new Respons(ResponseCode.SENSOR_WAARDE_FOUTIEF, "Sensorwaarde is ongekend (null)");
    	} else if (metingWaarde == null) {
    		return new Respons(ResponseCode.MEETWAARDE_FOUTIEF, "Meetwaarde is ongekend (null)");
    	}
    	
    	ResponseCode responseCode = metingRepository.registreerMeting(sensor, metingWaarde);
    	Respons respons = new Respons(responseCode);
        return respons;
    }
    
	@ApiOperation(value = "Registreer een nieuwe cofiguratie parameter")
    @RequestMapping(value = "/registreerConfiguratie", produces = "application/json", method = RequestMethod.GET)
    public Respons registreerConfiguratie(
    						@RequestParam(value="naam", defaultValue="") String naam,
    						@RequestParam(value="waarde", defaultValue="") String waarde) {
    	if (naam == "") {
    		return new Respons(ResponseCode.CONFIGURATIE_FOUTIEF, "Naam is verplicht mee te geven");
    	} else if (waarde == "") {
    		return new Respons(ResponseCode.CONFIGURATIE_FOUTIEF, "Initiele waarde is verplicht bij elke configuratieparameter");
    	}
    	
    	ResponseCode responseCode = configuratieRepository.registreerConfiguratie(naam.toUpperCase(), waarde.toUpperCase());
    	Respons respons = new Respons(responseCode);
        return respons;
    }
    
	@ApiOperation(value = "Haal alle configuratieparameters op")
    @RequestMapping(value = "/haalConfiguratieOp", produces = "application/json", method = RequestMethod.GET)
    public List<Configuratie> haalConfiguratieOp() {
    	return  configuratieRepository.haalOverzichtConfiguratieOp();
    }
    
	@ApiOperation(value = "Zoek een specifieke configuratie op")
    @RequestMapping(value = "/zoekSpecifiekeConfiguratieOp", produces = "application/json", method = RequestMethod.GET)
    public List<Configuratie> zoekSpecifiekeConfiguratieOp(@RequestParam(value="naam", defaultValue="") String naam, @RequestParam(value="omschrijving", defaultValue="") String omschrijving) {
    	return  configuratieRepository.zoekSpecifiekeConfiguratieOp(naam, omschrijving);
    }
	
	@ApiOperation(value = "Verwijder een specifieke configuratie")
    @RequestMapping(value = "/verwijderSpecifiekeConfiguratie", produces = "application/json", method = RequestMethod.GET)
    public Respons verwijderConfiguratie(@RequestParam(value="naam", defaultValue="") String naam) {
		ResponseCode responseCode = configuratieRepository.verwijderSpecifiekeConfiguratieOp(naam);
    	Respons respons = new Respons(responseCode);
        return respons;
    }

	@ApiOperation(value = "Haal een specifieke meting op (zonder argumenten de laatste meting < 1 minuut")
    @RequestMapping(value = "/haalMetingOp", produces = "application/json", method = RequestMethod.GET)
    public List<Meting> haalMetingOp(@RequestParam(value="timestamp", defaultValue = "0") long miliseconds, 
    		@RequestParam(value="sensor", defaultValue="0") long sensor) {
    	if (miliseconds == 0) {
    		miliseconds = new Date().getTime();
    	}
    	List<Meting> metingen = metingRepository.getMeting(miliseconds);
        return metingen;
    }

	@ApiOperation(value = "Sla logging op van sensor")
	@RequestMapping(value = "/registreerLogging", produces = "application/json", method = RequestMethod.GET)
	public Respons registreerLogging(@RequestParam(value="logging", defaultValue="") String logging) {
		Respons respons = new Respons(ResponseCode.KAN_LOGGING_NIET_WEGSCHRIJVEN);
		logger.info(logging);
		respons.setResponsCode(ResponseCode.OK);
		respons.setResponsString("");
		return respons;
	}
}