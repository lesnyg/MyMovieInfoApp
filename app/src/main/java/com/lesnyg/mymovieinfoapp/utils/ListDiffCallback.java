package com.lesnyg.mymovieinfoapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.lesnyg.mymovieinfoapp.models.Result;

import java.util.List;

public class ListDiffCallback extends DiffUtil.Callback {
    private final List<Result> oldList;
    private final List<Result> newList;

    public ListDiffCallback(List<Result> oldList, List<Result> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() ==
                newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Result oldItem = oldList.get(oldItemPosition);
        Result newItem = newList.get(newItemPosition);
        return oldItem.equals(newItem);
    }
}
