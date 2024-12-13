package com.example.flashcard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SCREEN2_Main2Activity extends AppCompatActivity {

    public int ctr = 0; //bottom pointer
    public int top; //top pointer

    public String deckName; //The name of the deck chosen

    public ArrayList<Card> cardArrayList; //Stores the cards temporarily

    TextView textqn; //Display Text for questions
    TextView textans; //Display Text for answers

    Button delete; //Delete button

    //Database Helper
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //UI Element declaration
        delete = findViewById(R.id.delete);
        textqn =findViewById(R.id.textView);
        textans = findViewById(R.id.textView2);

        //Acquiring and displaying appropriate deck
        deckName = getIntent().getStringExtra("DECK_NAME");
        databaseHelper = new DatabaseHelper(this);
        cardArrayList = databaseHelper.openDeck(deckName);
        top = cardArrayList.size()-1;
        textqn.setText(cardArrayList.get(0).getQns());
        textans.setText(cardArrayList.get(0).getAns());
        if (top==0){
            delete.setEnabled(false);
            delete.setBackgroundResource(R.drawable.disabled);
            delete.setTextColor(Color.parseColor("#333333"));
        }
    }

    //Move to the next card, otherwise cycle
    public void next (View view)
    {
        Log.d("ctr", String.valueOf(ctr));
        Log.d("top", String.valueOf(top));
        if(ctr<top)
        {
            ctr++;
            textqn.setText(cardArrayList.get(ctr).getQns());
            textans.setText(cardArrayList.get(ctr).getAns());
        }

        else if(ctr==top)
        {
            ctr = 0;
            textqn.setText(cardArrayList.get(ctr).getQns());
            textans.setText(cardArrayList.get(ctr).getAns());
        }
    }

    //Move to previous card, otherwise cycle
    public void prev (View view)
    {
        if(ctr>0)
        {
            ctr--;
            textqn.setText(cardArrayList.get(ctr).getQns());
            textans.setText(cardArrayList.get(ctr).getAns());
        }

        else
        {
            ctr = top;
            textqn.setText(cardArrayList.get(ctr).getQns());
            textans.setText(cardArrayList.get(ctr).getAns());
        }
    }

    //Display a random card
    public void shuffle (View view){
        Random random = new Random();
        int temp;

        while(true) {
            temp = ctr;
            ctr = random.nextInt(top + 1);

            if (ctr!=temp||top==0) {
                break;
            }
        }
        textans.setText(cardArrayList.get(ctr).getAns());
        textqn.setText(cardArrayList.get(ctr).getQns());
    }

    //Add a new card
    public void add (View view)
    {
        Intent intent = new Intent(this, SCREEN5_InsertCard.class);
        startActivityForResult(intent, 0);
    }

    //Edit the current card
    public void edit (View view)
    {
        Intent intent1 = new Intent(this, SCREEN6_EditCard.class);
        startActivityForResult(intent1,1);
    }

    //Delete the current card
    public void delete (View view)
    {
        //Ensuring no underflow
        if(top>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(SCREEN2_Main2Activity.this);

            builder.setCancelable(true);
            builder.setTitle("Are you sure?");
            builder.setMessage("Are you sure you want to delete this card?");

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        databaseHelper.deleteOne(cardArrayList.get(ctr));
                        cardArrayList = databaseHelper.openDeck(deckName);
                    }

                    catch(Exception e){
                        System.out.println("Cannot");
                    }

                    top = cardArrayList.size()-1;

                    if(ctr>top) {
                        ctr=top;
                    }

                    if (top==0) {
                        delete.setEnabled(false);
                        delete.setBackgroundResource(R.drawable.disabled);
                        delete.setTextColor(Color.parseColor("#333333"));
                    }

                    cardArrayList = databaseHelper.openDeck(deckName);
                    textans.setText(cardArrayList.get(ctr).getAns());
                    textqn.setText(cardArrayList.get(ctr).getQns());
                }
            });
            builder.show();
        }

        else if (top==0){
            delete.setEnabled(false);
            delete.setBackgroundResource(R.drawable.disabled);
            delete.setTextColor(Color.parseColor("#333333"));
        }
    }

    //Display SCREEN7_Annotations
    public void annotations (View view){
        Intent intent2 = new Intent (this, SCREEN7_Annotations.class);
        intent2.putExtra("ANNOTATION", cardArrayList.get(ctr).getAnnotations());
        Log.d("ctr", String.valueOf(ctr));
        startActivityForResult(intent2, 2);
    }
    //Test Mode
    public void testMode (View view){
        Intent intent3 = new Intent(this, SCREEN8_TestMode.class);
        intent3.putExtra("DECK_NAME", deckName);
        startActivity(intent3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Display new cards instantly from newly added cards
        if(requestCode == 0 && resultCode == RESULT_OK) {
            Card newCard = new Card (1, data.getStringExtra("QUESTION"), data.getStringExtra("ANSWER"), deckName, "");
            databaseHelper.addOne(newCard);
            cardArrayList = databaseHelper.openDeck(deckName);
            top = cardArrayList.size()-1;
            ctr = top;
            textans.setText(cardArrayList.get(ctr).getAns());
            textqn.setText(cardArrayList.get(ctr).getQns());
            delete.setEnabled(true);
            delete.setBackgroundResource(R.drawable.enabled);
            delete.setTextColor(Color.parseColor("#FFFFFF"));
        }

        //Display changes instantly and store them
        else if(requestCode == 1 && resultCode == RESULT_OK) {
            boolean editChecker = databaseHelper.edit(cardArrayList.get(ctr), deckName, data.getStringExtra("QUESTION"), data.getStringExtra("ANSWER"));
            if(editChecker == true) {
                Toast.makeText(SCREEN2_Main2Activity.this, "Changes saved", Toast.LENGTH_SHORT).show();
            }
            else if(editChecker == false){
                Toast.makeText(SCREEN2_Main2Activity.this, "Changes not saved", Toast.LENGTH_SHORT).show();
            }
            top = cardArrayList.size()-1;
            cardArrayList = databaseHelper.openDeck(deckName);
            textans.setText(cardArrayList.get(ctr).getAns());
            textqn.setText(cardArrayList.get(ctr).getQns());
        }

        //Save and display annotations
        else if(requestCode == 2 && resultCode == RESULT_OK){
            boolean annotChecker = databaseHelper.modifyAnnotation(cardArrayList.get(ctr).getId(), data.getStringExtra("ANNOTATION"));
            top = cardArrayList.size()-1;
            cardArrayList = databaseHelper.openDeck(deckName);
            textans.setText(cardArrayList.get(ctr).getAns());
            textqn.setText(cardArrayList.get(ctr).getQns());
            if(annotChecker == true) {
                Toast.makeText(SCREEN2_Main2Activity.this, "Annotations saved", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(SCREEN2_Main2Activity.this, "Annotations not saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




