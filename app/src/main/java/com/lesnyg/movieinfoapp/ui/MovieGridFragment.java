package com.lesnyg.movieinfoapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lesnyg.movieinfoapp.DetailActivity;
import com.lesnyg.movieinfoapp.MovieViewModel;
import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.adapter.MovieRecyclerAdapter;
import com.lesnyg.movieinfoapp.models.Result;

import java.util.Collections;
import java.util.List;

public class MovieGridFragment extends Fragment {
    private MovieRecyclerAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MovieViewModel mModel;

    public MovieGridFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);
        RecyclerView mRecycler = view.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mAdapter = new MovieRecyclerAdapter(new MovieRecyclerAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Result result) {
                Intent intent = new Intent(requireActivity(), DetailActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });
        mRecycler.setAdapter(mAdapter);
        mModel.fetchUpComing(1);
        mModel.filteredResult.observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                if (results != null) {
                    mAdapter.setitems(results);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                boolean isScrollable = recyclerView.canScrollVertically(1);

                if (!isScrollable) {
                    mModel.fetchPopular(mModel.currentPage + 1);
                }
            }
        });

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mModel.fetchSearch(s);
                return true;
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mModel.movieMenu) {
                    case UPCOMING:
                        mModel.fetchUpComing(1);
                        break;
                    case SORT:
                        sorting();
                        break;
                    case POPULAR:
                        mModel.fetchPopular(1);
                        break;
                }

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upcomming:
                mModel.fetchUpComing(1);
                mModel.movieMenu = MovieViewModel.MovieMenu.UPCOMING;
                return true;
            case R.id.action_date:
                sorting();
                mModel.movieMenu = MovieViewModel.MovieMenu.SORT;
                return true;
            case R.id.action_popular:
                mModel.fetchPopular(1);
                mModel.movieMenu = MovieViewModel.MovieMenu.POPULAR;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sorting() {
        List<Result> resultList = mModel.filteredResult.getValue();
        Collections.sort(resultList, (Result o1, Result o2) -> {
            return o2.getRelease_date().compareTo(o1.getRelease_date());
        });

        mModel.filteredResult.setValue(resultList);

    }
}







