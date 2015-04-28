package org.fenixedu.treasury.ui;

import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Place;

public class CountryConverterService implements Converter<String, Country> {

    @Override
    public Country convert(String source) {
        Place country = GeographicInfoLoader.getInstance().importPlaceFromString(source);
        if (country instanceof Country) {
            return (Country) country;
        }
        return null;
    }
}
