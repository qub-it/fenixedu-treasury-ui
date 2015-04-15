package org.fenixedu.treasury.domain.integration;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.treasury.domain.exceptions.TreasuryDomainException;

import pt.ist.fenixframework.Atomic;

public class ExportOperation extends ExportOperation_Base {
        

		protected ExportOperation() {
			super();
//			setBennu(Bennu.getInstance());
		}

		protected ExportOperation(final String code) {
			this();

			checkRules();
		}

		private void checkRules() {
//			if (LocalizedStringUtil.isEmpty(getCode())) {
//				throw new TreasuryDomainException(
//						"error.ExportOperation.code.required");
//			}
//
//			if (LocalizedStringUtil.isTrimmedEmpty(getName())) {
//				throw new TreasuryDomainException(
//						"error.ExportOperation.name.required");
//			}
//
//			findByCode(getCode());
//			getName().getLocales().stream()
//					.forEach(l -> findByName(getName().getContent(l)));
		}

		@Atomic
		public void edit() {
//			setCode(code);
//			setName(name);

			checkRules();
		}

		public boolean isDeletable() {
			return true;
		}

		@Atomic
		public void delete() {
			if (!isDeletable()) {
				throw new TreasuryDomainException(
						"error.ExportOperation.cannot.delete");
			}

			setBennu(null);

			deleteDomainObject();
		}

		// @formatter: off
		/************
		 * SERVICES *
		 ************/
		// @formatter: on

		public static Set<ExportOperation> readAll() {
			return Bennu.getInstance().getIntegrationOperationsSet().stream().filter(x->x instanceof ExportOperation).map(ExportOperation.class::cast).collect(Collectors.toSet());
		}

		public static ExportOperation findByCode(final String code) {
			ExportOperation result = null;

			for (final ExportOperation it : readAll()) {
//				if (!it.getCode().equalsIgnoreCase(code)) {
//					continue;
//				}

				if (result != null) {
					throw new TreasuryDomainException(
							"error.ExportOperation.duplicated.code");
				}

				result = it;
			}

			return result;
		}

		public static ExportOperation findByName(final String name) {
			ExportOperation result = null;

			for (final ExportOperation it : readAll()) {

//				if (!LocalizedStringUtil.isEqualToAnyLocaleIgnoreCase(
//						it.getName(), name)) {
//					continue;
//				}

				if (result != null) {
					throw new TreasuryDomainException(
							"error.ExportOperation.duplicated.name");
				}

				result = it;
			}

			return result;
		}

//		@Atomic
//		public static ExportOperation create(final String code,
//				final LocalizedString name) {
//			return new ExportOperation(code, name);
//		}

    
}
