package com.example.peter.exercise2.Data;

import android.content.Context;
import android.content.Intent;

public interface IAboutData {
    boolean checkIntent(Intent intent, Context context);
    String getFace();
    String getVk();
    String getTelegram();
}
