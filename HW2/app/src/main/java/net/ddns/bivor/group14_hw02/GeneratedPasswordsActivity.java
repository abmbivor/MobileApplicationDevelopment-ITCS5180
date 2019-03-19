package net.ddns.bivor.group14_hw02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswordsActivity extends AppCompatActivity {

    Button buttonFinish;
    TextView textViewPassword, textViewPassword2;
    Passwords passwordThread, passwordAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        setTitle("Generated Passwords");

        buttonFinish = findViewById(R.id.buttonFinish);

        LinearLayout threadLayout = findViewById(R.id.threadLayout);
        LinearLayout asyncLayout = findViewById(R.id.asyncLayout);
        LayoutInflater inflaterThread = LayoutInflater.from(this);
        LayoutInflater inflaterAsync = LayoutInflater.from(this);

        if(getIntent()!=null && getIntent().getExtras() != null){
            passwordThread = (Passwords) getIntent().getExtras().getSerializable(MainActivity.PASSWORD_THREAD);
            passwordAsync = (Passwords) getIntent().getExtras().getSerializable(MainActivity.PASSWORD_ASYNC);

            int lengthOfArrayListThread = passwordThread.getPassword().size();

            for (int i=0; i<lengthOfArrayListThread; i++){

                View viewThread = inflaterThread.inflate(R.layout.password,threadLayout,false);
                textViewPassword = viewThread.findViewById(R.id.textViewPassword);
                textViewPassword.setText(passwordThread.getPassword().get(i));
                threadLayout.addView(viewThread);
            }

            int lengthOfArrayListAsync = passwordAsync.getPassword().size();

            for (int j=0; j<lengthOfArrayListAsync; j++){
                View viewAsync = inflaterAsync.inflate(R.layout.password2, asyncLayout, false);
                textViewPassword2 = viewAsync.findViewById(R.id.textViewPassword2);
                textViewPassword2.setText(passwordAsync.getPassword().get(j));
                asyncLayout.addView(viewAsync);
            }
        }


        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneratedPasswordsActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
