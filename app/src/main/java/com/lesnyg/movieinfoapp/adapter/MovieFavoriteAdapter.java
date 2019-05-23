package com.lesnyg.movieinfoapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.databinding.ItemFavoritesBinding;
import com.lesnyg.movieinfoapp.models.Result;

import java.util.ArrayList;
import java.util.List;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.MovieHolder> {
    public List<Result> mList = new ArrayList<>();

    public MovieFavoriteAdapter(OnFavoriteClickListener listener) {
        mListener = listener;
    }

    public void setitems(List<Result> items) {
        mList = items;
        notifyDataSetChanged();
    }

    public void updateItems(List<Result> items) {
        mList.clear();
        mList.addAll(items);
        notifyDataSetChanged();
    }

    public List<Result> getItems(){
        return mList;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_favorites, viewGroup, false);
        MovieHolder movieHolder = new MovieHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    final Result item = mList.get(movieHolder.getAdapterPosition());
                    mListener.onFavoriteClick(item);
                }
            }
        });
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder movieHolder, int i) {
        Result result = mList.get(i);
        movieHolder.binding.setResult(result);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MovieHolder extends RecyclerView.ViewHolder {
        private ItemFavoritesBinding binding;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFavoritesBinding.bind(itemView);
        }
    }


    public interface OnFavoriteClickListener {
        void onFavoriteClick(Result item);
    }

    private OnFavoriteClickListener mListener;

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        mListener = listener;
    }
}
