package net.ddns.bivor.group14_inclass03;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, studentID;
    String department;
    RadioGroup rg;
    RadioButton rb;
    public static final int REQ_CODE = 100;
    public static final int REQ_CODE2 = 101;
    public static final String AVATAR_KEY = "AVATAR_KEY";
    public static final String STUDENT_KEY = "STUDENT_KEY";
    int avatar = R.drawable.select_image;
    ImageView iv;

    int rbID;
    public  static final int CHECKED_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("My Profile");

        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        studentID = findViewById(R.id.editTextStudentID);

        iv = findViewById(R.id.imageViewMain);

        rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == -1){

                }
                else {
                    rb = findViewById(checkedId);
                    department = rb.getText().toString();
                    rbID = checkedId;
                }

            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectAvatar.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstName.getText().toString().isEmpty()){
                    firstName.setError("Enter your First Name!");
                }
                else if (lastName.getText().toString().isEmpty()){
                    lastName.setError("Enter your Last Name!");
                }
                else if (studentID.getText().toString().isEmpty()){
                    studentID.setError("Enter your Student ID!");
                }
                else if (studentID.getText().toString().length()!=9)
                {
                    studentID.setError("Student ID should be of 9 Digits");
                }
                else if (department== null){

                    Toast.makeText(MainActivity.this, "Select a Department!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String first = firstName.getText().toString();
                    String last = lastName.getText().toString();
                    String studentNo = studentID.getText().toString();
                    String avatarID = Integer.toString(avatar);

                    Intent intent = new Intent(MainActivity.this, Display.class);
                    intent.putExtra(STUDENT_KEY, new Student(avatarID, first, last, studentNo,department, rbID));
                    startActivityForResult(intent, REQ_CODE2 );
                }


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
                   avatar = Integer.parseInt(data.getExtras().getString(AVATAR_KEY));
                   iv.setImageResource(avatar);
                   Log.d("demo", "Value of avatar : "+avatar);
            }
        }
        else if (requestCode == REQ_CODE2){
            if(resultCode == RESULT_OK){

                Student student = (Student) data.getExtras().getSerializable(STUDENT_KEY);

                firstName.setText(student.firstName);
                lastName.setText(student.lastName);
                studentID.setText(student.studentID);

                iv.setImageResource(Integer.parseInt(student.avatar));
                rg.check(student.rbID);

            }
        }
    }


}
