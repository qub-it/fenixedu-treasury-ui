/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com, anil.mamede@qub-it.com
 *
 * 
 * This file is part of FenixEdu Treasury.
 *
 * FenixEdu Treasury is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Treasury is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Treasury.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.treasury.ui.document.manageExemption;

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
import org.fenixedu.treasury.domain.TreasuryExemption;

//@Component("org.fenixedu.treasury.ui.document.manageExemption") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.document.manageExemption", accessGroup = "logged")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
// @BennuSpringController(value=TreasuryController.class)
@RequestMapping("/treasury/document/manageexemption/treasuryexemption")
public class TreasuryExemptionController extends TreasuryBaseController {

	//

	@RequestMapping
	public String home(Model model) {
		// this is the default behaviour, for handling in a Spring Functionality
		return "forward:/treasury/document/manageexemption/treasuryexemption/";
	}

	private TreasuryExemption getTreasuryExemption(Model model) {
		return (TreasuryExemption) model.asMap().get("treasuryExemption");
	}

	private void setTreasuryExemption(TreasuryExemption treasuryExemption,
			Model model) {
		model.addAttribute("treasuryExemption", treasuryExemption);
	}

	@Atomic
	public void deleteTreasuryExemption(TreasuryExemption treasuryExemption) {
		// CHANGE_ME: Do the processing for deleting the treasuryExemption
		// Do not catch any exception here

		// treasuryExemption.delete();
	}

	//
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		return "treasury/document/manageexemption/treasuryexemption/create";
	}

	//
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(
			@RequestParam(value = "code", required = false) java.lang.String code,
			@RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
			@RequestParam(value = "discountrate", required = false) java.math.BigDecimal discountRate,
			Model model, RedirectAttributes redirectAttributes) {
		/*
		 * Creation Logic
		 */

		try {

			TreasuryExemption treasuryExemption = createTreasuryExemption(code,
					name, discountRate);

			// Success Validation
			// Add the bean to be used in the View
			model.addAttribute("treasuryExemption", treasuryExemption);
			return redirect(
					"/treasury/accounting/managecustomer/customer/read/"
							+ getTreasuryExemption(model).getExternalId(),
					model, redirectAttributes);
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
	public TreasuryExemption createTreasuryExemption(java.lang.String code,
			org.fenixedu.commons.i18n.LocalizedString name,
			java.math.BigDecimal discountRate) {

		// @formatter: off

		/*
		 * Modify the creation code here if you do not want to create the object
		 * with the default constructor and use the setter for each field
		 */

		// CHANGE_ME It's RECOMMENDED to use "Create service" in DomainObject
		// TreasuryExemption treasuryExemption =
		// treasuryExemption.create(fields_to_create);

		// Instead, use individual SETTERS and validate "CheckRules" in the end
		// @formatter: on

		TreasuryExemption treasuryExemption = TreasuryExemption.create(code,
				name, discountRate);
		return treasuryExemption;
	}

	//
	@RequestMapping(value = "/")
	public String search(
			@RequestParam(value = "code", required = false) java.lang.String code,
			@RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
			@RequestParam(value = "discountrate", required = false) java.math.BigDecimal discountRate,
			Model model) {
		List<TreasuryExemption> searchtreasuryexemptionResultsDataSet = filterSearchTreasuryExemption(
				code, name, discountRate);

		// add the results dataSet to the model
		model.addAttribute("searchtreasuryexemptionResultsDataSet",
				searchtreasuryexemptionResultsDataSet);
		return "treasury/document/manageexemption/treasuryexemption/search";
	}

	private List<TreasuryExemption> getSearchUniverseSearchTreasuryExemptionDataSet() {
		//
		// The initialization of the result list must be done here
		//
		//
		return TreasuryExemption.findAll().collect(Collectors.toList());
	}

	private List<TreasuryExemption> filterSearchTreasuryExemption(
			java.lang.String code,
			org.fenixedu.commons.i18n.LocalizedString name,
			java.math.BigDecimal discountRate) {

		return getSearchUniverseSearchTreasuryExemptionDataSet()
				.stream()
				.filter(treasuryExemption -> code == null
						|| code.length() == 0
						|| (treasuryExemption.getCode() != null
								&& treasuryExemption.getCode().length() > 0 && treasuryExemption
								.getCode().toLowerCase()
								.contains(code.toLowerCase())))
				.filter(treasuryExemption -> name == null
						|| name.isEmpty()
						|| name.getLocales()
								.stream()
								.allMatch(
										locale -> treasuryExemption.getName()
												.getContent(locale) != null
												&& treasuryExemption
														.getName()
														.getContent(locale)
														.toLowerCase()
														.contains(
																name.getContent(
																		locale)
																		.toLowerCase())))
				.filter(treasuryExemption -> discountRate == null
						|| discountRate.equals(treasuryExemption
								.getDiscountRate()))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/search/view/{oid}")
	public String processSearchToViewAction(
			@PathVariable("oid") TreasuryExemption treasuryExemption,
			Model model, RedirectAttributes redirectAttributes) {

		// CHANGE_ME Insert code here for processing viewAction
		// If you selected multiple exists you must choose which one to use
		// below
		return redirect(
				"/treasury/document/manageexemption/treasuryexemption/read"
						+ "/" + treasuryExemption.getExternalId(), model,
				redirectAttributes);
	}

	//
	@RequestMapping(value = "/read/{oid}")
	public String read(
			@PathVariable("oid") TreasuryExemption treasuryExemption,
			Model model) {
		setTreasuryExemption(treasuryExemption, model);
		return "treasury/document/manageexemption/treasuryexemption/read";
	}

	//
	@RequestMapping(value = "/delete/{oid}", method = RequestMethod.POST)
	public String delete(
			@PathVariable("oid") TreasuryExemption treasuryExemption,
			Model model, RedirectAttributes redirectAttributes) {

		setTreasuryExemption(treasuryExemption, model);
		try {
			// call the Atomic delete function
			deleteTreasuryExemption(treasuryExemption);

			addInfoMessage("Sucess deleting TreasuryExemption ...", model);
			return redirect(
					"/treasury/document/manageexemption/treasuryexemption/",
					model, redirectAttributes);
		} catch (DomainException ex) {
			// Add error messages to the list
			addErrorMessage(
					"Error deleting the TreasuryExemption due to "
							+ ex.getLocalizedMessage(), model);
		}

		// The default mapping is the same Read View
		return "treasury/document/manageexemption/treasuryexemption/read/"
				+ getTreasuryExemption(model).getExternalId();
	}

	//
	@RequestMapping(value = "/update/{oid}", method = RequestMethod.GET)
	public String update(
			@PathVariable("oid") TreasuryExemption treasuryExemption,
			Model model) {
		setTreasuryExemption(treasuryExemption, model);
		return "treasury/document/manageexemption/treasuryexemption/update";
	}

	//
	@RequestMapping(value = "/update/{oid}", method = RequestMethod.POST)
	public String update(
			@PathVariable("oid") TreasuryExemption treasuryExemption,
			@RequestParam(value = "code", required = false) java.lang.String code,
			@RequestParam(value = "name", required = false) org.fenixedu.commons.i18n.LocalizedString name,
			@RequestParam(value = "discountrate", required = false) java.math.BigDecimal discountRate,
			Model model, RedirectAttributes redirectAttributes) {

		setTreasuryExemption(treasuryExemption, model);

		try {
			/*
			 * UpdateLogic here
			 */

			updateTreasuryExemption(code, name, discountRate, model);

			/* Succes Update */

			return redirect(
					"/treasury/document/manageexemption/treasuryexemption/read/"
							+ getTreasuryExemption(model).getExternalId(),
					model, redirectAttributes);
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
			return update(treasuryExemption, model);

		}
	}

	@Atomic
	public void updateTreasuryExemption(java.lang.String code,
			org.fenixedu.commons.i18n.LocalizedString name,
			java.math.BigDecimal discountRate, Model model) {

		// @formatter: off
		/*
		 * Modify the update code here if you do not want to update the object
		 * with the default setter for each field
		 */

		// CHANGE_ME It's RECOMMENDED to use "Edit service" in DomainObject
		// getTreasuryExemption(model).edit(fields_to_edit);

		// Instead, use individual SETTERS and validate "CheckRules" in the end
		// @formatter: on

		getTreasuryExemption(model).setCode(code);
		getTreasuryExemption(model).setName(name);
		getTreasuryExemption(model).setDiscountRate(discountRate);
	}

}
