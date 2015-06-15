/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: xpto@qub-it.com
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
package org.fenixedu.treasury.ui.managetreasuryexemption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.event.TreasuryEvent;
import org.fenixedu.treasury.domain.exemption.TreasuryExemption;
import org.fenixedu.treasury.domain.exemption.TreasuryExemptionType;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.fenixedu.treasury.ui.accounting.managecustomer.TreasuryEventController;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@Component("org.fenixedu.treasury.ui.manageTreasuryExemption") <-- Use for duplicate controller name disambiguation
@BennuSpringController(value = TreasuryEventController.class)
@RequestMapping(TreasuryExemptionController.CONTROLLER_URL)
public class TreasuryExemptionController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/managetreasuryexemption/treasuryexemption";
    private static final String JSP_PATH = "treasury/managetreasuryexemption/treasuryexemption";

    private static final String _SEARCH_TO_DELETE_ACTION_URI = "/search/delete/";
    public static final String SEARCH_TO_DELETE_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_DELETE_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_DELETE_ACTION_URI + "{oid}", method = RequestMethod.POST)
    public String processSearchToDeleteAction(@PathVariable("oid") TreasuryExemption treasuryExemption, Model model,
            RedirectAttributes redirectAttributes) {
        
        final TreasuryEvent treasuryEvent = treasuryExemption.getTreasuryEvent();

        try {
            treasuryExemption.delete();

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.TreasuryExemption.deletion.success"), model);
        } catch (DomainException ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        return redirect(TreasuryEventController.READ_URL + treasuryEvent.getExternalId(), model,
                redirectAttributes);
    }

    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI + "/{tresuryEventId}", method = RequestMethod.GET)
    public String create(@PathVariable("tresuryEventId") final TreasuryEvent treasuryEvent, final Model model) {

        model.addAttribute("treasuryEvent", treasuryEvent);

        model.addAttribute("TreasuryExemption_treasuryExemptionType_options",
                TreasuryExemptionType.findAll().sorted(TreasuryExemptionType.COMPARE_BY_NAME).collect(Collectors.toList()));

        model.addAttribute("TreasuryExemption_product_options",
                treasuryEvent.getPossibleProductsToExempt().stream().sorted(Product.COMPARE_BY_NAME).collect(Collectors.toList()));

        return jspPage("create");
    }

    @RequestMapping(value = _CREATE_URI + "/{tresuryEventId}", method = RequestMethod.POST)
    public String create(@PathVariable("tresuryEventId") final TreasuryEvent treasuryEvent, @RequestParam(
            value = "treasuryexemptiontype", required = false) final TreasuryExemptionType treasuryExemptionType, @RequestParam(
            value = "exemptbypercentage", required = false) final boolean exemptByPercentage, @RequestParam(
            value = "valuetoexempt", required = false) final BigDecimal valueToExempt, @RequestParam(value = "product",
            required = false) final Product product, @RequestParam(value = "reason", required = false) final String reason,
            final Model model, final RedirectAttributes redirectAttributes) {

        try {

            TreasuryExemption.create(treasuryExemptionType, treasuryEvent, reason, valueToExempt, product, true);

            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.TreasuryExemption.creation.success"), model);

            return redirect(TreasuryEventController.READ_URL + treasuryEvent.getExternalId(), model, redirectAttributes);
        } catch (DomainException de) {
            addErrorMessage(de.getLocalizedMessage(), model);

            return create(treasuryEvent, model);
        }
    }

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

}
