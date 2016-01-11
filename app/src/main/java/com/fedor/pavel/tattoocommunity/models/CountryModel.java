package com.fedor.pavel.tattoocommunity.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Countries")
public class CountryModel extends ParseObject {

    public static final String NAME_PARSE_KEY = "name";


    public CountryModel() {

    }

    public CountryModel(String name) {

        setName(name);

    }

    public String getName() {

        return getString(NAME_PARSE_KEY);

    }

    public void setName(String name) {

        put(NAME_PARSE_KEY, name);

    }


}
