package com.example.tav.happinesstime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView single,multi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        single = (CardView) findViewById(R.id.single);
        multi = (CardView) findViewById(R.id.multiple);
        single.setOnClickListener(this);
        multi.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.single : i = new Intent(this,CategoriesActivity.class); startActivity(i); break;
            //case R.id.multiple : i = new Intent(this,History.class); startActivity(i); break;
            default:break;
        }

    }

}
