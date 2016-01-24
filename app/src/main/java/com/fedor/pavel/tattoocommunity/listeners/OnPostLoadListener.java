package com.fedor.pavel.tattoocommunity.listeners;


import com.fedor.pavel.tattoocommunity.models.PostModel;

import java.util.List;

public interface OnPostLoadListener {


    void onPostsLoaded(List<PostModel> posts);


}
