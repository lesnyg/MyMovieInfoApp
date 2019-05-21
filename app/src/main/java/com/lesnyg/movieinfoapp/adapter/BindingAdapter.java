package com.lesnyg.movieinfoapp.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lesnyg.movieinfoapp.R;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("posterImage")
    public static void loadImage(ImageView view, String url) {

        String posterPath = "https://image.tmdb.org/t/p/w500" + url;
        Glide.with(view)
                .load(posterPath)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(view);
    }
}
