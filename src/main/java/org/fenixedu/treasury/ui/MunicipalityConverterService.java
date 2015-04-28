package org.fenixedu.treasury.ui;

import org.fenixedu.treasury.domain.geographic.GeographicInfoLoader;
import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.Municipality;

public class MunicipalityConverterService implements Converter<String, Municipality> {

    @Override
    public Municipality convert(String source) {
        String[] tokens = source.split(";");
        if (tokens.length != 3) {
            return null;
        }
        return GeographicInfoLoader.getInstance().getCountryByAlpha3(tokens[0]).getPlace(tokens[1]).getPlace(tokens[2]);
    }

}
