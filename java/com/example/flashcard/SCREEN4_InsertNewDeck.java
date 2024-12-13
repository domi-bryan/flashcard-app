package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

public class SCREEN4_InsertNewDeck extends AppCompatActivity {

    //Element declaration
    EditText newDeckname;
    EditText firstQuestion;
    EditText firstAnswer;
    Animation shake;
    Card newCard;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_deck);

        //Connecting with UI
        newDeckname = findViewById(R.id.newDeckName);
        firstQuestion = findViewById(R.id.firstQuestion);
        firstAnswer = findViewById(R.id.firstAnswer);
    }

    //Creating New Decks
    public void createNewDeck(View view) {

        databaseHelper = new DatabaseHelper(SCREEN4_InsertNewDeck.this);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        //Null checker
        if(newDeckname.getText().toString().isEmpty()){
            Toast.makeText(SCREEN4_InsertNewDeck.this, "Please insert a deck name!", Toast.LENGTH_SHORT).show();
            newDeckname.startAnimation(shake);
        }
        else if(firstQuestion.getText().toString().isEmpty()){
            Toast.makeText(SCREEN4_InsertNewDeck.this, "Please insert a first question!", Toast.LENGTH_SHORT).show();
            firstQuestion.startAnimation(shake);
        }
        else if(firstAnswer.getText().toString().isEmpty()){
            Toast.makeText(SCREEN4_InsertNewDeck.this, "Please insert a first answer!", Toast.LENGTH_SHORT).show();
            firstAnswer.startAnimation(shake);
        }

        //Input
        else{
            newCard = new Card(1, firstQuestion.getText().toString(), firstAnswer.getText().toString(), newDeckname.getText().toString(), "");
            boolean addChecker = databaseHelper.addOne(newCard);
            if (addChecker == true) {
                Toast.makeText(SCREEN4_InsertNewDeck.this, "Deck Successfully Created", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SCREEN4_InsertNewDeck.this, "Deck creation is unsuccessful", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }
}
