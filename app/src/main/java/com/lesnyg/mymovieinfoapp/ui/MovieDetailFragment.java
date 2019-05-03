package com.lesnyg.mymovieinfoapp.ui;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lesnyg.mymovieinfoapp.AlarmHATT;
import com.lesnyg.mymovieinfoapp.DetailActivity;
import com.lesnyg.mymovieinfoapp.MainActivity;
import com.lesnyg.mymovieinfoapp.MovieViewModel;
import com.lesnyg.mymovieinfoapp.R;
import com.lesnyg.mymovieinfoapp.adapter.MovieFavoriteAdapter;
import com.lesnyg.mymovieinfoapp.models.Result;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {

    private Result mResult ;
    private MovieViewModel mModel;
    RecyclerView mRecycler;
    MovieFavoriteAdapter mAdapter;

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
        Glide.with(getActivity())
                .load(posterPath)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImage);
        TextView titleText = view.findViewById(R.id.textView_title);
        titleText.setText(mResult.getTitle());
        TextView dateText = view.findViewById(R.id.textView_releasedate);
        dateText.setText("개봉일 : " + mResult.getRelease_date());
        TextView overViewText = view.findViewById(R.id.textView_overview);
        overViewText.setText(mResult.getOverview());

        mModel = ViewModelProviders.of(requireActivity())
                .get(MovieViewModel.class);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
        onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
//                new AlarmHATT(getView().getContext()).Alarm();
                noti();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPrepareOptionsMenu(Menu menu)
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdfNow.format(date);
        String upcommingDate = mResult.getRelease_date();

        MenuItem register = menu.findItem(R.id.action_noti);

        if(upcommingDate.compareTo(currentDate) < 0){
            register.setVisible(false);
        } else{
            register.setVisible(true);
        }
    }

    public void noti() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date strDate = sdf.parse(mResult.getRelease_date());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(requireActivity(), "default")
                        .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
                        .setContentTitle("New Movie")
                        .setContentText("오늘 개봉 예정인 영화가 있습니다.")
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(requireActivity(), DetailActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(requireActivity());

        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(new NotificationChannel("default", "기본채널",
                NotificationManager.IMPORTANCE_DEFAULT));
        mNotificationManager.notify(1, mBuilder.build());
    }

//    private Context context;
//    public AlarmHATT(Context context) {
//        this.context=context;
//    }
//    public void Alarm() {
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        //알람시간 calendar에 set해주기
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date strDate = sdf.parse(mResult.getRelease_date());
//
//        calendar.set(, calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 13, 0, 0);
//
//        //알람 예약
//        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//    }


}