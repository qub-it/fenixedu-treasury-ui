/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
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
package org.fenixedu.treasury.ui.administration.base.manageglobalinterestrate;

import static org.fenixedu.treasury.util.TreasuryConstants.treasuryBundle;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.tariff.GlobalInterestRateType;
import org.fenixedu.treasury.domain.tariff.InterestRateEntry;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

//@Component("org.fenixedu.treasury.ui.administration.base.manageGlobalInterestRate") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.base.manageGlobalInterestRate",
        accessGroup = "treasuryBackOffice")
@RequestMapping(GlobalInterestRateController.CONTROLLER_URL)
public class GlobalInterestRateController extends TreasuryBaseController {

    public static final String CONTROLLER_URL = "/treasury/administration/base/manageglobalinterestrate/globalinterestrate";

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private InterestRateEntry getGlobalInterestRate(Model model) {
        return (InterestRateEntry) model.asMap().get("globalInterestRate");
    }

    private void setGlobalInterestRate(InterestRateEntry globalInterestRate, Model model) {
        model.addAttribute("globalInterestRate", globalInterestRate);
    }

    @Atomic
    public void deleteGlobalInterestRate(InterestRateEntry globalInterestRate) {
        globalInterestRate.delete();
    }

//				
    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI)
    public String search(@RequestParam(value = "description", required = false) LocalizedString description,
            @RequestParam(value = "rate", required = false) BigDecimal rate, Model model) {
        List<InterestRateEntry> searchglobalinterestrateResultsDataSet = filterSearchGlobalInterestRate(description, rate);

        model.addAttribute("searchglobalinterestrateResultsDataSet", searchglobalinterestrateResultsDataSet);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/search";
    }

    private Stream<InterestRateEntry> getSearchUniverseSearchGlobalInterestRateDataSet() {
        return GlobalInterestRateType.findUnique().get().getInterestRateEntriesSet().stream();
    }

    private List<InterestRateEntry> filterSearchGlobalInterestRate(LocalizedString description, BigDecimal rate) {

        return getSearchUniverseSearchGlobalInterestRateDataSet()
                .filter(globalInterestRate -> description == null || description.isEmpty()
                        || description.getLocales().stream()
                                .allMatch(locale -> globalInterestRate.getDescription().getContent(locale) != null
                                        && globalInterestRate.getDescription().getContent(locale).toLowerCase()
                                                .contains(description.getContent(locale).toLowerCase())))
                .filter(globalInterestRate -> rate == null || rate.equals(globalInterestRate.getRate()))
                .sorted(InterestRateEntry.FIRST_DATE_COMPARATOR.reversed()).collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_VIEW_ACTION_URI = "/search/view/";
    public static final String SEARCH_TO_VIEW_ACTION_URL = CONTROLLER_URL + _SEARCH_TO_VIEW_ACTION_URI;

    @RequestMapping(value = _SEARCH_TO_VIEW_ACTION_URI + "{oid}")
    public String processSearchToViewAction(@PathVariable("oid") InterestRateEntry globalInterestRate, Model model,
            RedirectAttributes redirectAttributes) {

        return redirect(READ_URL + globalInterestRate.getExternalId(), model, redirectAttributes);
    }

    private static final String _READ_URI = "/read/";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI + "{oid}")
    public String read(@PathVariable("oid") InterestRateEntry globalInterestRate, Model model) {
        setGlobalInterestRate(globalInterestRate, model);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/read";
    }

    private static final String _DELETE_URI = "/delete/";
    public static final String DELETE_URL = CONTROLLER_URL + _DELETE_URI;

    @RequestMapping(value = _DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") InterestRateEntry globalInterestRate, Model model,
            RedirectAttributes redirectAttributes) {

        setGlobalInterestRate(globalInterestRate, model);
        try {
            assertUserIsBackOfficeMember(model);

            deleteGlobalInterestRate(globalInterestRate);

            addInfoMessage(treasuryBundle("label.success.delete"), model);
            return redirect(SEARCH_URL, model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(treasuryBundle("label.error.delete") + tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.error.delete") + ex.getLocalizedMessage(), model);
        }

        return redirect(READ_URL + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
    }

    private static final String _CREATE_URI = "/create";
    public static final String CREATE_URL = CONTROLLER_URL + _CREATE_URI;

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.GET)
    public String create(Model model) {
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/create";
    }

    @RequestMapping(value = _CREATE_URI, method = RequestMethod.POST)
    public String create(@RequestParam(value = "firstDay", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate firstDay,
            @RequestParam(value = "description", required = false) LocalizedString description,
            @RequestParam(value = "rate", required = false) BigDecimal rate,
            @RequestParam(value = "applypaymentmonth", required = false) boolean applyPaymentMonth,
            @RequestParam(value = "applyinfirstworkday", required = false) boolean applyInFirstWorkday, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            assertUserIsBackOfficeMember(model);

            FenixFramework.atomic(() -> {
                GlobalInterestRateType globalInterestRateType = GlobalInterestRateType.findUnique().get();
                
                InterestRateEntry globalInterestRate = InterestRateEntry.create(globalInterestRateType, firstDay, description, rate,
                        applyPaymentMonth, applyInFirstWorkday);
                
                model.addAttribute("globalInterestRate", globalInterestRate);
            });

            return redirect(READ_URL + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }
        return create(model);
    }

    private static final String _UPDATE_URI = "/update/";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") InterestRateEntry globalInterestRate, Model model) {
        setGlobalInterestRate(globalInterestRate, model);
        return "treasury/administration/base/manageglobalinterestrate/globalinterestrate/update";
    }

    @RequestMapping(value = _UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") InterestRateEntry globalInterestRate,
            @RequestParam(value = "firstDay", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate firstDay,
            @RequestParam(value = "description", required = false) LocalizedString description,
            @RequestParam(value = "rate", required = false) BigDecimal rate,
            @RequestParam(value = "applypaymentmonth", required = false) boolean applyPaymentMonth,
            @RequestParam(value = "applyinfirstworkday", required = false) boolean applyInFirstWorkday, Model model,
            RedirectAttributes redirectAttributes) {

        setGlobalInterestRate(globalInterestRate, model);

        try {
            assertUserIsBackOfficeMember(model);

            FenixFramework.atomic(
                    () -> getGlobalInterestRate(model).edit(firstDay, description, rate, applyPaymentMonth, applyInFirstWorkday));

            return redirect(READ_URL + getGlobalInterestRate(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(treasuryBundle("label.error.update") + tde.getLocalizedMessage(), model);
        } catch (Exception ex) {
            addErrorMessage(treasuryBundle("label.error.update") + ex.getLocalizedMessage(), model);
        }
        return update(globalInterestRate, model);
    }
}
