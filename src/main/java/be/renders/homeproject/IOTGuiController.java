package be.renders.homeproject;

import be.renders.homeproject.repository.MetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IOTGuiController {
	@Autowired
    MetingRepository metingRepository;
	
	@GetMapping("/grafiekTemperatuur")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        
        //List<Meting> metingen = metingRepository.getMeting(new Date().getTime());
        //model.addAttribute("metingen", metingen);
        
        Integer intzeroto8 = 3;
        Integer int8to12 = 2;
        Integer int12to16 = 1;
        Integer int16to20 = 3;
        Integer int20to24 = 20;
        
        model.addAttribute("intzeroto8", intzeroto8);
        model.addAttribute("int8to12", int8to12);
        model.addAttribute("int12to16", int12to16);
        model.addAttribute("int16to20", int16to20);
        model.addAttribute("int20to24", int20to24);
        
        StringBuilder linedata = new StringBuilder();

  
        linedata.append("[[12,1,2],[13,5,9],[14,4,3],");
        linedata.append("[15,1,2],[16,5,9],[17,4,3]]");

        
        model.addAttribute("linedata", linedata.toString());
        
        return "grafiekTemperatuur";
    }
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String landingPage(ModelMap model)
	{
		return "index";
	}
}
