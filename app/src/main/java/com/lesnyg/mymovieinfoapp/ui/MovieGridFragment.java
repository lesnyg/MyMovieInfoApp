package com.lesnyg.mymovieinfoapp.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lesnyg.mymovieinfoapp.DetailActivity;
import com.lesnyg.mymovieinfoapp.MainActivity;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.adapter.MovieRecyclerAdapter;
import com.lesnyg.mymovieinfoapp.models.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MovieGridFragment extends Fragment {
    private static final String ARG_MVIMAGE = "movieImage";
    private static final String ARG_MVTITLE = "movieTitle";

    private int mMovieImage;
    private String mMovieTitle;


    RecyclerView mRecycler;
    MovieRecyclerAdapter mAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private MovieViewModel mModel;


    public MovieGridFragment() {
        setHasOptionsMenu(true);
    }

    public static MovieGridFragment newInstance(Result result) {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MovieGridFragment newInstance() {

        Bundle args = new Bundle();

        MovieGridFragment fragment = new MovieGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieImage = getArguments().getInt(ARG_MVIMAGE);
            mMovieTitle = getArguments().getString(ARG_MVTITLE);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler = view.findViewById(R.id.recyclerview);
        mModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        if (getArguments() != null) {
            mModel.fetchUpComing();
        }

        mRecycler.setAdapter(mAdapter);


        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                boolean isScrollable = recyclerView.canScrollVertically(1);

                if (!isScrollable) {
                    mModel.fetchPopular(mModel.currentPage + 1);
                }
            }
        });
        mModel.filteredResult.observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                if(results != null) {
                    mAdapter.setitems(results);
                    mRecycler.setAdapter(mAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mAdapter = new MovieRecyclerAdapter(new MovieRecyclerAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Result result) {
                Intent intent = new Intent(requireActivity(), DetailActivity.class);
                intent.putExtra("result", result);
                startActivity(intent);
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
                mModel.fetchUpComing();
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
                mModel.fetchUpComing();
                return true;
            case R.id.action_date:
                sorting();
                return true;
            case R.id.action_popular:
                mModel.fetchPopular();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void sorting() {
        List<Result> resultList = new ArrayList<>();
        resultList = mModel.filteredResult.getValue();
        Collections.sort(resultList, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o2.getRelease_date().compareTo(o1.getRelease_date());
            }
        });

        mAdapter.setitems(resultList);
    }
}







