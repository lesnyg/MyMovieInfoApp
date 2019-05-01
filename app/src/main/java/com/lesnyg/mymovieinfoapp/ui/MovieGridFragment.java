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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lesnyg.mymovieinfoapp.MainActivity;
import com.lesnyg.mymovieinfoapp.adapter.MovieRecyclerAdapter;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
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
        // Required empty public constructor
    }

    public static MovieGridFragment newInstance() {
        MovieGridFragment fragment = new MovieGridFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
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
        mAdapter = new MovieRecyclerAdapter(new MovieRecyclerAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Result result) {


            }
        });

        mRecycler.setAdapter(mAdapter);
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        List<Result> notiResult = new ArrayList<>();
        for (int i = 0; i < notiResult.size(); i++) {
            Result result = notiResult.get(i);
            if (Integer.valueOf(result.getRelease_date()) > Integer.valueOf(today)) {
//                noti();
            }
        }
        mModel.filteredResult.observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                mAdapter.setitems(results);
                mRecycler.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);

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

//    public void noti() {
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(requireActivity(), "default")
//                        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
//                        .setContentTitle("New Movie")
//                        .setContentText("개봉 예정인 영화가 있습니다.")
//                        .setAutoCancel(true);
//
//        Intent resultIntent = new Intent(requireActivity(), MainActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(requireActivity());
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.createNotificationChannel(new NotificationChannel("default", "기본채널",
//                NotificationManager.IMPORTANCE_DEFAULT));
//        mNotificationManager.notify(1, mBuilder.build());
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu item) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, item);
//        return true;
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:

                return true;
            case R.id.action_home:

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

    interface OnItemClickListener {
        void onItemClicked(Result item);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}







