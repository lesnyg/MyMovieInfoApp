package com.lesnyg.movieinfoapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lesnyg.movieinfoapp.ui.FavoritesListFragment;
import com.lesnyg.movieinfoapp.ui.MovieGridFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, new MovieGridFragment())
                .commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main,new MovieGridFragment())
                                .commit();
                        return true;
                    case R.id.action_favorite:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_main, new FavoritesListFragment())
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }
}
