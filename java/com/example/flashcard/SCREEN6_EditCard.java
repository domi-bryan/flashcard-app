package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class SCREEN6_EditCard extends AppCompatActivity {

    //Element declaration
    EditText editquestion;
    EditText editanswer;
    Button edit;
    Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_query);

        //Connecting element to UI
        editquestion = findViewById(R.id.EditQuestion);
        editanswer = findViewById(R.id.EditAnswer);
        edit = findViewById(R.id.EditCard);

        //Input
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                //Null checker
                if(editquestion.getText().toString().isEmpty()){
                    editquestion.startAnimation(shake);
                    Toast.makeText(SCREEN6_EditCard.this, "Insert a new question!", LENGTH_SHORT).show();
                }
                else if(editanswer.getText().toString().isEmpty()){
                    editanswer.startAnimation(shake);
                    Toast.makeText(SCREEN6_EditCard.this, "Insert a new answer!", LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("QUESTION", editquestion.getText().toString());
                    intent.putExtra("ANSWER", editanswer.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }
}
