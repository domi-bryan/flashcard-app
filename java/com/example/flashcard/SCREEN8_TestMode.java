package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SCREEN8_TestMode extends AppCompatActivity {

    //Element declaration
    TextView questionText;
    EditText answerText;
    Animation shake;
    Button nextQuestion;
    String deckName;
    ArrayList<Card> cards = new ArrayList<>();
    DatabaseHelper databaseHelper;
    Intent intent;
    int ctr = 0;
    int top;
    int rightCounter = 0;
    int bell = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);

        //Connecting with UI
        databaseHelper = new DatabaseHelper(this);
        deckName = getIntent().getStringExtra("DECK_NAME");
        cards = databaseHelper.openDeck(deckName);
        intent = new Intent(this, SCREEN9_TestResultScreen.class);
        questionText = findViewById(R.id.questionText);
        questionText.setText(cards.get(ctr).getQns());
        answerText = findViewById(R.id.answerText);
        nextQuestion = findViewById(R.id.nextQuestion);

        top = cards.size()-1;

    }

    public void nextQuestion(View view){
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        //Moving to the next question / Finishing Quiz
        if(nextQuestion.getText().toString().equals("Check Answer") && answerText.getText().toString().equals(cards.get(ctr).getAns()))
        {
            answerText.setBackgroundResource(R.drawable.correct);
            answerText.setTextColor(Color.parseColor("#00FF00"));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            answerText.setEnabled(false);

            nextQuestion.setBackgroundResource(R.drawable.add_new_deck);
            nextQuestion.setTextColor(Color.parseColor("#00FFFF"));

            rightCounter = rightCounter + bell;

            if(ctr<top) {nextQuestion.setText("Next Question");}
            else if (ctr==top){nextQuestion.setText("Finish Quiz");}
        }

        //Blank Answer
        else if(answerText.getText().toString().equals("")){
            Toast.makeText(this, "Please Enter Your Answer", Toast.LENGTH_SHORT).show();
            answerText.startAnimation(shake);
        }

        //Wrong Answer
        else if (!answerText.getText().toString().equals(cards.get(ctr).getAns()))
        {
            answerText.startAnimation(shake); // starts animation

            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            answerText.getText().clear();
            bell = 0;

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(500);
            }
        }

        //Move to next question
        else if(nextQuestion.getText().toString().equals(("Next Question"))){
            ctr++;
            questionText.setText(cards.get(ctr).getQns());
            nextQuestion.setText("Check Answer");
            answerText.getText().clear();
            answerText.setEnabled(true);
            answerText.setBackgroundResource((R.drawable.editable));
            answerText.setTextColor(Color.parseColor("#FFFFFF"));
            nextQuestion.setTextColor(Color.parseColor("#FFFFFF"));
            nextQuestion.setBackgroundResource(R.drawable.enabled);

            bell = 1;
        }

        //Finish Quiz
        else if (nextQuestion.getText().toString().equals("Finish Quiz")){
            intent.putExtra("DECK_NAME", deckName);
            intent.putExtra("DECK_SIZE", cards.size());
            intent.putExtra("RIGHT_COUNTER", rightCounter);
            startActivity(intent);
            finish();
        }
    }


    public void skip(View view) {
        //Skip to the next question
        if(ctr<top){
            ctr++;
            questionText.setText(cards.get(ctr).getQns());
            nextQuestion.setText("Check Answer");
            answerText.getText().clear();
            answerText.setBackgroundResource((R.drawable.editable));
            bell=1;
        }
        //Skip to the end
        else if (ctr == top){
            intent.putExtra("DECK_NAME", deckName);
            intent.putExtra("DECK_SIZE", cards.size());
            intent.putExtra("RIGHT_COUNTER", rightCounter);
            startActivity(intent);
            finish();
        }
    }
}
