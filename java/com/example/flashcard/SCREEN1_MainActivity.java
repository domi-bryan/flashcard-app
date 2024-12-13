package com.example.flashcard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class SCREEN1_MainActivity extends AppCompatActivity implements View.OnLongClickListener, RecyclerViewAdapter.OnDeckClickListener {

    //RecyclerView Initialization
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //Button Initialization
    Button addNewDeck;
    Button notificationButton;

    //DatabaseHelper
    DatabaseHelper databaseHelper;

    //Search Box
    EditText searchBar;
    TextView recentActivities;
    Animation animation;

    //Intents
    Intent intent; //Go to SCREEN2_Main2Activity.class
    Intent intent1; //Go to SCREEN3_EditDeckName.class
    Intent intent2; //Go to SCREEN4_InsertNewDeck.class

    //Search History
    ArrayList<String> searchResults = new ArrayList<>();
    ArrayList<String> searchHistory = new ArrayList<>();
    String temp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing DatabaseHelper
        databaseHelper = new DatabaseHelper(SCREEN1_MainActivity.this);

        //Initializing Button to Add New Deck
        addNewDeck = findViewById(R.id.addNewDeck);

        //Initializing the Search Bar
        recentActivities = findViewById(R.id.recentActivities);
        searchBar = findViewById(R.id.searchBar);

        //Initializing Button to Set Notification
        notificationButton = findViewById(R.id.settingButton);

        //Initializing the RecyclerView, and passing in the values
        mRecyclerView = findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RecyclerViewAdapter(databaseHelper.getDeckNamelist(), this, SCREEN1_MainActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Initializing Intents
        intent = new Intent(this, SCREEN2_Main2Activity.class);
        intent1 = new Intent(this, SCREEN3_EditDeckName.class);
        intent2 = new Intent(this, SCREEN4_InsertNewDeck.class);

        //Notification channel
        notificationChannel();

        //Assigning animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);

        //Changing between search results and recent activities
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBar.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                for(int a=0; a<searchHistory.size()/2; a++){
                    temp = searchHistory.get(a);
                    searchHistory.set(a, searchHistory.get(searchHistory.size()-1-a));
                    searchHistory.set(searchHistory.size()-1-a, temp);
                }
                if(hasFocus && searchBar.getText().toString().isEmpty() && !searchHistory.isEmpty()){
                    mAdapter = new RecyclerViewAdapter(searchHistory, getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setText("Recent Activities:");
                    recentActivities.setTextColor(Color.parseColor("#FFFFFF"));
                    mRecyclerView.startAnimation(animation);
                }
                else if(hasFocus && searchBar.getText().toString().isEmpty() && searchHistory.isEmpty()){
                    mAdapter = new RecyclerViewAdapter(databaseHelper.getDeckNamelist(), getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setTextColor(Color.parseColor("#000000"));
                }
                else if(!hasFocus && searchBar.getText().toString().isEmpty()){
                    mAdapter = new RecyclerViewAdapter(databaseHelper.getDeckNamelist(), getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setTextColor(Color.parseColor("#000000"));
                }

            }
        });

        //Receiving search queries
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
                if(searchBar.hasFocus() && searchBar.getText().toString().isEmpty() && !searchHistory.isEmpty()){
                    mAdapter = new RecyclerViewAdapter(searchHistory, getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setText("Recent Activities:");
                    recentActivities.setTextColor(Color.parseColor("#FFFFFF"));
                    mRecyclerView.startAnimation(animation);
                }
                else if(searchBar.hasFocus() && searchBar.getText().toString().isEmpty() && searchHistory.isEmpty()){
                    mAdapter = new RecyclerViewAdapter(databaseHelper.getDeckNamelist(), getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setTextColor(Color.parseColor("#000000"));
                }
                else if(!searchBar.getText().toString().isEmpty()){
                    mAdapter = new RecyclerViewAdapter(searchResults, getApplicationContext(), SCREEN1_MainActivity.this);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                    recentActivities.setText("Search Results:");
                    recentActivities.setTextColor(Color.parseColor("#FFFFFF"));
                    mRecyclerView.startAnimation(animation);
                }
            }
        });
    }

    //searches according to search query
    private void search(String query) {
        searchResults.clear();
        for(int a=0; a<databaseHelper.getDeckNamelist().size(); a++){
            if(databaseHelper.getDeckNamelist().get(a).contains(query)){
                searchResults.add(databaseHelper.getDeckNamelist().get(a));
            }
        }
        mAdapter = new RecyclerViewAdapter(searchResults, this, SCREEN1_MainActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    //Retrieves data from recycler view adapter on which deck is clicked, so it can be passed to searchHistory arraylist
    @Override
    public void onDeckClick(String data) {
        if(searchHistory.isEmpty() || !searchHistory.contains(data)){
            searchHistory.add(data);
        }
        else if(searchHistory.contains(data)){
            searchHistory.remove(data);
            searchHistory.add(data);
        }
        searchBar.getText().clear();
        searchBar.clearFocus();
    }

    //Go to screen to add New Deck
    public void addNewDeck(View view){
        startActivityForResult(intent2, 0);
    }

    //Confirm Notification
    public void enableNotification(View view){

        Toast.makeText(this, "Notification Set!", Toast.LENGTH_SHORT).show();
        Calendar hourofday = Calendar.getInstance();
        hourofday.set(Calendar.HOUR_OF_DAY, 9);
        hourofday.set(Calendar.MINUTE, 0);
        hourofday.set(Calendar.SECOND, 10);

        Intent intent4 = new Intent(SCREEN1_MainActivity.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent4, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, hourofday.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //Notification Channel
    protected void notificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Generic Name";
            String description = "Generic Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("remindMe", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    //???
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //To instantly display new decks
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
            mAdapter = new RecyclerViewAdapter(databaseHelper.getDeckNamelist(), this, SCREEN1_MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

