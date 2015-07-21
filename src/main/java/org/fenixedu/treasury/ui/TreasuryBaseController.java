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
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.accesscontrol.TreasuryAccessControl;
import org.fenixedu.treasury.util.Constants;
import org.springframework.ui.Model;

public class TreasuryBaseController extends FenixEDUBaseController {

    protected void assertUserIsManager(Model model) {
        if (TreasuryAccessControl.getInstance().isManager(Authenticate.getUser())) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.manager"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.manager"));
        }
    }

    protected void assertUserIsBackOfficeMember(Model model) {
        if (TreasuryAccessControl.getInstance().isBackOfficeMember(Authenticate.getUser())) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.backoffice"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.backoffice"));
        }
    }

    protected void assertUserIsFrontOfficeMember(Model model) {
        if (TreasuryAccessControl.getInstance().isFrontOfficeMember(Authenticate.getUser())) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.frontoffice"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.frontoffice"));
        }
    }

    protected void assertUserIsBackOfficeMember(FinantialInstitution finantialInstitution, Model model) {
        if (TreasuryAccessControl.getInstance().isBackOfficeMember(Authenticate.getUser(), finantialInstitution)) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.backoffice"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.backoffice"));
        }
    }

    protected void assertUserIsAllowToModifySettlements(FinantialInstitution finantialInstitution, Model model) {
        if (TreasuryAccessControl.getInstance().isAllowToModifySettlements(Authenticate.getUser(), finantialInstitution)) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.allow.to.modify.settlements"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE,
                    "error.authorization.not.allow.to.modify.settlements"));
        }
    }

    protected void assertUserIsAllowToModifyInvoices(FinantialInstitution finantialInstitution, Model model) {
        if (TreasuryAccessControl.getInstance().isAllowToModifyInvoices(Authenticate.getUser(), finantialInstitution)) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.allow.to.modify.invoices"), model);
            throw new SecurityException(
                    BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.allow.to.modify.invoices"));
        }
    }

    protected void assertUserIsFrontOfficeMember(FinantialInstitution finantialInstitution, Model model) {
        if (TreasuryAccessControl.getInstance().isFrontOfficeMember(Authenticate.getUser(), finantialInstitution)) {
            return;
        } else {
            addErrorMessage(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.frontoffice"), model);
            throw new SecurityException(BundleUtil.getString(Constants.BUNDLE, "error.authorization.not.frontoffice"));
        }
    }

}
