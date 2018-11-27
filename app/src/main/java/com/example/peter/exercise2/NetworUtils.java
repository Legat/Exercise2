package com.example.peter.exercise2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworUtils {
    private static NetworUtils sNetworUtils = new NetworUtils();




    private NetworkReceiver mNetworkReceiver = new NetworkReceiver();

    public BroadcastReceiver getNetworkReceiver() {
        return mNetworkReceiver;
    }

    private NetworUtils() {
    }

    public static class NetworkReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
             String inform = "Enable internet connection";
             Toast.makeText(context,inform,Toast.LENGTH_LONG).show();
        }
    }
}
