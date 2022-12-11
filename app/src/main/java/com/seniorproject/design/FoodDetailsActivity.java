package com.seniorproject.design;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

public class FoodDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText nameOfFoodText;
    ImageButton saveDataBtn;
    TextView pageTitleTextView;
    String FoodName, FoodExpDate, FoodType, docID;
    boolean isEditMode = false;
    TextView deleteItemBtn;


    private Spinner spinnerFoodCatagory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        initDatePicker();
        dateButton = findViewById(R.id.selectDateBtn);
        dateButton.setText(getTodaysDate());
        nameOfFoodText = findViewById(R.id.nameOfFooodText);
        saveDataBtn = findViewById(R.id.SaveDataBtn);
        spinnerFoodCatagory = findViewById(R.id.spinnerFoodCatagory);
        deleteItemBtn = findViewById(R.id.deleteItemTextViewBtn);

        String[] catagories = getResources().getStringArray(R.array.catagoryPicker);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,catagories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodCatagory.setAdapter(adapter);
        saveDataBtn.setOnClickListener((v)-> saveData());
        pageTitleTextView = findViewById(R.id.pageTitle);

        FoodName = getIntent().getStringExtra("foodName");
        FoodExpDate = getIntent().getStringExtra("expDate");
        FoodType = getIntent().getStringExtra("foodType");
        docID = getIntent().getStringExtra("docID");

        if(docID!=null && !docID.isEmpty()){
            isEditMode = true;
        }

        nameOfFoodText.setText(FoodName);
        dateButton.setText(FoodExpDate);
        if(isEditMode){
            pageTitleTextView.setText("Edit the Item");
            deleteItemBtn.setVisibility(View.VISIBLE);
        }

        deleteItemBtn.setOnClickListener((v)-> deleteItemFromFirebase());

    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUNE";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEPT";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";
    }

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    void saveData(){
        String foodName = nameOfFoodText.getText().toString();
        String ExpDate = dateButton.getText().toString();
        if(nameOfFoodText==null || nameOfFoodText.toString().isEmpty()){
            nameOfFoodText.setError("You Must add an Item Name");
            return;
        }



        Foods foods = new Foods();
        foods.setFoodName(foodName);
        foods.setExpDate(ExpDate);
        foods.setTimestamp(Timestamp.now());

        saveFoodToFirebase(foods);

    }

    void saveFoodToFirebase(Foods foods){
        DocumentReference documentReference;
        if(isEditMode){
            documentReference = utility.getCollectionReferenceForFoodItems().document(docID);
        }else {

            documentReference = utility.getCollectionReferenceForFoodItems().document();
        }



        documentReference.set(foods).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //FOOD WAS ADDED TO DATABASE
                    utility.showToast(FoodDetailsActivity.this,"Food Item has been added");
                    finish();
                }else {
                   // FOOD NOT ADDED
                    utility.showToast(FoodDetailsActivity.this,"ERROR: Could Not Add Food Item");
                }
            }
        });
    }

    void deleteItemFromFirebase(){
        DocumentReference documentReference;

            documentReference = utility.getCollectionReferenceForFoodItems().document(docID);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    utility.showToast(FoodDetailsActivity.this,"Food Item has been deleted");
                    finish();
                }else {

                    utility.showToast(FoodDetailsActivity.this,"ERROR: Could Not delete Food Item");
                }
            }
        });
    }


    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(getParent().getTaskId() == R.id.spinnerFoodCatagory){

        }
   }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}