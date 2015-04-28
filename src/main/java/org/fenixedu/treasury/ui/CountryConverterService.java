package org.fenixedu.treasury.ui;

import org.fenixedu.treasury.domain.geographic.GeographicInfoLoader;
import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.Country;

public class CountryConverterService implements Converter<String, Country> {

    @Override
    public Country convert(String source) {
        return GeographicInfoLoader.getInstance().getCountryByAlpha3(source);
    }

//    @Override
//    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
//        return GeographicInfoLoader.getInstance().getCountryByAlpha3((String) source);
//    }
//
//    @Override
//    public Set<ConvertiblePair> getConvertibleTypes() {
//        return Collections.singleton(new ConvertiblePair(String.class, Country.class));
//    }
//
//    @Override
//    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
//        return String.class.equals(sourceType.getType()) && Country.class.isAssignableFrom(targetType.getType());
//    }

}
