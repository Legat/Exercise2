package com.example.peter.exercise2.Presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.peter.exercise2.Data.AboutData;
import com.example.peter.exercise2.R;
import com.example.peter.exercise2.View.IAboutView;

public class AboutPresenter implements IAboutPresenter {
    IAboutView aboutView;

    public AboutPresenter(IAboutView aboutView) {
        this.aboutView = aboutView;
    }

    @Override
    public void network(int idView) {
        AboutData data = new AboutData();
        Uri adress = Uri.parse(data.getTelegram());

    switch(idView){
        case 1:
          adress = Uri.parse(data.getFace());
            break;
        case 2:
           adress = Uri.parse(data.getVk());
            break;

    }

        Intent linkIntent = new Intent(Intent.ACTION_VIEW, adress);
        aboutView.networks(linkIntent);


    }

    @Override
    public void addView(Context context) {
        final TextView disclaimer = new TextView(context);
        disclaimer.setText(R.string.disclaimer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        disclaimer.setLayoutParams(params);
        aboutView.getView(disclaimer);
    }

    @Override
    public void sendMessage(String message, Context context) {
        AboutData data = new AboutData();
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if(data.checkIntent(intent,context)){
            aboutView.sendMessage(intent);
        } else{
            aboutView.sendMessageError(R.string.executing);
        }
    }
}
