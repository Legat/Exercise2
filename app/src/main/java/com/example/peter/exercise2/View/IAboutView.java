package com.example.peter.exercise2.View;

import android.content.Intent;
import android.widget.TextView;

public interface IAboutView {
    void sendMessage(Intent intent);
    void networks(Intent intent);
    void getView(final TextView disclaimer);
    void sendMessageError(int message);
}
