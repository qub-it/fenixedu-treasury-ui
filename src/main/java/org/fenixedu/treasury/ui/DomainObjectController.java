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
package org.fenixedu.treasury.ui;

import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.DomainObject;

import com.qubit.solution.fenixedu.bennu.versioning.service.HistoryRetriever;
import com.qubit.solution.fenixedu.bennu.versioning.service.VersionableObject;

//@Component("org.fenixedu.treasury.ui.accounting.manageDebtEntry") <-- Use for duplicate controller name disambiguation
@SpringFunctionality(app = TreasuryController.class, title = "label.title.domainobject", accessGroup = "#managers")
// CHANGE_ME accessGroup = "group1 | group2 | groupXPTO"
// or
//@BennuSpringController(value = DebitNoteController.class)
@RequestMapping(DomainObjectController.CONTROLLER_URL)
public class DomainObjectController extends TreasuryBaseController {
    public static final String CONTROLLER_URL = "/treasury/domainobjectversions";

    private DomainObject getDomainObject(Model model) {
        return (DomainObject) model.asMap().get("domainObject");
    }

    private void setDomainObject(DomainObject domainObject, Model model) {
        model.addAttribute("domainObject", domainObject);
    }

    @RequestMapping(value = "/read/{oid}")
    public String processSearchToViewAction(@PathVariable("oid") DomainObject domainObject, Model model,
            RedirectAttributes redirectAttributes) {

        if (domainObject instanceof VersionableObject) {
            VersionableObject obj = (VersionableObject) domainObject;
            com.qubit.solution.fenixedu.bennu.versioning.service.HistoryRetriever history = new HistoryRetriever(obj);
            List<Map<String, Object>> retrieveVersions = history.retrieveVersions();
        }
        return "/domainobjectversions/search";
    }
}
