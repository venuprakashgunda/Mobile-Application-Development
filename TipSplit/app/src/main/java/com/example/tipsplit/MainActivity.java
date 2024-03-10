package com.example.tipsplit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private EditText EditText1;
    private EditText EditText2;
    private TextView TipAmount;
    private TextView TotalTip;
    private TextView TotalPerPerson;
    private TextView TextView;
    private double ActualBill; //actual bill
    private double NoOfPeople; // no of people
    private double Tip ; // only tip
    private double TipTotal; //total with tip
    private double Percent; // percentage
    private double p;  // Radio Button Value
    private double ex; // Temporary variable
    RadioGroup RadioGroup;
    String h = "$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText1 = findViewById(R.id.BillTotal);
        EditText2 = findViewById(R.id.NoOfPeople);
        TotalPerPerson = findViewById(R.id.TotalPerson);
        TipAmount = findViewById(R.id.TipAmount);
        TotalTip = findViewById(R.id.TotalTip);
        TextView = findViewById(R.id.textView);
        RadioGroup = findViewById(R.id.radioGroup);
    }

    public void radioClicked(View l) {
            p=0;
        if (l.getId() == R.id.radioButton) {
            p = 12.00;
        } else if (l.getId() == R.id.radioButton2) {
            p = 15.00;
        } else if (l.getId() == R.id.radioButton3) {
            p = 18.00;
        } else if (l.getId() == R.id.radioButton4) {
            p = 20.00;
        }
        String sInputField1 = EditText1.getText().toString();
        ActualBill = Double.parseDouble(sInputField1);
        ex = ActualBill * p;
        Tip = ex / 100;
        TipAmount.setText(String.format("%s %.2f", h,Tip)); // only tip
        TipTotal  = ActualBill+Tip;
        TotalTip.setText(String.format("%s %.2f", h,TipTotal)); // along with tip
    }
    public void GoClicked(View v) {
        String sInputField2 = EditText2.getText().toString();
        NoOfPeople = Double.parseDouble(sInputField2);
        Percent = TipTotal/NoOfPeople;
        TotalPerPerson.setText(String.format("%s %.2f", h,Percent)); // final p person
    }

   @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TotalTip", TotalTip.getText().toString());
        outState.putString("TipAmount", TipAmount.getText().toString());
        outState.putString("TotalPerPerson", TotalPerPerson.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);
        TotalPerPerson.setText(savedInstanceState.getString("TotalPerPerson"));
        TipAmount.setText(savedInstanceState.getString("TipAmount"));
        TotalTip.setText(savedInstanceState.getString("TotalTip"));
    }

    public void onClear(View v) {
        TotalPerPerson.setText("");
        TotalTip.setText("");
        TipAmount.setText("");
        EditText1.setText("");
        EditText2.setText("");
        RadioGroup.clearCheck();
   }
}