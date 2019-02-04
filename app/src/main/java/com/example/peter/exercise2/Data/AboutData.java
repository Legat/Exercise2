package com.example.peter.exercise2.Data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AboutData implements IAboutData {
    private static final String FACE_BOOK = "https://www.facebook.com/peter.shnepelev";
    private static final String VK_COM = "https://vk.com/patricul";
    private static final String TELEGRAM = "https://web.telegram.org/#/im?p=@Patricul";

    @Override
    public boolean checkIntent(Intent intent, Context context) {
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) == null){
            return false;
        } else{
            return true;
        }
    }

    @Override
    public String getFace() {
        return FACE_BOOK;
    }

    @Override
    public String getVk() {
        return VK_COM;
    }

    @Override
    public String getTelegram() {
        return TELEGRAM;
    }
}
