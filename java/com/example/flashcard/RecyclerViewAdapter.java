package com.example.flashcard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ExampleViewHolder> {

    //Element declaration
    private ArrayList<String> deckName;
    private Context mContext;
    private OnDeckClickListener monDeckClickListener;

    //Connecting with UI
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public Button singularDeck;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            singularDeck = itemView.findViewById(R.id.deckButton);
        }
    }

    //Class declaration
    public RecyclerViewAdapter(ArrayList<String> deckNamelist, Context context, OnDeckClickListener onDeckClickListener){
        deckName = deckNamelist;
        mContext = context;
        this.monDeckClickListener = onDeckClickListener;
    }

    //Display decks
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.generic_button, parent, false);
        ExampleViewHolder exampleViewHolder = new ExampleViewHolder(view);
        return exampleViewHolder;
    }

    //Actions when button is pressed
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        String string = deckName.get(position);
        holder.singularDeck.setText(string);

        //Display Cards
        holder.singularDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monDeckClickListener.onDeckClick(deckName.get(position));
                Intent intent = new Intent(mContext, SCREEN2_Main2Activity.class);
                intent.putExtra("DECK_NAME", deckName.get(position));
                mContext.startActivity(intent);
            }
        });

        //Change deck name / delete deck
        holder.singularDeck.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               Intent intent = new Intent (mContext, SCREEN3_EditDeckName.class);
               intent.putExtra("DECK_NAME", deckName.get(position));
               mContext.startActivity(intent);
               return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return deckName.size();
    }

    //Pass to SCREEN1_MainActivity which search bar is focused
    public interface OnDeckClickListener {
        void onDeckClick(String data);
    }
}
