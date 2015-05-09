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
package org.fenixedu.treasury.ui.managetreasurysettings;

import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.fenixedu.treasury.ui.TreasuryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@SpringFunctionality(app = TreasuryController.class, title = "label.title.manageTreasurySettings", accessGroup = "logged")
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
        model.addAttribute("TreasurySettings_defaultCurrency_options", Currency.findAll().collect(Collectors.toSet()));
        model.addAttribute("TreasurySettings_defaultVatType_options", VatType.findAll().collect(Collectors.toSet()));
        model.addAttribute("treasurySettings", TreasurySettings.getInstance());

        return "treasury/managetreasurysettings/treasurysettings/update";
    }

    @RequestMapping(value = UPDATE_URI, method = RequestMethod.POST)
    public String update(@RequestParam(value = "defaultcurrency", required = true) final Currency defaultCurrency, @RequestParam(
            value = "defaultvattype", required = true) final VatType defaultVatType, final Model model,
            final RedirectAttributes redirectAttributes) {

        final TreasurySettings treasurySettings = TreasurySettings.getInstance();

        model.addAttribute("treasurySettings", treasurySettings);

        try {
            treasurySettings.edit(defaultCurrency, defaultVatType);

            return redirect("/treasury/managetreasurysettings/treasurysettings/read", model, redirectAttributes);
        } catch (final DomainException de) {
            addErrorMessage(de.getLocalizedMessage(), model);
            return update(model);
        }
    }

}
