package com.example.assign2_quizbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //controls
    EditText etUserName;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = findViewById(R.id.etUserName);
        btnStart = findViewById(R.id.btnStart);

        etUserName.addTextChangedListener(userNameTextWatcher);

        //btnStart button listener class
        btnStart.setOnClickListener(view -> {
            Intent iQuizScreen = new Intent(MainActivity.this, ActivityQuiz.class);//create intent object
            Bundle bundle = new Bundle();//create bundle object
            bundle.putString("NAME", etUserName.getText().toString());//fill bundle
            iQuizScreen.putExtras(bundle);
            startActivity(iQuizScreen);
        });//end btnStart button listener class

    }//end onCreate

    private TextWatcher userNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }//end method beforeTextChanged

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userNameInput = etUserName.getText().toString().trim();

            btnStart.setEnabled(!userNameInput.isEmpty());
        }//end method onTextChanged

        @Override
        public void afterTextChanged(Editable s) {

        }//end method afterTextChanged
    };//end inner class

}//end class