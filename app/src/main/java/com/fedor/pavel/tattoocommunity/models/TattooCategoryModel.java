package com.fedor.pavel.tattoocommunity.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("TattooCategories")
public class TattooCategoryModel extends ParseObject {

    public static final String NAME_PARSE_KEY = "name";

    public String getName() {

        return getString(NAME_PARSE_KEY);

    }

    public void setName(String put) {

        put(NAME_PARSE_KEY, put);

    }


}
