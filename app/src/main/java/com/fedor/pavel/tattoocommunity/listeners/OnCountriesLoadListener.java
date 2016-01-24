package com.fedor.pavel.tattoocommunity.listeners;


public interface OnCountriesLoadListener {

    void loadSuccessful();

    void loadFailed(int errorCode);


}
