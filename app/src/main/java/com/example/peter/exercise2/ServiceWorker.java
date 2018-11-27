package com.example.peter.exercise2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import androidx.work.Worker;

public class ServiceWorker extends Worker {
    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent(getApplicationContext(),SupportService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        } else{
            getApplicationContext().startService(intent);
        }


        return Result.SUCCESS;
    }

    @Override
    public void onStopped(boolean cancelled) {
        super.onStopped(cancelled);
    }
}
