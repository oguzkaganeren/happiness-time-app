package com.example.tav.happinesstime;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView questionTxt;
    TextView option1Txt;
    TextView option2Txt;
    TextView option3Txt;
    TextView option4Txt;
    String answer;
    String option1,option2,option3;
    String categori;
    RoundCornerProgressBar queTimer;
    MaterialDialog.Builder dialog;
    int values[]=new int[4];
    float reTime=100;
    Random rnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rnd=new Random();
        questionTxt=(TextView) findViewById(R.id.questiontxt);
        option1Txt=(TextView) findViewById(R.id.option1txt);
        option2Txt=(TextView) findViewById(R.id.option2txt);
        option3Txt=(TextView) findViewById(R.id.option3txt);
        option4Txt=(TextView) findViewById(R.id.option4txt);
        dialog = new MaterialDialog.Builder(this);
        clearTxt();
        queTimer = (RoundCornerProgressBar) findViewById(R.id.time);
        queTimer.setMax(100);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        categori = getIntent().getExtras().getString("where");//bir önceki activity'den gelen deger
        loadQuestion();

        //setTextOptions();
    }
    private void clearTxt(){
        option1Txt.setText("");
        option2Txt.setText("");
        option3Txt.setText("");
        option4Txt.setText("");
    }
    public void loadQuestion(){
        ValueEventListener getQues=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(singleSnapshot.hasChild("question")){
                        questionTxt.setText(singleSnapshot.child("question").getValue(String.class));
                    }
                    if(singleSnapshot.hasChild("answers")){
                        answer=singleSnapshot.child("answers").getValue(String.class);
                        option1Txt.setText(answer);
                    }
                    if(singleSnapshot.hasChild("wrong1")){
                        option1=singleSnapshot.child("wrong1").getValue(String.class);
                        option2Txt.setText(option1);
                    }
                    if(singleSnapshot.hasChild("wrong2")){
                        option2=singleSnapshot.child("wrong2").getValue(String.class);
                        option3Txt.setText(option2);
                    }
                    if(singleSnapshot.hasChild("wrong3")){
                        option3=singleSnapshot.child("wrong3").getValue(String.class);
                        option4Txt.setText(option3);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("test", "onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("questions").child(categori).addValueEventListener(getQues);
        mDatabase.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String previousKey) {
                new CountDownTimer(1000, 100) {
                    public void onTick(long millisUntilFinished) {
                        reTime-=1;
                        queTimer.setProgress(reTime);
                    }

                    public void onFinish() {
                        //activity kapatılınca error veriyor
                        dialog
                                .title("Game Over")
                                .content("TEST"+reTime)
                                .positiveText("Yeni Oyun")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                }.start();
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
