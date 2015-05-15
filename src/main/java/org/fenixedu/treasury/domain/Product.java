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
package org.fenixedu.treasury.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.domain.tariff.Tariff;
import org.fenixedu.treasury.util.Constants;
import org.fenixedu.treasury.util.LocalizedStringUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class Product extends Product_Base {

    protected Product() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected Product(final ProductGroup productGroup, final String code, final LocalizedString name,
            final LocalizedString unitOfMeasure, boolean active) {
        this();
        setProductGroup(productGroup);
        setCode(code);
        setName(name);
        setUnitOfMeasure(unitOfMeasure);
        setActive(active);

        checkRules();
    }

    private void checkRules() {
        if (getProductGroup() == null) {
            throw new TreasuryDomainException("error.Product.productGroup.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getCode())) {
            throw new TreasuryDomainException("error.Product.code.required");
        }

        if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
            throw new TreasuryDomainException("error.Product.name.required");
        }

        findByCode(getCode());
        getName().getLocales().stream().forEach(l -> findByName(getName().getContent(l)));

        if (LocalizedStringUtil.isTrimmedEmpty(getUnitOfMeasure())) {
            throw new TreasuryDomainException("error.Product.unitOfMeasure.required");
        }
    }

    public boolean isActive() {
        return getActive();
    }

    @Atomic
    public void edit(final String code, final LocalizedString name, final LocalizedString unitOfMeasure, boolean active) {
        setCode(code);
        setName(name);
        setUnitOfMeasure(unitOfMeasure);
        setActive(active);

        checkRules();
    }

    public boolean isDeletable() {
        return true;
    }

    @Atomic
    public void delete() {
        if (!isDeletable()) {
            throw new TreasuryDomainException("error.Product.cannot.delete");
        }
        setProductGroup(null);
        setBennu(null);

        deleteDomainObject();
    }

    // @formatter: off
    /************
     * SERVICES *
     ************/
    // @formatter: on

    public static Stream<Product> findAll() {
        return Bennu.getInstance().getProductsSet().stream();
    }

    public static Stream<Product> findByCode(final String code) {
        return findAll().filter(p -> p.getCode().equalsIgnoreCase(code));
    }

    public static Stream<Product> findByName(final String name) {
        return findAll().filter(p -> LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(p.getName(), name));
    }

    public static LocalizedString defaultUnitOfMeasure() {
        return BundleUtil.getLocalizedString(Constants.BUNDLE, "label.unitOfMeasure.default");
    }

    @Atomic
    public static Product create(final ProductGroup productGroup, final String code, final LocalizedString name,
            final LocalizedString unitOfMeasure, boolean active) {
        return new Product(productGroup, code, name, unitOfMeasure, active);
    }

    public Stream<Tariff> getActiveTariffs(DateTime when) {
        return this
                .getTariffSet()
                .stream()
                .filter(x -> (x.getBeginDate() != null && x.getBeginDate().isBefore(when))
                        && (x.getEndDate() == null || x.getEndDate().isAfter(when)));

    }

    public Set<Tariff> getActiveTariffsSet() {
        return getActiveTariffs(new DateTime()).collect(Collectors.toSet());
    }

    public void updateFinantialInstitutions(List<FinantialInstitution> finantialInstitutions) {
        for (FinantialInstitution inst : this.getFinantialInstitutionsSet()) {
            if (!finantialInstitutions.contains(inst)) {
                this.removeFinantialInstitutions(inst);
                inst.removeAvailableProducts(this);
            }
        }

        for (FinantialInstitution inst2 : finantialInstitutions) {
            if (!this.getFinantialInstitutionsSet().contains(inst2)) {
                this.addFinantialInstitutions(inst2);
                inst2.addAvailableProducts(this);
            }
        }
    }
}
