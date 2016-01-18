package com.fedor.pavel.tattoocommunity.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fedor.pavel.tattoocommunity.R;
import com.fedor.pavel.tattoocommunity.models.PostModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


public class ProportionalImageView extends ImageView {

    private float proportion;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = this.getMeasuredWidth();

        int height = (int) (width / proportion);

        setMeasuredDimension(width, height);

    }

    public void calculateProportion(PostModel postModel) {

        proportion = (float) postModel.getPhotoWidth() / (float) postModel.getPhotoHeight();

        ImageLoader.getInstance().displayImage(postModel.getPhotoUrl(),this,displayImageOptions());

    }

    public static DisplayImageOptions displayImageOptions() {

        return new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.bg_panel_gradient)
                .showImageForEmptyUri(R.drawable.bg_panel_gradient)
                .showImageOnFail(R.drawable.bg_panel_gradient)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

}
