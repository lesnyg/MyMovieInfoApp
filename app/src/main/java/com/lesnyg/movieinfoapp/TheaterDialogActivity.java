package com.lesnyg.movieinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TheaterDialogActivity extends AppCompatActivity implements OnClickListener {

    private String mTheaterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_dialog);

        findViewById(R.id.cgv_logo).setOnClickListener(this);
        findViewById(R.id.lotte_logo).setOnClickListener(this);
        findViewById(R.id.megabox_logo).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebViewActivity.class);
        switch (v.getId()) {
            case R.id.cgv_logo:
                mTheaterUrl = "http://m.cgv.co.kr/";
            break;
            case R.id.lotte_logo:
                mTheaterUrl = "http://m.lottecinema.co.kr/";
            break;
            case R.id.megabox_logo:
                mTheaterUrl = "http://m.megabox.co.kr/";
            break;


        }
        intent.putExtra("cgv",mTheaterUrl);
        startActivity(intent);
        finish();
    }
}
