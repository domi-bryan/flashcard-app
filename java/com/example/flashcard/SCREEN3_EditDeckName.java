package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SCREEN3_EditDeckName extends AppCompatActivity {

    //Element Initialization
    EditText editText;
    String oldDeckName;
    String newDeckName;

    //Database Helper
    DatabaseHelper databaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_name);

        //Element Initialization
        editText = findViewById(R.id.editText2);

        //Database Helper Initialization
        databaseHelper = new DatabaseHelper(SCREEN3_EditDeckName.this);

        //Catching the Old Deck Name
        oldDeckName = getIntent().getStringExtra("DECK_NAME");
    }

    //Null checker and input
    public void EditCard(View view) {
        newDeckName = editText.getText().toString();
        if(!newDeckName.isEmpty()) {
            boolean editChecker = databaseHelper.changeDeckName(oldDeckName, newDeckName);
            if (editChecker == true) {
                Toast.makeText(SCREEN3_EditDeckName.this, "Deck name successfully changed", Toast.LENGTH_SHORT).show();
            } else  if (editChecker == false){
                Toast.makeText(SCREEN3_EditDeckName.this, "Deck name is not changed", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this, SCREEN1_MainActivity.class);
            intent.putExtra("EDIT_CHECKER", editChecker);
            startActivity(intent);

            finish();
        }
        else if (newDeckName.isEmpty()){
            Toast.makeText(SCREEN3_EditDeckName.this, "Please enter a new deck name!", Toast.LENGTH_SHORT).show();
        }
    }

    //Null checker and deleting decks
    public void deleteDeck(View view) {
        if(databaseHelper.countDeck()>=1) {
            databaseHelper.deleteDeck(oldDeckName);
            Toast.makeText(this, "Deck removed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SCREEN1_MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "No decks to delete", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SCREEN1_MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
