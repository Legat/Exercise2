package com.example.peter.exercise2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
   private static final String FACE_BOOK = "https://www.facebook.com/peter.shnepelev";
   private static final String VK_COM = "https://vk.com/patricul";
   private static final String TELEGRAM = "https://web.telegram.org/#/im?p=@Patricul";
    private Button sendBtn;
    private ImageButton faceBtn, telBtn, vkBtn;
    private EditText messageEdit;
    private LinearLayout conainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        messageEdit = findViewById(R.id.message_edit);

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(this);
//            @Override
//            public void onClick(View view) {
//                sendMessage();
//            }
//        });

       /// code revew
        faceBtn = findViewById(R.id.face_btn);
        telBtn = findViewById(R.id.teleg_btn);
        vkBtn = findViewById(R.id.vk_btn);

        faceBtn.setOnClickListener(this);
        telBtn.setOnClickListener(this);
        vkBtn.setOnClickListener(this);

        conainer = findViewById(R.id.info_container);
        TextView disclaimer = new TextView(this);
        disclaimer.setText(R.string.disclaimer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        disclaimer.setLayoutParams(params);
        conainer.addView(disclaimer);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.face_btn:
                networks(FACE_BOOK);
                break;
            case R.id.teleg_btn:
                networks(TELEGRAM);
                break;
            case R.id.vk_btn:
                networks(VK_COM);
                break;
            case R.id.send_btn:
                sendMessage();
        }
    }
    private void networks(String reference){
        Uri address = Uri.parse(reference);
        Intent linkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(linkIntent);
    }

    private boolean checkIntent(Intent intent){
        PackageManager packageManager = getPackageManager();
        if(packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) == null){
            return false;
        } else{
            return true;
        }
    }

    private void sendMessage(){
        String message = messageEdit.getText().toString();
        Intent intent = new Intent();
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if(checkIntent(intent))
            startActivity(intent);
        else
            Toast.makeText(this, R.string.executing, Toast.LENGTH_LONG).show();
    }
}
