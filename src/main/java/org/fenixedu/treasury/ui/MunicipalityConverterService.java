package org.fenixedu.treasury.ui;

import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Municipality;
import pt.ist.standards.geographic.Place;

public class MunicipalityConverterService implements Converter<String, Municipality> {

    @Override
    public Municipality convert(String source) {
        Place municipality = GeographicInfoLoader.getInstance().importPlaceFromString(source);
        if (municipality instanceof Municipality) {
            return (Municipality) municipality;
        }
        return null;
    }

}
