package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.*;

public class SCREEN5_InsertCard extends AppCompatActivity {

    //Element declaration
    EditText addquestion, addanswer;
    Button insert;
    Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_query);

        //Connecting with UI
        insert = findViewById(R.id.Insert);
        addquestion = findViewById(R.id.Addquestion);
        addanswer = findViewById(R.id.Addanswer);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                //Null checker
                if(addquestion.getText().toString().isEmpty()){
                    addquestion.startAnimation(shake);
                    Toast.makeText(SCREEN5_InsertCard.this, "Insert a new question!", LENGTH_SHORT).show();
                }
                else if(addanswer.getText().toString().isEmpty()){
                    addanswer.startAnimation(shake);
                    Toast.makeText(SCREEN5_InsertCard.this, "Insert a new answer!", LENGTH_SHORT).show();
                }

                //Input
                else{
                    Intent intent = new Intent();
                    intent.putExtra("QUESTION", addquestion.getText().toString());
                    intent.putExtra("ANSWER", addanswer.getText().toString());
                    setResult(RESULT_OK, intent);
                    Toast.makeText(SCREEN5_InsertCard.this, "Card Added", LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
