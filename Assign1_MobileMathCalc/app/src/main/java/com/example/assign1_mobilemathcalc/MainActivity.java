package com.example.assign1_mobilemathcalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    mathOperationsClass moc = new mathOperationsClass();

    private double firstValue = Double.NaN;
    private double secondValue = Double.NaN;
    private String mathOperator = "";
    private boolean isBtnDecimalClicked = false;
    private boolean isResultInDisplay = false;

    private TextView tvBottomDisplay;
    private TextView tvTopDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNegPos = (Button) findViewById(R.id.btnNegPos);
        Button btnZero = (Button) findViewById(R.id.btnZero);
        Button btnDecimal = (Button) findViewById(R.id.btnDecimal);
        Button btnCalculate = (Button) findViewById(R.id.btnCalculate);
        Button btnOne = (Button) findViewById(R.id.btnOne);
        Button btnTwo = (Button) findViewById(R.id.btnTwo);
        Button btnThree = (Button) findViewById(R.id.btnThree);
        Button btnAddition = (Button) findViewById(R.id.btnAddition);
        Button btnFour = (Button) findViewById(R.id.btnFour);
        Button btnFive = (Button) findViewById(R.id.btnFive);
        Button btnSix = (Button) findViewById(R.id.btnSix);
        Button btnSubtraction = (Button) findViewById(R.id.btnSubtraction);
        Button btnSeven = (Button) findViewById(R.id.btnSeven);
        Button btnEight = (Button) findViewById(R.id.btnEight);
        Button btnNine = (Button) findViewById(R.id.btnNine);
        Button btnMultiplication = (Button) findViewById(R.id.btnMultiplication);
        Button btnCE = (Button) findViewById(R.id.btnCE);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        Button btnRemoveDigit = (Button) findViewById(R.id.btnRemoveDigit);
        Button btnDivision = (Button) findViewById(R.id.btnDivision);
        tvBottomDisplay = (TextView)findViewById(R.id.tvBottomDisplay);
        tvTopDisplay = (TextView)findViewById(R.id.tvTopDisplay);

        //set all numeric buttons for the same listener class
        btnZero.setOnClickListener(onNumericButtonClicked);
        btnOne.setOnClickListener(onNumericButtonClicked);
        btnTwo.setOnClickListener(onNumericButtonClicked);
        btnThree.setOnClickListener(onNumericButtonClicked);
        btnFour.setOnClickListener(onNumericButtonClicked);
        btnFive.setOnClickListener(onNumericButtonClicked);
        btnSix.setOnClickListener(onNumericButtonClicked);
        btnSeven.setOnClickListener(onNumericButtonClicked);
        btnEight.setOnClickListener(onNumericButtonClicked);
        btnNine.setOnClickListener(onNumericButtonClicked);

        //set all math operation buttons for the same listener class
        btnCalculate.setOnClickListener(onOperationButtonClicked);
        btnAddition.setOnClickListener(onOperationButtonClicked);
        btnSubtraction.setOnClickListener(onOperationButtonClicked);
        btnMultiplication.setOnClickListener(onOperationButtonClicked);
        btnDivision.setOnClickListener(onOperationButtonClicked);

        //btnNegPos button listener class
        btnNegPos.setOnClickListener(view -> {
            double negPosValue;
            String numberInTvText = tvBottomDisplay.getText().toString();

            negPosValue = moc.negPos(Double.parseDouble(numberInTvText));

            if (negPosValue < 0) {
                numberInTvText = "-" + numberInTvText;
                tvBottomDisplay.setText(numberInTvText);
            }
            else if (negPosValue > 0) {
                tvBottomDisplay.setText(numberInTvText.substring(1));
            }
            else {
                tvBottomDisplay.setText(numberInTvText);
            }
        });//end btnNegPos button listener class

        //btnDecimal button listener class
        btnDecimal.setOnClickListener(view -> {
            boolean isDecimal;
            String numberInTvText = tvBottomDisplay.getText().toString();

            isDecimal = moc.decimal(Double.parseDouble(numberInTvText));

            if (!isDecimal && !isBtnDecimalClicked && !isResultInDisplay) {
                tvBottomDisplay.setText(String.format("%s%s", numberInTvText, "."));
                isBtnDecimalClicked = true;
            }
        });//end btnDecimal button listener class

        //btnCE button listener class
        btnCE.setOnClickListener(view -> {
            tvBottomDisplay.setText("0");
            isBtnDecimalClicked = false;
            isResultInDisplay = false;
        });//end btnCE button listener class

        //btnClear button listener class
        btnClear.setOnClickListener(view -> {
            tvTopDisplay.setText("");
            tvBottomDisplay.setText("0");
            firstValue = Double.NaN;
            secondValue = Double.NaN;
            mathOperator = "";
            isBtnDecimalClicked = false;
            isResultInDisplay = false;
        });//end btnClear button listener class

        //btnRemoveDigit button listener class
        btnRemoveDigit.setOnClickListener(view -> {
            String numberInTvText = tvBottomDisplay.getText().toString();
            if (numberInTvText.length() > 1 && !isResultInDisplay) {
                tvBottomDisplay.setText(numberInTvText.substring(0, numberInTvText.length() - 1));
            }
            else if (!isResultInDisplay) {
                tvBottomDisplay.setText("0");
                isBtnDecimalClicked = false;
            }

            if (numberInTvText.indexOf(".") == (numberInTvText.length()-1)) {
                isBtnDecimalClicked = false;
            }
        });//end btnRemoveDigit button listener class

    }//end onCreate

    public View.OnClickListener onNumericButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button btnNumber = (Button)view;
            String numberInBtnText = btnNumber.getText().toString();
            String numberInTvText = tvBottomDisplay.getText().toString();

            if ((numberInTvText.equals("0")) || (isResultInDisplay)) {
                tvBottomDisplay.setText(numberInBtnText);
                isResultInDisplay = false;
            }
            else {
                tvBottomDisplay.setText(String.format("%s%s", numberInTvText, numberInBtnText));
            }
        }//end method onClick
    };//end inner class

    public View.OnClickListener onOperationButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Button btnOperator = (Button)view;
            String operatorInBtnText = btnOperator.getText().toString();

            if ((Double.isNaN(firstValue)) && (!operatorInBtnText.equals("="))) {
                firstValue = Double.parseDouble(tvBottomDisplay.getText().toString());
                mathOperator = operatorInBtnText;

                if (firstValue % 1 == 0) {
                    tvTopDisplay.setText(String.format("%s %s", String.valueOf((int) firstValue), mathOperator));
                }
                else {
                    tvTopDisplay.setText(String.format("%s %s", tvBottomDisplay.getText().toString(), mathOperator));
                }

                isResultInDisplay = true;
                isBtnDecimalClicked = false;
            }
            else if (!Double.isNaN(firstValue)) {
                secondValue = Double.parseDouble(tvBottomDisplay.getText().toString());
                tvBottomDisplay.setText(moc.calculate(firstValue, mathOperator, secondValue));
                tvTopDisplay.setText("");
                firstValue = Double.NaN;
                secondValue = Double.NaN;
                mathOperator = "";
                isResultInDisplay = true;
                isBtnDecimalClicked = false;
            }

        }//end method onClick
    };//end inner class

}//end class