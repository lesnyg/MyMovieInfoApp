package com.lesnyg.movieinfoapp.ui;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.lesnyg.movieinfoapp.AlarmReceiver;
import com.lesnyg.movieinfoapp.CounterViewModel;
import com.lesnyg.movieinfoapp.MovieViewModel;
import com.lesnyg.movieinfoapp.R;
import com.lesnyg.movieinfoapp.TheaterDialogActivity;
import com.lesnyg.movieinfoapp.adapter.CommentAdapter;
import com.lesnyg.movieinfoapp.databinding.FragmentMovieDetailBinding;
import com.lesnyg.movieinfoapp.models.Comment;
import com.lesnyg.movieinfoapp.models.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;


public class MovieDetailFragment extends Fragment {

    private Result mResult;
    private MovieViewModel mModel;
    private FragmentMovieDetailBinding mBinding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private CommentAdapter mAdapter;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    public static MovieDetailFragment newInstance(Result result) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("filteredResult", result);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResult = (Result) getArguments().getSerializable("filteredResult");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mBinding = FragmentMovieDetailBinding.bind(view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String posterPath = "https://image.tmdb.org/t/p/w500" + mResult.getPoster_path();

        ImageView posterImage = view.findViewById((R.id.imageView_poster));
        Glide.with(getActivity())
                .load(posterPath)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImage);
        mBinding.setResult(mResult);
        mModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        CounterViewModel viewModel = ViewModelProviders.of(this).get(CounterViewModel.class);

        viewModel.goodcounter.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mBinding.setViewModel(viewModel);
            }
        });
        mBinding.setLifecycleOwner(this);

        Query query = FirebaseFirestore.getInstance()
                .collection("comment")
                .whereEqualTo("movieId",mResult.getId());

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();



        RecyclerView recyclerView = view.findViewById(R.id.comment_recycler);
        mAdapter = new CommentAdapter(options);
        recyclerView.setAdapter(mAdapter);

        mBinding.btnCommentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("comment", mBinding.editComment.getText().toString());
                user.put("movieId", mResult.getId());

                db.collection("comment").add(user);
            }
        });


        view.findViewById(R.id.booking_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), TheaterDialogActivity.class);
                startActivity(intent);
            }
        });





    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
        onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                mModel.addFavorite(mResult);
                return true;
            case R.id.action_sharing:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = "원하는 텍스트를 입력하세요";
                intent.putExtra(Intent.EXTRA_TEXT, mResult.getTitle());
                Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                startActivity(chooser);
                return true;
            case R.id.action_noti:
                alarm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdfNow.format(date);
        String upcommingDate = mResult.getRelease_date();

        MenuItem register = menu.findItem(R.id.action_noti);

        if (upcommingDate.compareTo(currentDate) < 0) {
            register.setVisible(false);
        } else {
            register.setVisible(true);
        }
    }


    private void alarm() {
        AlarmManager alarm_manager = (AlarmManager) requireContext().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), AlarmReceiver.class);
        intent.putExtra("text", mResult.getTitle());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, 0);

        alarm_manager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 10000, pendingIntent); //10초후 알림
    }
}