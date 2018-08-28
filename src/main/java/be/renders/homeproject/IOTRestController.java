package be.renders.homeproject;

import java.util.Date;
import java.util.List;

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
    
	
    @RequestMapping(value = "/piechart", method = RequestMethod.GET)
	public String drawPieChart(ModelMap model)
	{
		Slice s1 = Slice.newSlice(15, Color.newColor("CACACA"), "Mac", "Mac");
		Slice s2 = Slice.newSlice(50, Color.newColor("DF7417"), "Window",
				"Window");
		Slice s3 = Slice.newSlice(25, Color.newColor("951800"), "Linux",
				"Linux");
		Slice s4 = Slice.newSlice(10, Color.newColor("01A1DB"), "Others",
				"Others");

		PieChart pieChart = GCharts.newPieChart(s1, s2, s3, s4);
		pieChart.setTitle("Google Pie Chart", Color.BLACK, 15);
		pieChart.setSize(720, 360);
		pieChart.setThreeD(true);

		model.addAttribute("pieUrl", pieChart.toURLString());

		return "display";
	}
}