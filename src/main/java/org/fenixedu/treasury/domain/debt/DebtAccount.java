package org.fenixedu.treasury.domain.debt;

import java.util.Set;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.Customer;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class DebtAccount extends DebtAccount_Base {

	public DebtAccount() {
		super();
        setBennu(Bennu.getInstance());
	}
	



    protected DebtAccount(final FinantialInstitution finantialInstitution, final Customer customer) {
        this();
		setCustomer(customer);
		setFinantialInstitution(insitution);

		checkRules();
	}

	private void checkRules() {
		if (LocalizedStringUtil.isEmpty(getCode())) {
			throw new TreasuryDomainException("error.DebtAccount.code.required");
		}

		if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
			throw new TreasuryDomainException("error.DebtAccount.name.required");
		}

		findByCode(getCode());
		getName().getLocales().stream()
				.forEach(l -> findByName(getName().getContent(l)));
	}

	@Atomic
	public void edit(final String code, final LocalizedString name) {
		setCode(code);
		setName(name);

		checkRules();
	}

	public boolean isDeletable() {
		return true;
	}

	@Atomic
	public void delete() {
		if (!isDeletable()) {
			throw new TreasuryDomainException("error.DebtAccount.cannot.delete");
		}

		setBennu(null);

		deleteDomainObject();
	}

	// @formatter: off
	/************
	 * SERVICES *
	 ************/
	// @formatter: on

	public static Set<DebtAccount> readAll() {
		return Bennu.getInstance().getDebtAccountsSet();
	}

	public static DebtAccount findByCode(final String code) {
		DebtAccount result = null;

		for (final DebtAccount it : readAll()) {
			if (!it.getCode().equalsIgnoreCase(code)) {
				continue;
			}

			if (result != null) {
				throw new TreasuryDomainException(
						"error.DebtAccount.duplicated.code");
			}

			result = it;
		}

		return result;
	}

	public static DebtAccount findByName(final String name) {
		DebtAccount result = null;

		for (final DebtAccount it : readAll()) {

			if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(it.getName(),
					name)) {
				continue;
			}

			if (result != null) {
				throw new TreasuryDomainException(
						"error.DebtAccount.duplicated.name");
			}

			result = it;
		}

		return result;
	}

    @Atomic
    public static DebtAccount create(final FinantialInstitution finantialInstitution, final Customer customer) {
        return new DebtAccount(finantialInstitution, customer);
    }

}
