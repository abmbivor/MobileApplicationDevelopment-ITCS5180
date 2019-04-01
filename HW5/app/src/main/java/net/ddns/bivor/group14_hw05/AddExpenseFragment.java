package net.ddns.bivor.group14_hw05;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class AddExpenseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    EditText editTextAddExpenseName, editTextAddExpenseCost;
    TextView textViewAddExpenseDate;
    Button buttonPickDate, buttonChooseImage, buttonAddExpense;
    ImageView imageViewReceipt;
    ProgressBar pb;
    Expense expense;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private int PICK_IMAGE_REQUEST = 1;
    private int CAPTURE_IMAGE_REQUEST = 2;
    public static final int REQUEST_CODE = 10;
    public  static final int MY_PERMISSIONS_REQUEST = 11;
    public AddExpenseFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        editTextAddExpenseName = rootView.findViewById(R.id.editTextAddExpenseName);
        editTextAddExpenseCost = rootView.findViewById(R.id.editTextAddExpenseCost);
        textViewAddExpenseDate = rootView.findViewById(R.id.textViewAddExpenseDate);
        buttonPickDate = rootView.findViewById(R.id.buttonPickDate);
        buttonChooseImage = rootView.findViewById(R.id.buttonChooseImage);
        buttonAddExpense = rootView.findViewById(R.id.buttonAddExpense);
        imageViewReceipt = rootView.findViewById(R.id.imageViewReceipt);
        pb = rootView.findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        expense = new Expense();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddExpenseFragment.this, REQUEST_CODE);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
                pictureDialog.setTitle("Select Action");
                String[] pictureDialogItems = {
                        "Select photo from gallery",
                        "Capture photo from camera" };
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent();
                                        // Show only images, no videos or anything else
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        //intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        // Always show the chooser (if there are multiple options available)
                                        startActivityForResult(Intent.createChooser(intent, "Select a Picture"), PICK_IMAGE_REQUEST);
                                        break;
                                    case 1:
                                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                                == PackageManager.PERMISSION_DENIED)
                                        {
                                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST );
                                        }
                                        else {
                                            Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            startActivityForResult(intent2, CAPTURE_IMAGE_REQUEST);
                                        }
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
            }
        });

        buttonAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextAddExpenseName.getText().toString().isEmpty()){
                    editTextAddExpenseName.setError("Enter Expense Name");
                    Toast.makeText(getActivity(), "Enter Expense Name", Toast.LENGTH_SHORT).show();
                }
                else if(editTextAddExpenseCost.getText().toString().isEmpty()){
                    editTextAddExpenseCost.setError("Enter Expense Name");
                    Toast.makeText(getActivity(), "Enter Expense Amount", Toast.LENGTH_SHORT).show();
                }
                else if (textViewAddExpenseDate.getText().toString().equals("Select a Date")){
                    Toast.makeText(getActivity(), "Select a Date", Toast.LENGTH_SHORT).show();
                }
                else{

                    pb.setVisibility(View.VISIBLE);
                    buttonPickDate.setEnabled(false);
                    buttonChooseImage.setEnabled(false);
                    buttonAddExpense.setEnabled(false);

                    imageViewReceipt.setDrawingCacheEnabled(true);
                    imageViewReceipt.buildDrawingCache();
                    Bitmap bitmap = imageViewReceipt.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    imageViewReceipt.setDrawingCacheEnabled(false);
                    byte[] data = baos.toByteArray();

                    String path ="receipts/" + UUID.randomUUID() + ".png";
                    final StorageReference receiptRef = storage.getReference(path);

                    UploadTask uploadTask = receiptRef.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pb.setVisibility(View.INVISIBLE);
                            buttonPickDate.setEnabled(true);
                            buttonChooseImage.setEnabled(true);
                            buttonAddExpense.setEnabled(true);

                            receiptRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    expense.imageURL = ""+uri;
                                    expense.name = editTextAddExpenseName.getText().toString();
                                    expense.cost = editTextAddExpenseCost.getText().toString();
                                    expense.datePicked = textViewAddExpenseDate.getText().toString();


                                    mListener.goToExpenseOnAdd(expense);
                                }
                            });
                        }
                    });


                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, CAPTURE_IMAGE_REQUEST);
                } else {
                    Toast.makeText(getActivity(), "Camera Permission Not Given", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageViewReceipt.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageViewReceipt.setImageBitmap(thumbnail);

        }
        else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            String dateExtra = data.getStringExtra("pickedDate");
            Date date1 = null;
            try {
                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateExtra);
                Log.d("demo", date1.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            expense.date = date1;
            textViewAddExpenseDate.setText(data.getStringExtra("selectedDate"));
            expense.datePicked = data.getStringExtra("selectedDate");

        }
        else {
            String s = "";
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void goToExpenseOnAdd(Expense expense);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date = ""+MONTHS[month]+" "+day+", "+year;
            String datePicked = day+"/"+(month+1)+"/"+year;
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra("selectedDate", date).putExtra("pickedDate", datePicked)
            );
        }
    }

}
