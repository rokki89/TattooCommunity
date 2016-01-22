package com.fedor.pavel.tattoocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.text.ParsePosition;

@ParseClassName("Cities")
public class CityModel extends ParseObject {

    public static final String COUNTRY_ID_PARSE_KEY = "country_id";

    public static final String NAME_PARSE_KEY = "name";

    public static final String COORDINATES_PARSE_KEY  = "coordinates";

    public CityModel() {


    }

    public CityModel(String countryId, String name) {

        setCountryId(countryId);

        setName(name);

    }

    public String getName() {
        return getString(NAME_PARSE_KEY);
    }

    public String getCountryId() {
        return getString(COUNTRY_ID_PARSE_KEY);
    }

    public void setName(String name) {

        put(NAME_PARSE_KEY, name);

    }

    public void setCountryId(String countryId) {

        put(COUNTRY_ID_PARSE_KEY, countryId);

    }

    public void setCoordinates(double lat, double lng){

        put(COORDINATES_PARSE_KEY, new ParseGeoPoint(lat, lng));

    }

    public ParseGeoPoint getGeoPoint() {

        return (ParseGeoPoint) get(COORDINATES_PARSE_KEY);

    }

}
