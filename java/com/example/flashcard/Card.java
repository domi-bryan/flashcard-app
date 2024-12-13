package com.example.flashcard;

public class Card {

    private int id;
    private String qns;
    private String ans;
    private String deckName;
    private String annotations;

    //Constructors

    public Card(int id, String qns, String ans, String deckName, String annotations) {
        this.id = id;
        this.qns = qns;
        this.ans = ans;
        this.deckName = deckName;
        this.annotations = annotations;
    }

    //Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQns() {
        return qns;
    }

    public void setQns(String qns) {
        this.qns = qns;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    //toString
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", qns='" + qns + '\'' +
                ", ans='" + ans + '\'' +
                ", deckName='" + deckName + '\'' +
                ", annotations='" + annotations + '\'' +
                '}';
    }
}

