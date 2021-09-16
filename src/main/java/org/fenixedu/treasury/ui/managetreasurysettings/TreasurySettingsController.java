/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa
 * software development project between Quorum Born IT and ServiÃ§os Partilhados da
 * Universidade de Lisboa:
 *  - Copyright Â© 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright Â© 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: ricardo.pedro@qub-it.com
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
package org.fenixedu.treasury.ui.managetreasurysettings;

import java.util.stream.Collectors;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.manageTreasurySettings",
        accessGroup = "treasuryManagers")
@RequestMapping(TreasurySettingsController.CONTROLLER_URL)
public class TreasurySettingsController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/managetreasurysettings/treasurysettings";
    private static final String UPDATE_URI = "/update";
    public static final String UPDATE_URL = CONTROLLER_URL + UPDATE_URI;
    private static final String READ_URI = "/read";
    public static final String READ_URL = CONTROLLER_URL + READ_URI;

    @RequestMapping
    public String home(Model model) {
        return "forward:/treasury/managetreasurysettings/treasurysettings/read";
    }

    @RequestMapping(value = READ_URI)
    public String read(final Model model) {
        model.addAttribute("treasurySettings", TreasurySettings.getInstance());

        return "treasury/managetreasurysettings/treasurysettings/read";
    }

    @RequestMapping(value = UPDATE_URI, method = RequestMethod.GET)
    public String update(final Model model) {
        model.addAttribute("TreasurySettings_defaultCurrency_options",
                Currency.findAll().sorted((x, y) -> x.getName().getContent().compareToIgnoreCase(y.getName().getContent()))
                        .collect(Collectors.toList()));
        model.addAttribute("TreasurySettings_interestProduct_options",
                Product.findAllActive().sorted((x, y) -> x.getName().getContent().compareToIgnoreCase(y.getName().getContent()))
                        .collect(Collectors.toList()));
        model.addAttribute("treasurySettings", TreasurySettings.getInstance());

        return "treasury/managetreasurysettings/treasurysettings/update";
    }

    @RequestMapping(value = UPDATE_URI, method = RequestMethod.POST)
    public String update(@RequestParam(value = "defaultcurrency", required = true) final Currency defaultCurrency,
            @RequestParam(value = "interestproduct", required = true) final Product interestProduct,
            @RequestParam(value = "advancedpaymentproduct", required = true) final Product advancedPaymentProduct,
            @RequestParam(value = "numberofpaymentplansperStudent", required = true) final Integer numberOfPaymentPlansPerStudent,
            final Model model, final RedirectAttributes redirectAttributes) {
        final TreasurySettings treasurySettings = TreasurySettings.getInstance();

        model.addAttribute("treasurySettings", treasurySettings);

        try {
            treasurySettings.edit(defaultCurrency, interestProduct, advancedPaymentProduct, numberOfPaymentPlansPerStudent, treasurySettings.getCanRegisterPaymentWithMultipleMethods());

            return redirect(READ_URL, model, redirectAttributes);
        } catch (final TreasuryDomainException tde) {
            addErrorMessage(tde.getLocalizedMessage(), model);
            return update(model);
        }
    }
}
