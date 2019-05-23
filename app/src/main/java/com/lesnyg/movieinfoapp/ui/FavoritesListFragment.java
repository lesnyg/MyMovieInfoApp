package com.lesnyg.movieinfoapp.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lesnyg.movieinfoapp.DetailActivity;
import com.lesnyg.movieinfoapp.MovieViewModel;
import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.SwipeController;
import com.lesnyg.movieinfoapp.SwipeControllerActions;
import com.lesnyg.movieinfoapp.adapter.MovieFavoriteAdapter;
import com.lesnyg.movieinfoapp.models.Result;

import java.util.Collections;
import java.util.List;


public class FavoritesListFragment extends Fragment implements MovieFavoriteAdapter.OnFavoriteClickListener {
    private boolean isSwapped = false;
    private Result mResult;
    private MovieViewModel mViewModel;
    private SearchView mSearchView;
    private SwipeController swipeController = null;


    public FavoritesListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_favorites);

        MovieFavoriteAdapter adapter = new MovieFavoriteAdapter(new MovieFavoriteAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(Result item) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_main, MovieDetailFragment.newInstance(item));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        recyclerView.setAdapter(adapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            //삭제버튼
            @Override
            public void onRightClicked(int position) {
                mResult = adapter.mList.get(position);
                mViewModel.deleteFavorite(mResult);
            }


        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                Collections.swap(adapter.getItems(),
                        viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());

                adapter.notifyItemMoved(viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());

                isSwapped = true;

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            private void setItemsClickable(RecyclerView recyclerView,
                                           boolean isClickable) {
                for (int i = 0; i < recyclerView.getChildCount(); ++i) {
                    recyclerView.getChildAt(i).setClickable(isClickable);
                }
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);

        mViewModel.getFavorite().observe(requireActivity(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> items) {
                mViewModel.results = items;
                adapter.updateItems(items);
                mViewModel.searchResult.setValue(mViewModel.results);
            }
        });

        mViewModel.searchResult.observe(requireActivity(), new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                mSearchView = view.findViewById(R.id.favorite_search_view);
                mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        mViewModel.searchFavorite(s);
                        adapter.updateItems(mViewModel.searchResult.getValue());
                        return true;
                    }

                });
            }

        });

        adapter.setOnFavoriteClickListener(this);

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onFavoriteClick(Result item) {
        Intent intent = new Intent(requireActivity(), DetailActivity.class);
        intent.putExtra("result", item);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.favorite_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                mSearchView.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



