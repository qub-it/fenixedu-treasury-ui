package pt.ist.standards.geographic;

import java.util.stream.Stream;

public class GeographicInfoLoader {

    public static final String PRT = "PRT";
    private static GeographicInfoLoader geographicInfoLoader;
    private final Planet earth;

    private GeographicInfoLoader() {
        earth = Planet.getEarth();
    }

    public Stream<Country> findAllCountries() {
        return earth.getPlaces().stream();
    }

    synchronized public static GeographicInfoLoader getInstance() {
        if (geographicInfoLoader == null) {
            geographicInfoLoader = new GeographicInfoLoader();
        }
        return geographicInfoLoader;
    }

    public static boolean isDefaultCountry(Country country) {
        return country.alpha3.equals(PRT);
    }

    public Place importPlaceFromString(final String placeString) {
        if (placeString == null || placeString.length() == 0) {
            return null;
        }
        return earth.importFrom(placeString);
    }

    public static String externalizePlace(Place place) {
        return place.exportAsString();
    }

    public static <T extends Place> T internalizePlace(String placeString) {
        GeographicInfoLoader geographicInfoLoader = GeographicInfoLoader.getInstance();
        return (T) geographicInfoLoader.importPlaceFromString(placeString);
    }
}
