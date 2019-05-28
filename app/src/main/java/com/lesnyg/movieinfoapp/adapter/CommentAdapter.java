package com.lesnyg.movieinfoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.databinding.ItemCommentBinding;
import com.lesnyg.movieinfoapp.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mItems = new ArrayList<>();

    public CommentAdapter() {
    }


    public void setItems(List<Comment> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void updatecomment(List<Comment> comment) {
        mItems.clear();
        mItems.addAll(comment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        final CommentViewHolder viewHolder = new CommentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment item = mItems.get(position);
        holder.binding.setComment(item);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
        }
    }
}