package net.ddns.bivor.group14_hw1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    ToggleButton tb;
    TextView textViewAlcohol, textViewBACLevel, textViewStatus;
    SeekBar sb;
    RadioGroup rg;
    Button buttonSave, buttonAdd, buttonReset;
    EditText weight;
    ProgressBar pb;
    double alcoholValue = 0.05;
    double genderConstant = 0.55;
    int ounceValue = 1;
    int weightValue = -1;
    int seekbarValue = 5;
    double accumulatedValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("BAC Calculator");

        toolbar.setLogo(R.drawable.wine);

        buttonAdd = findViewById(R.id.buttonAddDrink);
        buttonSave = findViewById(R.id.buttonSave);
        buttonReset = findViewById(R.id.buttonReset);

        weight= findViewById(R.id.editText);


        tb = findViewById(R.id.toggleButtonGender);
        sb = findViewById(R.id.seekBar);
        pb = findViewById(R.id.progressBar);
        rg = findViewById(R.id.radioGroup);



        textViewAlcohol = findViewById(R.id.textViewAlcohol);
        textViewBACLevel = findViewById(R.id.textViewBACLevel);
        textViewStatus = findViewById(R.id.textViewStatus);



        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!isChecked)genderConstant = 0.55;
                    else genderConstant = 0.68;
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radioButton)ounceValue = 1;
                else if (checkedId == R.id.radioButton2)ounceValue = 5;
                else if (checkedId == R.id.radioButton3)ounceValue = 12;

//                ounceValue = Integer.parseInt(rb.getText().toString());
            }
        });

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alcoholValue = ((progress+1)*5)/100.0;            // as the initial value is 0 we are adding 1 to set the min. value to 1.
                textViewAlcohol.setText(""+((progress+1)*5)+" %");
                seekbarValue = ((progress+1)*5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewAlcohol.setText(""+seekbarValue+" %");
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(weight.getText().toString().isEmpty())weightValue= -1;
                else
                {
                    weightValue = Integer.parseInt(weight.getText().toString());

                    double BACLevel = Math.round(accumulatedValue/ (weightValue*genderConstant)*100.0)/100.0; //calculating the BACLevel and rounding it to 2 decimal places.

                    if(BACLevel <0.25){

                        textViewBACLevel.setText(""+BACLevel);
                        pb.setProgress((int)(BACLevel*100));

                        if(BACLevel<=0.08){
                            textViewStatus.setText("You're safe");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.green) );
                        }
                        else if (BACLevel>0.08 && BACLevel <0.20){
                            textViewStatus.setText("Be careful...");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.yellow) );
                        }
                        else{
                            textViewStatus.setText("Over the limit!");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.red) );
                        }

                    }
                    else {

                        textViewBACLevel.setText(""+0.25);
                        textViewStatus.setText("Over the limit!");
                        textViewStatus.setBackgroundColor(getResources().getColor(R.color.red) );
                        buttonAdd.setEnabled(false);
                        buttonSave.setEnabled(false);
                        pb.setProgress(25);
                        Toast.makeText(getApplicationContext(),"No more Drinks for you",Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //textViewStatus.setText(weight.getText().toString());
                if(weight.getText().toString().isEmpty()){
                    weight.setError("Enter the weight in lb.");
                }

                else if (weightValue == -1){
                    Toast.makeText(getApplicationContext(),"Click Save Button to save your weight to calculate BAC Level",Toast.LENGTH_LONG).show();
                }

                else{
                    weightValue = Integer.parseInt(weight.getText().toString());

                    accumulatedValue += ounceValue*alcoholValue*6.24;

                    double BACLevel = Math.round(accumulatedValue/ (weightValue*genderConstant)*100.0)/100.0; //calculating the BACLevel and rounding it to 2 decimal places.

                    if(BACLevel <0.25){

                        textViewBACLevel.setText(""+BACLevel);
                        pb.setProgress((int)(BACLevel*100));

                        if(BACLevel<=0.08){
                            textViewStatus.setText("You're safe");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.green) );
                        }
                        else if (BACLevel>0.08 && BACLevel <0.20){
                            textViewStatus.setText("Be careful...");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.yellow) );
                        }
                        else{
                            textViewStatus.setText("Over the limit!");
                            textViewStatus.setBackgroundColor(getResources().getColor(R.color.red) );
                        }

                    }
                    else {

                        accumulatedValue = 0;
                        textViewBACLevel.setText(""+0.25);
                        textViewStatus.setText("Over the limit!");
                        textViewStatus.setBackgroundColor(getResources().getColor(R.color.red) );
                        buttonAdd.setEnabled(false);
                        buttonSave.setEnabled(false);
                        pb.setProgress(25);
                        Toast.makeText(getApplicationContext(),"No more Drinks for you",Toast.LENGTH_SHORT).show();

                    }

                    //Toast.makeText(getApplicationContext(),"Drink Added",Toast.LENGTH_SHORT).show();

                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                rg.check(R.id.radioButton);

                sb.setProgress(0);
                textViewAlcohol.setText("5%");

                weightValue = -1;
                weight.setText("");
                alcoholValue = 0.05;
                accumulatedValue = 0;
                tb.setChecked(false);

                textViewBACLevel.setText(""+0.00);
                pb.setProgress(0);
                textViewStatus.setText("You're safe");
                textViewStatus.setBackgroundColor(getResources().getColor(R.color.green) );

                if(!buttonAdd.isEnabled())buttonAdd.setEnabled(true);
                if(!buttonSave.isEnabled())buttonSave.setEnabled(true);

            }
        });

    }


}
