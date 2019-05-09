package com.lesnyg.mymovieinfoapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.databinding.ItemMovieBinding;
import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
    private List<Result> mList = new ArrayList<>();

    public MovieRecyclerAdapter(OnMovieClickListener listener) {
        mListener = listener;
    }

    public void setitems(List<Result> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_movie, viewGroup, false);
        MovieHolder movieHolder = new MovieHolder(view);
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder movieHolder, int i) {
        Result result = mList.get(i);
        movieHolder.binding.setResult(result);
        movieHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMovieClick(mList.get(movieHolder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        private ItemMovieBinding binding;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMovieBinding.bind(itemView);
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(Result result);
    }

    private OnMovieClickListener mListener;

    public void setOnMovieClickListener(OnMovieClickListener listener) {
        mListener = listener;
    }
}
