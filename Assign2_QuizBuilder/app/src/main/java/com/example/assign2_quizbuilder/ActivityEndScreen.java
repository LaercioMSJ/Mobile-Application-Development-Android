package com.example.assign2_quizbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityEndScreen extends AppCompatActivity {

    //controls
    TextView tvCongratulations;
    TextView tvFinalMessage;
    Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endscreen);

        tvCongratulations = findViewById(R.id.tvCongratulations);
        tvFinalMessage = findViewById(R.id.tvFinalMessage);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        Bundle bundle = getIntent().getExtras();

        String name = "";
        int correctAnswers = 0;
        int totalQuestions = 0;

        if (bundle != null) {
            if (!bundle.getString("NAME").equals("")) {
                name = bundle.getString("NAME");
            }
            correctAnswers = bundle.getInt("POINTS");
            totalQuestions = bundle.getInt("TOTAL");
        }

        tvCongratulations.setText(String.format(getResources().getString(R.string.congratulations), name, correctAnswers, totalQuestions));

        float ratio = (float) correctAnswers/totalQuestions;
        String finalMessage;

        if (correctAnswers == 0) {
            finalMessage = getResources().getString(R.string.finalMessageOne);
        }
        else if (ratio < 0.6 ) {
            finalMessage = getResources().getString(R.string.finalMessageTwo);
        }
        else if (ratio < 0.9 ) {
            finalMessage = getResources().getString(R.string.finalMessageThree);
        }
        else {
            finalMessage = getResources().getString(R.string.finalMessageFour);
        }

        tvFinalMessage.setText(finalMessage);

        //btnPlayAgain button listener class
        btnPlayAgain.setOnClickListener(view -> {
            Intent iStartScreen = new Intent(ActivityEndScreen.this, MainActivity.class);
            startActivity(iStartScreen);
        });//end btnPlayAgain button listener class
    }
}
