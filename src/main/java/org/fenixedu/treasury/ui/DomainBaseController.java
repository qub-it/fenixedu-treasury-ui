package org.fenixedu.treasury.ui;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
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
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class DomainBaseController {
				private static final String ERROR_MESSAGES = "errorMessages";
   				private static final String WARNING_MESSAGES = "warningMessages";
   				private static final String INFO_MESSAGES = "infoMessages";
   
				//The HTTP Request that can be used internally in the controller
				protected @Autowired HttpServletRequest request;
				
				//The entity in the Model
				

				// The list of INFO messages that can be showed on View
				protected void addInfoMessage(String message, Model model)
				{
					((List<String>)model.asMap().get(INFO_MESSAGES)).add(message);
				}
				
				// The list of WARNING messages that can be showed on View
				protected void addWarningMessage(String message, Model model)
				{
					((List<String>)model.asMap().get(WARNING_MESSAGES)).add(message);
				}
				
				// The list of ERROR messages that can be showed on View
				protected void addErrorMessage(String message, Model model)
				{
					((List<String>)model.asMap().get(ERROR_MESSAGES)).add(message);
				}
				
				protected void clearMessages(Model model)
				{
					model.addAttribute(INFO_MESSAGES, new ArrayList<String>());
					model.addAttribute(WARNING_MESSAGES, new ArrayList<String>());
					model.addAttribute(ERROR_MESSAGES, new ArrayList<String>());
				}

 				@ModelAttribute
   				protected void addModelProperties(Model model) {
       				model.addAttribute(INFO_MESSAGES, new ArrayList<String>());
       				model.addAttribute(WARNING_MESSAGES, new ArrayList<String>());
       				model.addAttribute(ERROR_MESSAGES, new ArrayList<String>());

       				String infoMessages = request.getParameter(INFO_MESSAGES);
       				if (infoMessages != null) {
           				addInfoMessage(infoMessages, model);
       				}
       				String warningMessages = request.getParameter(WARNING_MESSAGES);
       				if (warningMessages != null) {
           				addWarningMessage(warningMessages, model);
       				}
		       		String errorMessages = request.getParameter(ERROR_MESSAGES);
       				if (errorMessages != null) {
           				addErrorMessage(errorMessages, model);
       				}
					//Add here more attributes to the Model
					//model.addAttribute(<attr1Key>, <attr1Value>);
					//....
			   }				
				
				
		}	
