package com.lesnyg.mymovieinfoapp.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.List;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("items")
    public static void items(RecyclerView recyclerView, List<Result> items) {
        MovieRecyclerAdapter adapter = (MovieRecyclerAdapter) recyclerView.getAdapter();
        adapter.setitems(items);
    }
}
