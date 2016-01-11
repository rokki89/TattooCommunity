package com.fedor.pavel.tattoocommunity.models;


import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Place")
public class PlaceModel extends ParseObject {


    public static final String USER_ID_PARSE_KEY = "user_id";

    public static final String CITY_ID_PARSE_KEY = "city_id";

    public static final String STREET_PARSE_KEY = "street";

    public static final String NAME_PARSE_KEY = "name";

    public static final String COORDINATES_PARSE_KEY = "coordinates";

    public PlaceModel() {

    }

    public PlaceModel(String userId, String cityId, String street, String name, int lat, int lng) {

        setUserId(userId);

        setCityId(cityId);

        setStreet(street);

        setName(name);

        setCoordinates(lat, lng);


    }

    public PlaceModel(String userId, String cityId, String street, String name) {


        setUserId(userId);

        setCityId(cityId);

        setStreet(street);

        setName(name);


    }

    public String getUserId() {

        return getString(USER_ID_PARSE_KEY);

    }

    public String getCityId() {

        return getString(CITY_ID_PARSE_KEY);

    }

    public String getStreet() {

        return getString(STREET_PARSE_KEY);

    }

    public String getName() {

        return getString(NAME_PARSE_KEY);

    }

    public ParseGeoPoint getGeoPoint() {

        return (ParseGeoPoint) get(COORDINATES_PARSE_KEY);

    }

    public void setUserId(String userId) {

        put(USER_ID_PARSE_KEY, userId);

    }

    public void setCityId(String cityId) {

        put(CITY_ID_PARSE_KEY, cityId);

    }

    public void setStreet(String street) {

        put(STREET_PARSE_KEY, street);

    }

    public void setName(String name) {

        put(NAME_PARSE_KEY, name);

    }

    public void setCoordinates(float lat, float lng) {

        put(COORDINATES_PARSE_KEY, new ParseGeoPoint(lat, lng));

    }

}
