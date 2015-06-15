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

package org.fenixedu.treasury.domain.integration;

import java.util.Collection;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ERPConfiguration extends ERPConfiguration_Base {

    protected ERPConfiguration() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final Series paymentsIntegrationSeries, final FinantialInstitution finantialInstitution,
            final java.lang.String code, final java.lang.String externalURL, final java.lang.String username,
            final java.lang.String password) {
        setPaymentsIntegrationSeries(paymentsIntegrationSeries);
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setExternalURL(externalURL);
        setUsername(username);
        setPassword(password);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getPaymentsIntegrationSeries() == null) {
            throw new TreasuryDomainException("error.ERPConfiguration.paymentsIntegrationSeries.required");
        }

        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.ERPConfiguration.finantialInstitution.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByPaymentsIntegrationSeries(getPaymentsIntegrationSeries().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.paymentsIntegrationSeries.duplicated");
        //} 
        //if (findByFinantialInstitution(getFinantialInstitution().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.finantialInstitution.duplicated");
        //} 
        //if (findByCode(getCode().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.code.duplicated");
        //} 
        //if (findByExternalURL(getExternalURL().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.externalURL.duplicated");
        //} 
        //if (findByUsername(getUsername().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.username.duplicated");
        //} 
        //if (findByPassword(getPassword().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.password.duplicated");
        //} 
    }

    @Atomic
    public void edit(final Series paymentsIntegrationSeries, final FinantialInstitution finantialInstitution,
            final java.lang.String code, final java.lang.String externalURL, final java.lang.String username,
            final java.lang.String password) {
        setPaymentsIntegrationSeries(paymentsIntegrationSeries);
        setFinantialInstitution(finantialInstitution);
        setCode(code);
        setExternalURL(externalURL);
        setUsername(username);
        setPassword(password);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);

        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ERPConfiguration.cannot.be.deleted"));
        //}
    }

    public boolean isDeletable() {
        return false; // ACFSILVA
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.ERPConfiguration.cannot.delete");
        }
        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static ERPConfiguration create(final Series paymentsIntegrationSeries,
            final FinantialInstitution finantialInstitution, final java.lang.String code, final java.lang.String externalURL,
            final java.lang.String username, final java.lang.String password) {
        ERPConfiguration eRPConfiguration = new ERPConfiguration();
        eRPConfiguration.init(paymentsIntegrationSeries, finantialInstitution, code, externalURL, username, password);
        return eRPConfiguration;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<ERPConfiguration> findAll() {
        return Bennu.getInstance().getConfigurationsSet().stream().filter(c -> c instanceof ERPConfiguration)
                .map(ERPConfiguration.class::cast);
    }

    public static Stream<ERPConfiguration> findByPaymentsIntegrationSeries(final Series paymentsIntegrationSeries) {
        return findAll().filter(i -> paymentsIntegrationSeries.equals(i.getPaymentsIntegrationSeries()));
    }

    public static Stream<ERPConfiguration> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
        return findAll().filter(i -> finantialInstitution.equals(i.getFinantialInstitution()));
    }

    public static Stream<ERPConfiguration> findByCode(final java.lang.String code) {
        return findAll().filter(i -> code.equalsIgnoreCase(i.getCode()));
    }

    public static Stream<ERPConfiguration> findByExternalURL(final java.lang.String externalURL) {
        return findAll().filter(i -> externalURL.equalsIgnoreCase(i.getExternalURL()));
    }

    public static Stream<ERPConfiguration> findByUsername(final java.lang.String username) {
        return findAll().filter(i -> username.equalsIgnoreCase(i.getUsername()));
    }

    public static Stream<ERPConfiguration> findByPassword(final java.lang.String password) {
        return findAll().filter(i -> password.equalsIgnoreCase(i.getPassword()));
    }

}
