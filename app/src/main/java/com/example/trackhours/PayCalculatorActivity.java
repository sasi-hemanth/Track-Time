package com.example.trackhours;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PayCalculatorActivity extends AppCompatActivity {

    EditText editTextHoursWorked, editTextPayRate, editTextTaxPercentage;
    Button buttonCalculate, buttonBack;
    TextView textViewGrossPay, textViewNetPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_calculator);

        editTextHoursWorked = findViewById(R.id.editText_hours_worked);
        editTextPayRate = findViewById(R.id.editText_pay_rate);
        editTextTaxPercentage = findViewById(R.id.editText_tax_percentage);
        buttonCalculate = findViewById(R.id.button_calculate);
        buttonBack = findViewById(R.id.button_back);
        textViewGrossPay = findViewById(R.id.textView_gross_pay);
        textViewNetPay = findViewById(R.id.textView_net_pay);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePay();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calculatePay() {
        String hoursWorkedStr = editTextHoursWorked.getText().toString();
        String payRateStr = editTextPayRate.getText().toString();
        String taxPercentageStr = editTextTaxPercentage.getText().toString();

        if (hoursWorkedStr.isEmpty() || payRateStr.isEmpty() || taxPercentageStr.isEmpty()) {
            return;
        }

        double hoursWorked = Double.parseDouble(hoursWorkedStr);
        double payRate = Double.parseDouble(payRateStr);
        double taxPercentage = Double.parseDouble(taxPercentageStr);


        double grossPay = hoursWorked * payRate;
        double taxAmount = (grossPay * taxPercentage) / 100;
        double netPay = grossPay - taxAmount;


        textViewGrossPay.setText("Gross Pay: $" + String.format("%.2f", grossPay));
        textViewNetPay.setText("Net Pay: $" + String.format("%.2f", netPay));
    }
}
