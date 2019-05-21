package com.lesnyg.movieinfoapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lesnyg.movieinfoapp.models.Result;
import com.lesnyg.movieinfoapp.ui.MovieDetailFragment;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Result result = (Result) getIntent().getSerializableExtra("result");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail, MovieDetailFragment.newInstance(result))
                .commit();
    }


}
