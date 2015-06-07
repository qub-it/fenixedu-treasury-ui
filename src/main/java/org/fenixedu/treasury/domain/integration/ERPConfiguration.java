package org.fenixedu.treasury.domain.integration;

import java.util.Collection;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.document.Series;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.Constants;

import pt.ist.fenixframework.Atomic;

public class ERPConfiguration extends ERPConfiguration_Base {

    public ERPConfiguration() {
        super();
    }

    protected void init(final Series paymentsIntegrationSeries, final FinantialInstitution finantialInstitution,
            final boolean exportAnnulledRelatedDocuments, final java.lang.String code, final java.lang.String externalURL,
            final java.lang.String username, final java.lang.String password) {
        setPaymentsIntegrationSeries(paymentsIntegrationSeries);
        setFinantialInstitution(finantialInstitution);
        setExportAnnulledRelatedDocuments(exportAnnulledRelatedDocuments);
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
        //if (findByExportAnnulledRelatedDocuments(getExportAnnulledRelatedDocuments().count()>1)
        //{
        //  throw new TreasuryDomainException("error.ERPConfiguration.exportAnnulledRelatedDocuments.duplicated");
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
            final boolean exportAnnulledRelatedDocuments, final java.lang.String code, final java.lang.String externalURL,
            final java.lang.String username, final java.lang.String password) {
        setPaymentsIntegrationSeries(paymentsIntegrationSeries);
        setFinantialInstitution(finantialInstitution);
        setExportAnnulledRelatedDocuments(exportAnnulledRelatedDocuments);
        setCode(code);
        setExternalURL(externalURL);
        setUsername(username);
        setPassword(password);
        checkRules();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (this.getPaymentsIntegrationSeries() != null) {
            blockers.add(BundleUtil.getString(Constants.BUNDLE,
                    "error.ERPConfiguration.cannot.be.deleted.due.to.payments.integration.series"));
        }
        //add more logical tests for checking deletion rules
        //if (getXPTORelation() != null)
        //{
        //    blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.ERPConfiguration.cannot.be.deleted"));
        //}
    }

    @Atomic
    public void delete() {
        TreasuryDomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        if (!isDeletable()) {
            throw new TreasuryDomainException("error.ERPConfiguration.cannot.delete");
        }
        setFinantialInstitution(null);
        setPaymentsIntegrationSeries(null);
        deleteDomainObject();
    }

    private boolean isDeletable() {

        return true;
    }

    @Atomic
    public static ERPConfiguration create(final Series paymentsIntegrationSeries,
            final FinantialInstitution finantialInstitution, final boolean exportAnnulledRelatedDocuments,
            final java.lang.String code, final java.lang.String externalURL, final java.lang.String username,
            final java.lang.String password) {
        ERPConfiguration eRPConfiguration = new ERPConfiguration();
        eRPConfiguration.init(paymentsIntegrationSeries, finantialInstitution, exportAnnulledRelatedDocuments, code, externalURL,
                username, password);
        return eRPConfiguration;
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

}
