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
package org.fenixedu.treasury.ui.accounting.managecustomer;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.AdhocCustomer;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.dto.AdhocCustomerBean;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = CustomerController.class)
@RequestMapping(AdhocCustomerController.CONTROLLER_URL)
public class AdhocCustomerController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/accounting/managecustomer/adhoccustomer";

    private static final String SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + SEARCH_URI;
    
    private static final String UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;

    private static final String CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + CREATE_URI;

    private static final String READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

    private static final String DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + DELETE_URI;


    private Customer getAdhocCustomer(Model model) {
        return (Customer) model.asMap().get("adhocCustomer");
    }

    private void setAdhocCustomer(Customer adhocCustomer, Model model) {
        model.addAttribute("adhocCustomer", adhocCustomer);
    }

    @Atomic
    public void deleteAdhocCustomer(Customer adhocCustomer) {
    }

    private void setAdhocCustomerBean(AdhocCustomerBean bean, Model model) {
        model.addAttribute("adhocCustomerBeanJson", getBeanJson(bean));
        model.addAttribute("adhocCustomerBean", bean);
    }

    @RequestMapping(value = SEARCH_URI)
    public String search(Model model) {
        List<Customer> searchadhoccustomerResultsDataSet = filterSearchAdhocCustomer();
        model.addAttribute("searchadhoccustomerResultsDataSet", searchadhoccustomerResultsDataSet);

        return "treasury/accounting/managecustomer/adhoccustomer/search";
    }

    private List<Customer> getSearchUniverseSearchAdhocCustomerDataSet() {
        return Customer.findAll().collect(Collectors.<Customer> toList());
    }

    private List<Customer> filterSearchAdhocCustomer() {
        return getSearchUniverseSearchAdhocCustomerDataSet().stream().collect(Collectors.toList());
    }

    private static final String SEARCH_VIEW_URI = "/search/view/";
    public static final String SEARCH_VIEW_URL = CONTROLLER_URL + SEARCH_VIEW_URI;

    @RequestMapping(value = SEARCH_VIEW_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") Customer adhocCustomer, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(READ_URL + adhocCustomer.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        AdhocCustomerBean bean = new AdhocCustomerBean();
        bean.update();
        
        return _create(bean, model);
    }
    
    private String _create(final AdhocCustomerBean bean, final Model model) {
        this.setAdhocCustomerBean(bean, model);
        return "treasury/accounting/managecustomer/adhoccustomer/create";
    }

    @RequestMapping(value = "/createpostback", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String createpostback(@RequestParam(value = "bean", required = false) final AdhocCustomerBean bean, Model model) {
        bean.update();

        return getBeanJson(bean);
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "bean", required = false) AdhocCustomerBean bean, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            assertUserIsBackOfficeMember(model);
            
            if(Strings.isNullOrEmpty(bean.getAddressCountryCode())) {
                addErrorMessage(treasuryBundle("error.Customer.addressCountryCode.required"), model);
                return _create(bean, model);
            }
            
            if(!bean.isAddressValid()) {
                throw new TreasuryDomainException("error.AdhocCustomer.fill.required.address.fields");
            }
            
            if(bean.getFinantialInstitutions() == null || bean.getFinantialInstitutions().isEmpty()) {
                throw new TreasuryDomainException("error.AdhocCustomer.specify.at.least.one.finantial.instituition");
            }
            
            final Customer adhocCustomer = AdhocCustomer.create(bean.getCustomerType(), bean.getFiscalNumber(), bean.getName(),
                    bean.getAddress(), bean.getDistrictSubdivision(), bean.getRegion(), bean.getZipCode(),
                    bean.getAddressCountryCode(), bean.getIdentificationNumber(), bean.getFinantialInstitutions());
            
            return redirect(CustomerController.READ_URL + adhocCustomer.getExternalId(), model, redirectAttributes);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
            return _create(bean, model);
        }
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") Customer adhocCustomer, Model model) {
        final AdhocCustomerBean bean = new AdhocCustomerBean(adhocCustomer);
        
        setAdhocCustomer(adhocCustomer, model);
        setAdhocCustomerBean(bean, model);
        return "treasury/accounting/managecustomer/adhoccustomer/update";
    }

    @RequestMapping(value = "/updatepostback/{oid}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody String updatepostback(@PathVariable("oid") Customer adhocCustomer,
            @RequestParam(value = "bean", required = false) AdhocCustomerBean bean, Model model) {
        bean.update();
        
        this.setAdhocCustomerBean(bean, model);
        return getBeanJson(bean);
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") final Customer adhocCustomer,
            @RequestParam(value = "bean", required = false) final AdhocCustomerBean bean, final Model model, final RedirectAttributes redirectAttributes) {
        setAdhocCustomer(adhocCustomer, model);

        try {
            assertUserIsBackOfficeMember(model);

            if(!bean.isAddressValid()) {
                throw new TreasuryDomainException("error.AdhocCustomer.fill.required.address.fields");
            }
            
            if (adhocCustomer.isAdhocCustomer()) {
                ((AdhocCustomer) adhocCustomer).edit(bean.getCustomerType(), bean.getName(), bean.getAddress(), bean.getDistrictSubdivision(), bean.getRegion(), bean.getZipCode(),
                        bean.getIdentificationNumber(), bean.getFinantialInstitutions());
            } else if(adhocCustomer.isPersonCustomer()) {
                adhocCustomer.registerFinantialInstitutions(bean.getFinantialInstitutions());
            }
            
            return redirect(CustomerController.READ_URL + getAdhocCustomer(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }
        return update(adhocCustomer, model);
    }

}
