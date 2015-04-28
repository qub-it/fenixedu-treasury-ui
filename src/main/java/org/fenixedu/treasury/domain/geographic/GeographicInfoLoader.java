package org.fenixedu.treasury.domain.geographic;

import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.Locality;
import pt.ist.standards.geographic.Municipality;
import pt.ist.standards.geographic.Planet;
import pt.ist.standards.geographic.PostalCode;

public class GeographicInfoLoader {

    private static GeographicInfoLoader geographicInfoLoader;
    private final Planet earth;

    private GeographicInfoLoader() {
        earth = Planet.getEarth();
    }

    public Country getCountryByAlpha3(String countryAlpha3) {
        return earth.getByAlfa3(countryAlpha3);
    }

    public District getDistrictByCode(String countryAlpha3, String districtCode) {
        return earth.getByAlfa3(countryAlpha3).getPlace(districtCode);
    }

    public Municipality getMunicipalityByCode(String countryAlpha3, String districtCode, String municipalityCode) {
        return earth.getByAlfa3(countryAlpha3).getPlace(districtCode).getPlace(municipalityCode);
    }

    public Locality getLocalityByCode(String countryAlpha3, String districtCode, String municipalityCode, String localityCode) {
        return earth.getByAlfa3(countryAlpha3).getPlace(districtCode).getPlace(municipalityCode).getPlace(localityCode);
    }

    public PostalCode getPostalCodeByCode(String countryAlpha3, String postalCodeString) {
        return earth.getByAlfa3(countryAlpha3).getPostalCode(postalCodeString);
    }

    synchronized public static GeographicInfoLoader getInstance() {
        if (geographicInfoLoader == null) {
            geographicInfoLoader = new GeographicInfoLoader();
        }
        return geographicInfoLoader;
    }

    public static String externalizeCountry(Country country) {
        return country.alpha3;
    }

    public static Country internalizeCountry(String countryString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        return geographicInfoLoader.getCountryByAlpha3(countryString);
    }

    public static String externalizeDistrict(District district) {
        return district.parent.alpha3 + ";" + district.code;
    }

    public static District internalizeDistrict(String districtString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        String[] tokens = districtString.split(";");
        String countryCode = tokens[0];
        String code = tokens[1];
        return geographicInfoLoader.getDistrictByCode(countryCode, code);
    }

    public static String externalizeMunicipality(Municipality municipality) {
        return municipality.parent.parent.alpha3 + ";" + municipality.parent.code + ";" + municipality.code;
    }

    public static Municipality internalizeMunicipality(String municipalityString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        String[] tokens = municipalityString.split(";");
        String countryCode = tokens[0];
        String districtCode = tokens[1];
        String code = tokens[2];
        return geographicInfoLoader.getMunicipalityByCode(countryCode, districtCode, code);
    }

    public static String externalizeLocality(Locality locality) {
        return locality.parent.parent.parent.alpha3 + ";" + locality.parent.parent.code + ";" + locality.parent.code + ";"
                + locality.code;
    }

    public static Locality internalizeLocality(String localityString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        String[] tokens = localityString.split(";");
        String countryCode = tokens[0];
        String districtCode = tokens[1];
        String municipalityCode = tokens[2];
        String code = tokens[3];
        return geographicInfoLoader.getLocalityByCode(countryCode, districtCode, municipalityCode, code);
    }

    public static String externalizePostalCode(PostalCode postalCode) {
        return postalCode.parent.parent.parent.parent.alpha3 + ";" + postalCode.code;
    }

    public static PostalCode internalizePostalCode(String postalCodeString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        String[] tokens = postalCodeString.split(";");
        String countryCode = tokens[0];
        String code = tokens[1];
        return geographicInfoLoader.getPostalCodeByCode(countryCode, code);
    }

}
