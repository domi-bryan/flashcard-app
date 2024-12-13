package com.example.flashcard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //String declaration
    public static final String CARDS_TABLE = "CARDS_TABLE";
    public static final String COLUMN_QUESTIONS = "QUESTIONS";
    public static final String COLUMN_ANSWERS = "ANSWERS";
    public static final String COLUMN_DECK_NAME = "DECK_NAME";
    public static final String COLUMN_ANNOTATIONS = "ANNOTATIONS";
    public static final String ID = "ID";

    //Class declaration
    public DatabaseHelper (@Nullable Context context) {
        super(context, "cards.db", null, 1);
    }

    //Create Statement
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CARDS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DECK_NAME + " TEXT, " + COLUMN_QUESTIONS + " TEXT, " + COLUMN_ANSWERS + " TEXT, " + COLUMN_ANNOTATIONS + " TEXT" + ")";
        db.execSQL(createTableStatement);
    }

    //Upgrade Statement
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Add new record
    public boolean addOne (Card cards){

        SQLiteDatabase db  = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_QUESTIONS, cards.getQns());
        cv.put(COLUMN_ANSWERS, cards.getAns());
        cv.put(COLUMN_DECK_NAME, cards.getDeckName());
        cv.put(COLUMN_ANNOTATIONS, cards.getAnnotations());

        long insert = db.insert(CARDS_TABLE, null, cv);

        if(insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //Delete a record
    public boolean deleteOne (Card cards){
        SQLiteDatabase db  = getWritableDatabase();
        String queryString = "DELETE FROM "+ CARDS_TABLE +" WHERE "+ ID + " = " + cards.getId();
        final Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return  true;
        }
        else{
            return false;
        }
    }

    //Update a record
    public boolean edit (Card cards, String deckName, String questions, String answers){
        SQLiteDatabase db = getWritableDatabase();
        Log.d("questions", questions);
        Log.d("answers", answers);
        String queryString = "UPDATE "+ CARDS_TABLE +" SET "+ COLUMN_DECK_NAME + " = " + " '" +  deckName + "' "+", "
                + COLUMN_QUESTIONS + " = "+ " '"+  questions + "' " + ", "
                + COLUMN_ANSWERS + " = "+ " '" + answers + "' "
                + " WHERE "+ ID + " = " + cards.getId();
        final Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){return true;}
        else{return true;}
    }

    //Retrieve distinct deck names
    public ArrayList<String> getDeckNamelist (){
        ArrayList<String> returnList = new ArrayList<>();

        String queryString = "SELECT DISTINCT " + COLUMN_DECK_NAME + " FROM " + CARDS_TABLE;

        SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery(queryString, null);

        if(cursor!=null && cursor.getCount() > 0)
        {
            if(cursor.moveToFirst()){
                do{
                    String deckName = cursor.getString(0);
                    returnList.add(deckName);
                }while (cursor.moveToNext());
            }

            else{

            }
        }
        cursor.close();
        db.close();
        return returnList;
    }

    //Retreive cards in a deck
    public ArrayList<Card> openDeck(String deckNameQuery){
        ArrayList<Card> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM "+ CARDS_TABLE + " WHERE "+ COLUMN_DECK_NAME + " = " + " '" + deckNameQuery + "' ";

        SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            do{
                int ID = cursor.getInt(0);
                String deckName = cursor.getString(1);
                String questions = cursor.getString(2);
                String answer = cursor.getString(3);
                String annotations = cursor.getString(4);

                Card cardsInADeck = new Card(ID, questions, answer, deckName, annotations);
                returnList.add(cardsInADeck);

            }while (cursor.moveToNext());
        }
        else{

        }
        cursor.close();
        db.close();
        return returnList;
    }

    //Change deck name
    public boolean changeDeckName(String oldDeckName, String newDeckName){
        String queryString = "UPDATE "+ CARDS_TABLE +" SET "+ COLUMN_DECK_NAME + " = '" +  newDeckName + "' "
                + "WHERE "+ COLUMN_DECK_NAME + " = '"+ oldDeckName + "' ";

        SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){return true;}
        else{return true;}
    }

    //modify annotation
    public boolean modifyAnnotation(int id, String annotation){
        String queryString = "UPDATE "+ CARDS_TABLE +" SET "+ COLUMN_ANNOTATIONS + " = '" +  annotation + "' "
                + "WHERE "+ ID + " = "+ id;

        SQLiteDatabase db = getReadableDatabase();

        final Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){return true;}
        else{return true;}
    }

    //Delete a deck
    public boolean deleteDeck (String deckName){
        SQLiteDatabase db  = getWritableDatabase();
        String queryString = "DELETE FROM "+ CARDS_TABLE +" WHERE "+ COLUMN_DECK_NAME + " = '" + deckName +"' ";
        final Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    //Count distinct decks
    public int countDeck(){
        SQLiteDatabase db  = getWritableDatabase();
        int count=1;
        String queryString = "SELECT COUNT(DISTINCT " + COLUMN_DECK_NAME + ") AS \"" + count + "\" FROM "+ CARDS_TABLE;
        final Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return count;
        }
        else{
            return -1;
        }
    }
}
