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

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.FenixEDUBaseController;
import org.fenixedu.treasury.domain.accesscontrol.TreasuryAccessControl;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class TreasuryBaseController extends FenixEDUBaseController {

    protected void assertUserIsManager(Model model, RedirectAttributes redirectAttributes) {
        if (TreasuryAccessControl.getInstance().isManager(Authenticate.getUser())) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.manager"), model);
            redirect("/treasury", model, redirectAttributes);
        }
    }

//    protected void assertUserIsBackOfficeMember(Model model, RedirectAttributes redirectAttributes) {
//        if (TreasuryAccessControl.getInstance().isBackOfficeMember(Authenticate.getUser())) {
//            return;
//        } else {
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.manager"), model);
//            redirect("/treasury", model, redirectAttributes);
//        }
//    }
//
//    protected void assertUserIsFrontOfficeMember(Model model, RedirectAttributes redirectAttributes) {
//        if (TreasuryAccessControl.getInstance().isFrontOfficeMember(Authenticate.getUser())) {
//            return;
//        } else {
//            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.manager"), model);
//            redirect("/treasury", model, redirectAttributes);
//        }
//    }

}
