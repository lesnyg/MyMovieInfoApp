package com.lesnyg.movieinfoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    private int notiId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("text");
        String content = "오늘 " + text + "(이)가 개봉합니다";
        NotificationUtil.showNotification(context, content, notiId);
        notiId++;
    }


}
