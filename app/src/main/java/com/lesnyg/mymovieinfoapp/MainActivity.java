package com.lesnyg.mymovieinfoapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lesnyg.mymovieinfoapp.models.Result;
import com.lesnyg.mymovieinfoapp.ui.FavoritesListFragment;
import com.lesnyg.mymovieinfoapp.ui.MovieGridFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, MovieGridFragment.newInstance())
                .commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main, MovieGridFragment.newInstance())
                                .commit();
                        return true;
                    case R.id.action_favorite:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main, FavoritesListFragment.newInstance())
                                .commit();
                        return true;
                }
                return false;
            }
        });



    }






}
