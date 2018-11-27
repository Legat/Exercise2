package com.example.peter.exercise2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StopReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // an Intent broadcast.

        context.stopService(new Intent(context,SupportService.class));
        Toast.makeText(context,"Updating is stopped",Toast.LENGTH_SHORT).show();
      //  throw new UnsupportedOperationException("Not yet implemented");
    }
}
