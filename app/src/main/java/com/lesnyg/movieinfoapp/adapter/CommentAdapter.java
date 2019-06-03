package com.lesnyg.movieinfoapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.databinding.ItemCommentBinding;
import com.lesnyg.movieinfoapp.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.CommentViewHolder> {
    private List<Comment> mItems = new ArrayList<>();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options) {
        super(options);
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

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull Comment comment) {
        commentViewHolder.binding.setComment(getItem(i));
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new CommentViewHolder(view);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommentBinding.bind(itemView);
        }
    }
}