package com.example.flashcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SCREEN7_Annotations extends AppCompatActivity {

    //Element decleration
    EditText annotationBox;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotations);

        //Receiving pre saved annotation
        String annotations = getIntent().getStringExtra("ANNOTATION");

        //Connecting elements with declaration
        annotationBox = findViewById(R.id.annotationBox);
        annotationBox.setText(annotations);
    }

    //Saving annotations
    public void saveAnnotations(View view) {
        intent = new Intent();
        intent.putExtra("ANNOTATION", annotationBox.getText().toString());
        setResult(RESULT_OK, intent);
        annotationBox.getText().toString();
        finish();
    }
}
