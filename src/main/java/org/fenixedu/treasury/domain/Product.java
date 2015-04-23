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

import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;
import org.fenixedu.treasury.util.LocalizedStringUtil;

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

    public static Set<Product> readAll() {
        return Bennu.getInstance().getProductsSet();
    }

    public static Product findByCode(final String code) {
        Product result = null;

        for (final Product it : readAll()) {
            if (!it.getCode().equalsIgnoreCase(code)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Product.duplicated.code");
            }

            result = it;
        }

        return result;
    }

    public static Product findByName(final String name) {
        Product result = null;

        for (final Product it : readAll()) {

            if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(), name)) {
                continue;
            }

            if (result != null) {
                throw new TreasuryDomainException("error.Product.duplicated.name");
            }

            result = it;
        }

        return result;
    }

    @Atomic
    public static Product create(final ProductGroup productGroup, final String code, final LocalizedString name,
            final LocalizedString unitOfMeasure, boolean active) {
        return new Product(productGroup, code, name, unitOfMeasure, active);
    }

    public boolean isActive() {
        return getActive();
    }
}
