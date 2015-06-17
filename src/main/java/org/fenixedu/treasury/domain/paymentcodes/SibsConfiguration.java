/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
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

package org.fenixedu.treasury.domain.paymentcodes;

import java.util.Collection;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

import com.google.common.base.Strings;

public class SibsConfiguration extends SibsConfiguration_Base {

    protected SibsConfiguration() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected void init(final FinantialInstitution finantialInstitution, final java.lang.String entityReferenceCode,
            final java.lang.String sourceInstitutionId, final java.lang.String destinationInstitutionId) {
        setFinantialInstitution(finantialInstitution);
        setEntityReferenceCode(entityReferenceCode);
        setSourceInstitutionId(sourceInstitutionId);
        setDestinationInstitutionId(destinationInstitutionId);
        checkRules();
    }

    private void checkRules() {
        //
        //CHANGE_ME add more busines validations
        //
        if (getFinantialInstitution() == null) {
            throw new TreasuryDomainException("error.SibsConfiguration.finantialInstitution.required");
        }

        //CHANGE_ME In order to validate UNIQUE restrictions
        //if (findByFinantialInstitution(getFinantialInstitution().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.finantialInstitution.duplicated");
        //}	
        //if (findByEntityReferenceCode(getEntityReferenceCode().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.entityReferenceCode.duplicated");
        //}	
        //if (findBySourceInstitutionId(getSourceInstitutionId().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.sourceInstitutionId.duplicated");
        //}	
        //if (findByDestinationInstitutionId(getDestinationInstitutionId().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.destinationInstitutionId.duplicated");
        //}	
        //if (findByCode(getCode().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.code.duplicated");
        //}	
        //if (findByExternalURL(getExternalURL().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.externalURL.duplicated");
        //}	
        //if (findByUsername(getUsername().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.username.duplicated");
        //}	
        //if (findByPassword(getPassword().count()>1)
        //{
        //	throw new TreasuryDomainException("error.SibsConfiguration.password.duplicated");
        //}	
    }

    @Atomic
    public void edit(final FinantialInstitution finantialInstitution, final java.lang.String entityReferenceCode,
            final java.lang.String sourceInstitutionId, final java.lang.String destinationInstitutionId,
            final java.lang.String code, final java.lang.String externalURL, final java.lang.String username,
            final java.lang.String password) {
        setFinantialInstitution(finantialInstitution);
        setEntityReferenceCode(entityReferenceCode);
        setSourceInstitutionId(sourceInstitutionId);
        setDestinationInstitutionId(destinationInstitutionId);
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
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.SibsConfiguration.cannot.be.deleted"));
        //}
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

//        if (!isDeletable()) {
//            throw new TreasuryDomainException("error.SibsConfiguration.cannot.delete");
//        }
        setBennu(null);

        deleteDomainObject();
    }

    @Atomic
    public static SibsConfiguration create(final FinantialInstitution finantialInstitution,
            final java.lang.String entityReferenceCode, final java.lang.String sourceInstitutionId,
            final java.lang.String destinationInstitutionId) {
        SibsConfiguration sibsConfiguration = new SibsConfiguration();
        sibsConfiguration.init(finantialInstitution, entityReferenceCode, sourceInstitutionId, destinationInstitutionId);
        return sibsConfiguration;
    }

    public boolean isValid() {
        return !Strings.isNullOrEmpty(this.getDestinationInstitutionId())
                && !Strings.isNullOrEmpty(this.getSourceInstitutionId()) && !Strings.isNullOrEmpty(this.getEntityReferenceCode());
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

//    public static Stream<SibsConfiguration> findAll() {
//        return Bennu.getInstance().getSibsConfigurationsSet().stream();
//    }
//
//    public static Stream<SibsConfiguration> findByFinantialInstitution(final FinantialInstitution finantialInstitution) {
//        return findAll().filter(i -> finantialInstitution.equals(i.getFinantialInstitution()));
//    }
//
//    public static Stream<SibsConfiguration> findByEntityReferenceCode(final java.lang.String entityReferenceCode) {
//        return findAll().filter(i -> entityReferenceCode.equalsIgnoreCase(i.getEntityReferenceCode()));
//    }
//
//    public static Stream<SibsConfiguration> findBySourceInstitutionId(final java.lang.String sourceInstitutionId) {
//        return findAll().filter(i -> sourceInstitutionId.equalsIgnoreCase(i.getSourceInstitutionId()));
//    }
//
//    public static Stream<SibsConfiguration> findByDestinationInstitutionId(final java.lang.String destinationInstitutionId) {
//        return findAll().filter(i -> destinationInstitutionId.equalsIgnoreCase(i.getDestinationInstitutionId()));
//    }
//
//    public static Stream<SibsConfiguration> findByCode(final java.lang.String code) {
//        return findAll().filter(i -> code.equalsIgnoreCase(i.getCode()));
//    }
//
//    public static Stream<SibsConfiguration> findByExternalURL(final java.lang.String externalURL) {
//        return findAll().filter(i -> externalURL.equalsIgnoreCase(i.getExternalURL()));
//    }
//
//    public static Stream<SibsConfiguration> findByUsername(final java.lang.String username) {
//        return findAll().filter(i -> username.equalsIgnoreCase(i.getUsername()));
//    }
//
//    public static Stream<SibsConfiguration> findByPassword(final java.lang.String password) {
//        return findAll().filter(i -> password.equalsIgnoreCase(i.getPassword()));
//    }

}
