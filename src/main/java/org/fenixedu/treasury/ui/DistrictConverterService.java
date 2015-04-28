package org.fenixedu.treasury.ui;

import org.fenixedu.treasury.domain.geographic.GeographicInfoLoader;
import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.District;

public class DistrictConverterService implements Converter<String, District> {

    @Override
    public District convert(String source) {
        String[] tokens = source.split(";");
        if (tokens.length != 2) {
            return null;
        }
        return GeographicInfoLoader.getInstance().getCountryByAlpha3(tokens[0]).getPlace(tokens[1]);
    }

}
