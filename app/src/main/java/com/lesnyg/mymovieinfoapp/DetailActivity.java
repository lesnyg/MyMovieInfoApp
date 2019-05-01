package com.lesnyg.mymovieinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lesnyg.mymovieinfoapp.models.Result;
import com.lesnyg.mymovieinfoapp.ui.MovieDetailFragment;

public class DetailActivity extends AppCompatActivity {
    Result result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_detail, MovieDetailFragment.newInstance(result))
                .commit();
    }
}
