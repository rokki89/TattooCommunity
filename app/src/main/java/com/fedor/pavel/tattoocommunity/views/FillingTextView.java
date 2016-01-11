package com.fedor.pavel.tattoocommunity.views;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


public class FillingTextView extends TextView {


    public FillingTextView(Context context) {
        super(context);
    }

    public FillingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FillingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void offsetTextSizeRatio(int verticalOffset, AppBarLayout appBarLayout, boolean increase) {

        float titleTextSize;

        if (increase) {

            titleTextSize = Math.abs((float) verticalOffset) * ((float) (20.0) / appBarLayout.getTotalScrollRange());

        } else {

            titleTextSize = (float) (appBarLayout.getTotalScrollRange() - Math.abs(verticalOffset))
                    * ((float) (20.0) / appBarLayout.getTotalScrollRange());

        }

        setTextSize(titleTextSize == 0 ? 1f : titleTextSize);


        if (titleTextSize < 1) {

            setVisibility(View.GONE);

        } else {

            setVisibility(View.VISIBLE);
        }


    }

}
