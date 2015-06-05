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
package org.fenixedu.treasury.ui.administration.managefinantialinstitution;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.ui.TreasuryBaseController;
import org.springframework.stereotype.Component;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@Component("org.fenixedu.treasury.ui.administration.manageFinantialInstitution")
//@SpringFunctionality(app = TreasuryController.class, title = "label.title.administration.manageFinantialInstitution",accessGroup = "managers")
@BennuSpringController(value = FinantialInstitutionController.class)
@RequestMapping(FinantialEntityController.CONTROLLER_URL)
public class FinantialEntityController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/administration/managefinantialinstitution/finantialentity";
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

    @RequestMapping
    public String home(Model model) {
        //this is the default behaviour, for handling in a Spring Functionality
        if (model.containsAttribute("finantialInstitutionId")) {
            return "forward:/treasury/administration/managefinantialinstitution/finantialinstitution/read/"
                    + model.asMap().get("finantialInstitutionId");
        }
        return "forward:/treasury/administration/managefinantialinstitution/finantialinstitution/";
    }

    private FinantialEntity getFinantialEntity(Model model) {
        return (FinantialEntity) model.asMap().get("finantialEntity");
    }

    private void setFinantialEntity(FinantialEntity finantialEntity, Model model) {
        model.addAttribute("finantialEntity", finantialEntity);
    }

    @Atomic
    public void deleteFinantialEntity(FinantialEntity finantialEntity) {
        finantialEntity.delete();
    }

    @RequestMapping(value = "/search/view/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") FinantialEntity finantialEntity, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(
                "/treasury/administration/managefinantialinstitution/finantialentity/read" + "/"
                        + finantialEntity.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = READ_URI + "{oid}")
    public String read(@PathVariable("oid") FinantialEntity finantialEntity, Model model) {
        setFinantialEntity(finantialEntity, model);
        return "treasury/administration/managefinantialinstitution/finantialentity/read";
    }

    @RequestMapping(value = DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String delete(@PathVariable("oid") FinantialEntity finantialEntity, Model model, RedirectAttributes redirectAttributes) {
        setFinantialEntity(finantialEntity, model);
        try {
            deleteFinantialEntity(finantialEntity);
            addInfoMessage(BundleUtil.getString(Constants.BUNDLE, "label.success.delete"), model);
            return redirect("/treasury/administration/managefinantialinstitution/finantialentity/", model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage("Error deleting the FinantialEntity due to " + tde.getLocalizedMessage(), model);
        }
        return "treasury/administration/managefinantialinstitution/finantialentity/read/"
                + getFinantialEntity(model).getExternalId();
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.GET)
    public String create(
            @RequestParam(value = "finantialInstitutionId", required = false) FinantialInstitution finantialInstitution,
            Model model) {
        model.addAttribute("finantialInstitutionId", finantialInstitution.getExternalId());
        return "treasury/administration/managefinantialinstitution/finantialentity/create";
    }

    @RequestMapping(value = CREATE_URI, method = RequestMethod.POST)
    public String create(
            @RequestParam(value = "finantialInstitutionId", required = false) FinantialInstitution finantialInstitution,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            FinantialEntity finantialEntity = createFinantialEntity(finantialInstitution, code, name);
            model.addAttribute("finantialEntity", finantialEntity);
            return redirect(
                    "/treasury/administration/managefinantialinstitution/finantialentity/read/"
                            + getFinantialEntity(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(" Error creating due to " + tde.getLocalizedMessage(), model);
            return create(finantialInstitution, model);
        }
    }

    @Atomic
    public FinantialEntity createFinantialEntity(FinantialInstitution finantialInstitution, String code, LocalizedString name) {
        FinantialEntity finantialEntity = FinantialEntity.create(finantialInstitution, code, name);
        return finantialEntity;
    }

    @RequestMapping(value = "/search/edit/{oid}")
    public String processSearchToEditAction(@PathVariable("oid") FinantialEntity finantialEntity, Model model,
            RedirectAttributes redirectAttributes) {
        return redirect(
                "/treasury/administration/managefinantialinstitution/finantialentity/update" + "/"
                        + finantialEntity.getExternalId(), model, redirectAttributes);
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String update(@PathVariable("oid") FinantialEntity finantialEntity, Model model) {
        setFinantialEntity(finantialEntity, model);
        return "treasury/administration/managefinantialinstitution/finantialentity/update";
    }

    @RequestMapping(value = UPDATE_URI + "{oid}", method = RequestMethod.POST)
    public String update(@PathVariable("oid") FinantialEntity finantialEntity,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) LocalizedString name, Model model,
            RedirectAttributes redirectAttributes) {
        setFinantialEntity(finantialEntity, model);
        try {
            updateFinantialEntity(code, name, model);
            /*Success Update */
            return redirect(
                    "/treasury/administration/managefinantialinstitution/finantialentity/read/"
                            + getFinantialEntity(model).getExternalId(), model, redirectAttributes);
        } catch (TreasuryDomainException tde) {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "label.error.update") + tde.getLocalizedMessage(), model);
            return update(finantialEntity, model);
        }
    }

    @Atomic
    public void updateFinantialEntity(String code, LocalizedString name, Model model) {
        getFinantialEntity(model).edit(code, name);
    }

}
