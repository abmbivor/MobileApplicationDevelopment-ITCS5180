package net.ddns.bivor.group14_inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SelectAvatar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        setTitle("Select Avatar");

        ImageView f1 = (ImageView) findViewById(R.id.f1);
        ImageView m1 = (ImageView) findViewById(R.id.m1);
        ImageView f2 = (ImageView) findViewById(R.id.f2);
        ImageView m2 = (ImageView) findViewById(R.id.m2);
        ImageView f3 = (ImageView) findViewById(R.id.f3);
        ImageView m3 = (ImageView) findViewById(R.id.m3);

        findViewById(R.id.f1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_f_1;
                String key = avatar.toString();
                if(avatar == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        findViewById(R.id.f2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_f_2;
                String key = avatar.toString();
                if(key  == null){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        findViewById(R.id.f3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_f_3;
                String key = avatar.toString();
                if(key  == null){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        findViewById(R.id.m1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_m_1;
                String key = avatar.toString();
                if(key  == null){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        findViewById(R.id.m2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_m_2;
                String key = avatar.toString();
                if(key  == null){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

        findViewById(R.id.m3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer avatar = R.drawable.avatar_m_3;
                String key = avatar.toString();
                if(key  == null){
                    setResult(RESULT_CANCELED);
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.AVATAR_KEY, key);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }
}
