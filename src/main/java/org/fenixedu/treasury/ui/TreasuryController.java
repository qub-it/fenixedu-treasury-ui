package org.fenixedu.treasury.ui;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import java.util.List;
import java.util.ArrayList;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.fenixedu.bennu.spring.portal.BennuSpringController;


@RequestMapping("/treasury")
@SpringApplication(group = "logged", path = "treasury", title = "title.treasury")
@SpringFunctionality(app = TreasuryController.class, title = "title.treasury")
public class TreasuryController {
	
//	@RequestMapping
//	public String home(Model model) {
//		//this is the default destination for handling request to the root of the Module 
//      //put here the default functionality destination of your Module
//		return "redirect:<PUT_HERE_THE_DEFAULT_CONTROLLER_MAPPING>";
//	}
	
}
