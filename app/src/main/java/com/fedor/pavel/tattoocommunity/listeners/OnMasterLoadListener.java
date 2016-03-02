package com.fedor.pavel.tattoocommunity.listeners;

import com.fedor.pavel.tattoocommunity.models.UserModel;

import java.util.List;

/**
 * Created by pcc on 28.01.2016.
 */
public interface OnMasterLoadListener {

    void onMasterLoaded(List<UserModel> posts);
}
