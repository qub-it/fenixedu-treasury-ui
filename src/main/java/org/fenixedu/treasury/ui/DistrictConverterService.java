package org.fenixedu.treasury.ui;

import org.springframework.core.convert.converter.Converter;

import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Place;

public class DistrictConverterService implements Converter<String, District> {

    @Override
    public District convert(String source) {
        Place district = GeographicInfoLoader.getInstance().importPlaceFromString(source);
        if (district instanceof District) {
            return (District) district;
        }
        return null;
    }

}
