package com.example.tav.happinesstime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView sport,history,science,cinema,mix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        sport = (CardView) findViewById(R.id.sport_card);
        history = (CardView) findViewById(R.id.history);
        science = (CardView) findViewById(R.id.science);
        cinema = (CardView) findViewById(R.id.cinema);
        mix = (CardView) findViewById(R.id.mix);

        sport.setOnClickListener(this);
        history.setOnClickListener(this);
        science.setOnClickListener(this);
        cinema.setOnClickListener(this);
        mix.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){
            case R.id.sport_card :
                i = new Intent(this,QuestionActivity.class);
                i.putExtra("where", "sport");
                startActivity(i);
                break;
            case R.id.history :
                i = new Intent(this,QuestionActivity.class);
                i.putExtra("where", "history");
                startActivity(i);
                break;
            case R.id.science :
                i = new Intent(this,QuestionActivity.class);
                i.putExtra("where", "science");
                startActivity(i);
                break;
            case R.id.cinema :
                i = new Intent(this,QuestionActivity.class);
                i.putExtra("where", "cinema");
                startActivity(i);
                break;
            case R.id.mix :
                i = new Intent(this,QuestionActivity.class);
                i.putExtra("where", "mix");
                startActivity(i);
                break;
            default:break;
        }



    }
}
