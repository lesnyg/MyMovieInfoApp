package com.lesnyg.mymovieinfoapp.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.lesnyg.mymovieinfoapp.adapter.MovieFavoriteAdapter;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.models.Result;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Result mResult ;
    private MovieViewModel mModel;

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
//        FragmentMovieDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false);
//        binding.imageView.setImageResource(mResult.getPoster_path());
//        binding.textViewTitle.setText(mResult.getTitle());
//        binding.textViewReleasedate.setText(mResult.getRelease_date());
//        binding.textViewOverview.setText(mResult.getOverview());
//        return binding.getRoot();
        View view;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_movie_detail2, container, false);
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String posterPath = "https://image.tmdb.org/t/p/w500" + mResult.getPoster_path();

        ImageView posterImage = view.findViewById((R.id.imageView));
//        posterImage.setImageResource();
        Glide.with(getActivity())
                .load(posterPath)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImage);
        TextView titleText = view.findViewById(R.id.textView_title);
        titleText.setText(mResult.getTitle());
        TextView dateText = view.findViewById(R.id.textView_releasedate);
        dateText.setText("상영일 : " + mResult.getRelease_date());
        TextView overViewText = view.findViewById(R.id.textView_overview);
        overViewText.setText(mResult.getOverview());

        mModel = ViewModelProviders.of(requireActivity())
                .get(MovieViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                MovieFavoriteAdapter adapter = new MovieFavoriteAdapter();
                mModel.addFavorit(mResult);
                return true;
            case R.id.action_sharing:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
//                        intent.setType("image/*");
                String text = "원하는 텍스트를 입력하세요";
//                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+posterPath));
                intent.putExtra(Intent.EXTRA_TEXT, mResult.getTitle());
                Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
                startActivity(chooser);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    interface OnSelectedMovieListener {
        String onSelectedMovie();
    }

    private OnSelectedMovieListener mListener;

    public void setOnSelectedMovieListener(OnSelectedMovieListener listener) {
        mListener = listener;
    }
}