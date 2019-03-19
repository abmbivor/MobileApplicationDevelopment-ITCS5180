package net.ddns.bivor.group14_inclass03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Display extends AppCompatActivity {

    TextView textViewName, textViewStudentID, textViewDepartment;
    ImageView imageViewDisplay;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        setTitle("Display My Profile");

        textViewName = findViewById(R.id.textViewDisplayName);
        textViewStudentID = findViewById(R.id.textViewDisplayStudentID);
        textViewDepartment = findViewById(R.id.textViewDisplayDepartment);

        imageViewDisplay = findViewById(R.id.imageViewDisplay);

        if (getIntent()!= null && getIntent().getExtras()!= null){

            student = (Student) getIntent().getExtras().getSerializable(MainActivity.STUDENT_KEY);

            textViewName.setText(student.firstName +" " + student.lastName);
            textViewStudentID.setText(student.studentID);
            textViewDepartment.setText(student.department);
            imageViewDisplay.setImageResource(Integer.parseInt(student.avatar));

        }

        findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.STUDENT_KEY, student);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
