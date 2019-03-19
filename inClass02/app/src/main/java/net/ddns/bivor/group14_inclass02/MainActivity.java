package net.ddns.bivor.group14_inclass02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextWeight,editTextHeight1,getEditTextHeight2;
    TextView textViewBMI, textViewStatus;
    Button buttonCalculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight1 = findViewById(R.id.editTextHeight1);
        getEditTextHeight2 = findViewById(R.id.editTextHeight2);
        textViewBMI = findViewById(R.id.textViewBMI);
        textViewStatus = findViewById(R.id.textViewStatus);

        buttonCalculate = findViewById(R.id.buttonCalculate);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String weight = editTextWeight.getText().toString();
                String height1 = editTextHeight1.getText().toString();
                String height2 = getEditTextHeight2.getText().toString();

                if(weight.equals("")){
                    editTextWeight.setError("Fill it up!");
                }
                else if(height1.equals("")){
                    editTextHeight1.setError("Fill it up!");
                }
                else if(height2.equals("")){
                    getEditTextHeight2.setError("Fill it up!");
                }
                else if(Float.parseFloat(height2)>=12){
                    getEditTextHeight2.setError("Can not be 12 or more!");
                }
                else {
                    float weightVal = Float.parseFloat(weight);
                    float height1Val = Float.parseFloat(height1);
                    float height2Val = Float.parseFloat(height2);

                    float heightVal = (height1Val*12)+ height2Val;

                    float bmi = (float) ((weightVal/Math.pow(heightVal,2))*703);

                    textViewBMI.setText(bmi+"");

                    if(bmi< 18.5){
                        textViewStatus.setText("Underweight");
                    }
                    else if (bmi>=18.5 && bmi<25){
                        textViewStatus.setText("Normal Weight");
                    }
                    else if (bmi>=25 && bmi<30){
                        textViewStatus.setText("Overweight");
                    }
                    else{
                        textViewStatus.setText("Obese");
                    }

                    Toast.makeText(getApplicationContext(),"BMI Calculated",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
