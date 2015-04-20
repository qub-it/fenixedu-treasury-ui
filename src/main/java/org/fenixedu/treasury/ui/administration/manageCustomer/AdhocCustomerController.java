package org.fenixedu.treasury.ui.administration.manageCustomer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import org.joda.time.DateTime;
import java.util.stream.Collectors;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.stereotype.Component;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.fenixedu.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.domain.AdhocCustomer;

//@Component("org.fenixedu.treasury.ui.administration.manageCustomer") <-- Use for duplicate controller name disambiguation
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.manageCustomer",accessGroup = "anyone")// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
//or
@BennuSpringController(value = TreasuryController.class)
@RequestMapping("/treasury/administration/managecustomer/adhoccustomer")
public class AdhocCustomerController extends TreasuryBaseController {

	//

	@RequestMapping
	public String home(Model model) {
		// this is the default behaviour, for handling in a Spring Functionality
		return "forward:/treasury/administration/managecustomer/adhoccustomer/";
	}

	private AdhocCustomer getAdhocCustomer(Model model) {
		return (AdhocCustomer) model.asMap().get("adhocCustomer");
	}

	private void setAdhocCustomer(AdhocCustomer adhocCustomer, Model model) {
		model.addAttribute("adhocCustomer", adhocCustomer);
	}

	@Atomic
	public void deleteAdhocCustomer(AdhocCustomer adhocCustomer) {
		// CHANGE_ME: Do the processing for deleting the adhocCustomer
		// Do not catch any exception here

		adhocCustomer.delete();
	}

	//
	@RequestMapping(value = "/")
	public String search(Model model) {
		List<AdhocCustomer> searchadhoccustomerResultsDataSet = filterSearchAdhocCustomer();

		// add the results dataSet to the model
		model.addAttribute("searchadhoccustomerResultsDataSet",
				searchadhoccustomerResultsDataSet);
		return "treasury/administration/managecustomer/adhoccustomer/search";
	}

	private List<AdhocCustomer> getSearchUniverseSearchAdhocCustomerDataSet() {
		//
		// The initialization of the result list must be done here
		//
		//
		return new ArrayList<AdhocCustomer>(AdhocCustomer.findAll().collect(
				Collectors.toList())); // CHANGE_ME
		// return new ArrayList<AdhocCustomer>();
	}

	private List<AdhocCustomer> filterSearchAdhocCustomer() {

		return getSearchUniverseSearchAdhocCustomerDataSet().stream().collect(
				Collectors.toList());
	}

	@RequestMapping(value = "/search/deleteMultiple")
	public String processSearchToDeleteMultiple(
			@RequestParam("adhocCustomers") List<AdhocCustomer> adhocCustomers,
			Model model, RedirectAttributes redirectAttributes) {

		// CHANGE_ME Insert code here for processing deleteMultiple
		// If you selected multiple exists you must choose which one to use
		// below
		return redirect(
				"/treasury/administration/managecustomer/adhoccustomer/",
				model, redirectAttributes);
	}

	@RequestMapping(value = "/search/view/{oid}")
	public String processSearchToViewAction(
			@PathVariable("oid") AdhocCustomer adhocCustomer, Model model,
			RedirectAttributes redirectAttributes) {

		// CHANGE_ME Insert code here for processing viewAction
		// If you selected multiple exists you must choose which one to use
		// below
		return redirect(
				"/treasury/administration/managecustomer/adhoccustomer/read"
						+ "/" + adhocCustomer.getExternalId(), model,
				redirectAttributes);
	}

	@RequestMapping(value = "/search/delete/{oid}", method = RequestMethod.POST)
	public String processSearchToDeleteAction(
			@PathVariable("oid") AdhocCustomer adhocCustomer, Model model,
			RedirectAttributes redirectAttributes) {
		setAdhocCustomer(adhocCustomer, model);
		try {
			// call the Atomic delete function
			deleteAdhocCustomer(adhocCustomer);

			addInfoMessage("Sucess deleting AdhocCustomer ...", model);
			return redirect(
					"/treasury/administration/managecustomer/adhoccustomer/",
					model, redirectAttributes);
		} catch (DomainException ex) {
			// Add error messages to the list
			addErrorMessage(
					"Error deleting the AdhocCustomer due to "
							+ ex.getLocalizedMessage(), model);
		}

		// The default mapping is the same Search screen
		return "treasury/administration/managecustomer/adhoccustomer/search";
	}

	//
	@RequestMapping(value = "/read/{oid}")
	public String read(@PathVariable("oid") AdhocCustomer adhocCustomer,
			Model model) {
		setAdhocCustomer(adhocCustomer, model);
		return "treasury/administration/managecustomer/adhoccustomer/read";
	}

	//
	@RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
	public String delete(@PathVariable("oid") AdhocCustomer adhocCustomer,
			Model model, RedirectAttributes redirectAttributes) {

		setAdhocCustomer(adhocCustomer, model);
		try {
			// call the Atomic delete function
			deleteAdhocCustomer(adhocCustomer);

			addInfoMessage("Sucess deleting AdhocCustomer ...", model);
			return redirect(
					"/treasury/administration/managecustomer/adhoccustomer/",
					model, redirectAttributes);
		} catch (DomainException ex) {
			// Add error messages to the list
			addErrorMessage(
					"Error deleting the AdhocCustomer due to "
							+ ex.getLocalizedMessage(), model);
		}

		// The default mapping is the same Read View
		return "treasury/administration/managecustomer/adhoccustomer/read/"
				+ getAdhocCustomer(model).getExternalId();
	}

	//
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		return "treasury/administration/managecustomer/adhoccustomer/create";
	}

	//
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(
			@RequestParam(value = "code", required = false) java.lang.String code,
			@RequestParam(value = "fiscalnumber", required = false) java.lang.String fiscalNumber,
			@RequestParam(value = "name", required = false) java.lang.String name,
			@RequestParam(value = "address", required = false) java.lang.String address,
			@RequestParam(value = "districtsubdivision", required = false) java.lang.String districtSubdivision,
			@RequestParam(value = "zipcode", required = false) java.lang.String zipCode,
			@RequestParam(value = "countrycode", required = false) java.lang.String countryCode,
			Model model, RedirectAttributes redirectAttributes) {
		/*
		 * Creation Logic
		 */

		try {

			AdhocCustomer adhocCustomer = createAdhocCustomer(code,
					fiscalNumber, name, address, districtSubdivision, zipCode,
					countryCode);

			// Success Validation
			// Add the bean to be used in the View
			model.addAttribute("adhocCustomer", adhocCustomer);
			return redirect(
					"/treasury/administration/managecustomer/adhoccustomer/read/"
							+ getAdhocCustomer(model).getExternalId(), model,
					redirectAttributes);
		} catch (DomainException de) {

			// @formatter: off
			/*
			 * If there is any error in validation
			 * 
			 * Add a error / warning message
			 * 
			 * addErrorMessage(" Error creating due to " +
			 * de.getLocalizedMessage(),model);
			 * addWarningMessage(" Warning creating due to "+
			 * ex.getLocalizedMessage(),model);
			 */
			// @formatter: on

			addErrorMessage(
					" Error creating due to " + de.getLocalizedMessage(), model);
			return create(model);
		}
	}

	@Atomic
	public AdhocCustomer createAdhocCustomer(java.lang.String code,
			java.lang.String fiscalNumber, java.lang.String name,
			java.lang.String address, java.lang.String districtSubdivision,
			java.lang.String zipCode, java.lang.String countryCode) {

		// @formatter: off

		/*
		 * Modify the creation code here if you do not want to create the object
		 * with the default constructor and use the setter for each field
		 */

		// CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
		// AdhocCustomer adhocCustomer = adhocCustomer.create(fields_to_create);

		// Instead, use individual SETTERS and validate "CheckRules" in the end
		// @formatter: on

		AdhocCustomer adhocCustomer = AdhocCustomer.create(code, fiscalNumber,
				name, address, districtSubdivision, zipCode, countryCode);

		return adhocCustomer;
	}

	//
	@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
	public String update(@PathVariable("oid") AdhocCustomer adhocCustomer,
			Model model) {
		setAdhocCustomer(adhocCustomer, model);
		return "treasury/administration/managecustomer/adhoccustomer/update";
	}

	//
	@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
	public String update(
			@PathVariable("oid") AdhocCustomer adhocCustomer,
			@RequestParam(value = "code", required = false) java.lang.String code,
			@RequestParam(value = "fiscalnumber", required = false) java.lang.String fiscalNumber,
			@RequestParam(value = "name", required = false) java.lang.String name,
			@RequestParam(value = "address", required = false) java.lang.String address,
			@RequestParam(value = "districtsubdivision", required = false) java.lang.String districtSubdivision,
			@RequestParam(value = "zipcode", required = false) java.lang.String zipCode,
			@RequestParam(value = "countrycode", required = false) java.lang.String countryCode,
			Model model, RedirectAttributes redirectAttributes) {

		setAdhocCustomer(adhocCustomer, model);

		try {
			/*
			 * UpdateLogic here
			 */

			updateAdhocCustomer(code, fiscalNumber, name, address,
					districtSubdivision, zipCode, countryCode, model);

			/* Succes Update */

			return redirect(
					"/treasury/administration/managecustomer/adhoccustomer/read/"
							+ getAdhocCustomer(model).getExternalId(), model,
					redirectAttributes);
		} catch (DomainException de) {
			// @formatter: off

			/*
			 * If there is any error in validation
			 * 
			 * Add a error / warning message
			 * 
			 * addErrorMessage(" Error updating due to " +
			 * de.getLocalizedMessage(),model);
			 * addWarningMessage(" Warning updating due to " +
			 * de.getLocalizedMessage(),model);
			 */
			// @formatter: on

			addErrorMessage(
					" Error updating due to " + de.getLocalizedMessage(), model);
			return update(adhocCustomer, model);

		}
	}

	@Atomic
	public void updateAdhocCustomer(java.lang.String code,
			java.lang.String fiscalNumber, java.lang.String name,
			java.lang.String address, java.lang.String districtSubdivision,
			java.lang.String zipCode, java.lang.String countryCode, Model model) {

		// @formatter: off
		/*
		 * Modify the update code here if you do not want to update the object
		 * with the default setter for each field
		 */

		// CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
		// getAdhocCustomer(model).edit(fields_to_edit);

		// Instead, use individual SETTERS and validate "CheckRules" in the end
		// @formatter: on
		getAdhocCustomer(model).edit(code, fiscalNumber, name, address,
				districtSubdivision, zipCode, countryCode);
	}

}
