package com.example.assign2_quizbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.*;

public class ActivityQuiz extends AppCompatActivity {

    //controls
    TextView tvTotalQuestions;
    TextView tvLblCorrectAnswer;
    TextView tvCorrectAnswer;
    Button btnRestart;
    TextView tvDefinition;
    RadioGroup radioTermGroup;
    RadioButton radioTermOne;
    RadioButton radioTermTwo;
    RadioButton radioTermThree;
    RadioButton radioTermFour;
    Button btnConfirm;

    ArrayList<String[]> termsDefinitionsArrayList = new ArrayList<>();
    Map<String,String> termsAndDefinitionsHashMap = new HashMap<>();
    int totalQuestions = 0;
    int currentQuestion = 0;
    int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvLblCorrectAnswer = findViewById(R.id.tvLblCorrectAnswer);
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        btnRestart = findViewById(R.id.btnRestart);
        tvDefinition = findViewById(R.id.tvDefinition);
        radioTermGroup = findViewById(R.id.radioTermGroup);
        radioTermOne = findViewById(R.id.radioTermOne);
        radioTermTwo = findViewById(R.id.radioTermTwo);
        radioTermThree = findViewById(R.id.radioTermThree);
        radioTermFour = findViewById(R.id.radioTermFour);
        btnConfirm = findViewById(R.id.btnConfirm);

        readInternalRaw();
        shuffleArrayList();
        displayQuestionAndOptions();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.getString("NAME").equals("")) {
            String name = bundle.getString("NAME");
            tvLblCorrectAnswer.setText(String.format(getResources().getString(R.string.correctAnswer), name));
        }

        btnRestart.setOnClickListener(view -> {
            Intent iMainScreen = new Intent(ActivityQuiz.this, MainActivity.class);
            startActivity(iMainScreen);
        });

        radioTermGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> btnConfirm.setEnabled(true));

        btnConfirm.setOnClickListener(view -> {

            checkAnswer();

            radioTermGroup.clearCheck();
            btnConfirm.setEnabled(false);

            if (totalQuestions == currentQuestion) {
                Intent iEndScreen = new Intent(ActivityQuiz.this, ActivityEndScreen.class);
                if (bundle != null) {
                    bundle.putInt("POINTS", correctAnswers);
                    bundle.putInt("TOTAL", totalQuestions);
                }
                iEndScreen.putExtras(bundle);
                startActivity(iEndScreen);
            }
            else {
                displayQuestionAndOptions();
            }
        });
    }//end onCreate

    public void readInternalRaw(){
        String line;
        String[] lineArray;
        BufferedReader bufferedReader;

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.quiz2);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = bufferedReader.readLine()) != null) {
                lineArray = line.split(";");
                termsDefinitionsArrayList.add(lineArray);
                termsAndDefinitionsHashMap.put(lineArray[0],lineArray[1]);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shuffleArrayList(){
        Collections.shuffle(termsDefinitionsArrayList);
        totalQuestions = termsDefinitionsArrayList.size();
    }

    public void displayQuestionAndOptions(){
        Random random = new Random();
        ArrayList<Integer> indexList = new ArrayList<>();
        ArrayList<String> optionsList = new ArrayList<>();

        for (int i = 0; i < totalQuestions; i++) {
            indexList.add(i);
        }

        tvDefinition.setText(termsDefinitionsArrayList.get(currentQuestion)[1]);
        optionsList.add(termsDefinitionsArrayList.get(currentQuestion)[0]);
        indexList.remove(currentQuestion);
        currentQuestion += 1;

        int currentRandom;
        while (optionsList.size() < 4) {
            currentRandom = random.nextInt(indexList.size());
            optionsList.add(termsDefinitionsArrayList.get(indexList.get(currentRandom))[0]);
            indexList.remove(currentRandom);
        }

        Collections.shuffle(optionsList);

        radioTermOne.setText(optionsList.get(0));
        radioTermTwo.setText(optionsList.get(1));
        radioTermThree.setText(optionsList.get(2));
        radioTermFour.setText(optionsList.get(3));

        tvTotalQuestions.setText(String.format("%s/%s", currentQuestion, totalQuestions));
    }

    public void checkAnswer(){
        // get selected radio button from radioGroup
        int selectedRadioButtonId = radioTermGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioSelected = findViewById(selectedRadioButtonId);

        String term = radioSelected.getText().toString();
        String definition = tvDefinition.getText().toString();

        if (Objects.equals(termsAndDefinitionsHashMap.get(term), definition)) {
            correctAnswers += 1;
            Toast.makeText(ActivityQuiz.this, "Correct", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(ActivityQuiz.this, "Incorrect", Toast.LENGTH_SHORT).show();
        }

        tvCorrectAnswer.setText(String.format("%s", correctAnswers));
    }

}//and class ActivityTwo