package com.example.tav.happinesstime;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView questionTxt;
    String answer;
    TextView optionTxt[]=new TextView[4];
    CardView cards[]=new CardView[4];
    String option1,option2,option3;
    String categori;
    TheQuestion[] myQuestions;
    Integer userPoint;
    int questionCounter=0;
    int milisfuture=4000;
    int downinter=100;
    RoundCornerProgressBar queTimer;
    MaterialDialog.Builder dialog;
    int values[]=new int[4];
    int sy=1;
    int sl=0;
    float reTime=200;
    Random rnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        myQuestions=new TheQuestion[50];
        for(int i=0; i<50;i++){
            myQuestions[i]=new TheQuestion();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rnd=new Random();
        questionTxt=(TextView) findViewById(R.id.questiontxt);
        cards[0]=(CardView) findViewById(R.id.option1);
        cards[1]=(CardView) findViewById(R.id.option2);
        cards[2]=(CardView) findViewById(R.id.option3);
        cards[3]=(CardView) findViewById(R.id.option4);
        optionTxt[0]=(TextView) findViewById(R.id.option1txt);
        optionTxt[1]=(TextView) findViewById(R.id.option2txt);
        optionTxt[2]=(TextView) findViewById(R.id.option3txt);
        optionTxt[3]=(TextView) findViewById(R.id.option4txt);
        dialog = new MaterialDialog.Builder(QuestionActivity.this);
        clearTxt();
        queTimer = (RoundCornerProgressBar) findViewById(R.id.time);
        queTimer.setMax(200);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        categori = getIntent().getExtras().getString("where");//bir önceki activity'den gelen deger

        readTheFiles();
        Log.d("test", "bbbb: "+myQuestions[0].question);
        nextQues();

        getPoint();
        cards[0].setOnClickListener(this);
        cards[1].setOnClickListener(this);
        cards[2].setOnClickListener(this);
        cards[3].setOnClickListener(this);


    }
    public void getPoint(){
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("points") && mAuth != null) {
                    userPoint = snapshot.child("points").getValue(Integer.class);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
    private void itTrue(int i){
        cards[i].setBackgroundColor(getResources().getColor(R.color.md_green_500));
        optionTxt[i].setTextColor(getResources().getColor(R.color.md_white_1000));
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("profile").child("points").setValue(userPoint+10);
        setTextOptions();
        tTimer.cancel();
        reTime=200;
        milisfuture=4000;
        downinter=100;
        queTimer.setProgress(200);
        new CountDownTimer(500, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                enableCards();
                nextQues();
            }
        }.start();

        //disable ekle
    }
    private void disableCards(int i){
        for (int j=0;j<4;j++){
            cards[j].setClickable(false);
            if(i!=j&&!optionTxt[j].getText().equals(answer)){
                cards[j].setVisibility(View.INVISIBLE);
            }
            if(i!=j&&optionTxt[j].getText().equals(answer)){
                cards[j].setBackgroundColor(getResources().getColor(R.color.md_green_500));
                optionTxt[j].setTextColor(getResources().getColor(R.color.md_white_1000));
            }
        }
    }
    private void enableCards(){
        for (int j=0;j<4;j++){
            cards[j].setClickable(true);
            cards[j].setVisibility(View.VISIBLE);
            cards[j].setBackgroundColor(getResources().getColor(R.color.md_white_1000));
            optionTxt[j].setTextColor(getResources().getColor(R.color.md_black_1000));
        }
    }
    private void itFalse(int i){
        cards[i].setBackgroundColor(getResources().getColor(R.color.md_red_500));
        optionTxt[i].setTextColor(getResources().getColor(R.color.md_white_1000));
        tTimer.onFinish();
        disableCards(i);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.option1 :
                if(optionTxt[0].getText()==myQuestions[questionCounter-1].answers){
                    itTrue(0);
                }else{
                    itFalse(0);
                }
                break;
            case R.id.option2 :
                if(optionTxt[1].getText()==myQuestions[questionCounter-1].answers){
                    itTrue(1);
                }else{
                    itFalse(1);
                }
                break;
            case R.id.option3 :
                if(optionTxt[2].getText()==myQuestions[questionCounter-1].answers){
                    itTrue(2);
                }else{
                    itFalse(2);
                }
                break;
            case R.id.option4 :
                if(optionTxt[3].getText()==myQuestions[questionCounter-1].answers){
                    itTrue(3);
                }else{
                    itFalse(3);
                }
                break;
            default:break;
        }
    }
    public boolean checkSame(int pos){
        for (int i=0;i<4;i++){
            if(values[pos]==values[i]&&i!=pos){
                return true;
            }
        }
        return false;
    }
    private void clearTxt(){
        optionTxt[0].setText("");
        optionTxt[1].setText("");
        optionTxt[2].setText("");
        optionTxt[3].setText("");
    }
    public void setTextOptions(){
        for (int i=0; i<4;i++){
            do {
                int rndNum=(rnd.nextInt(4)+1)-1;
                values[i]=rndNum;
            }while (checkSame(i));
        }
    }
    void readTheFiles()
    {
        TheQuestion ques=new TheQuestion();
        BufferedReader reader;
        String key;
        try
        {
            final InputStream file = getAssets().open(categori+".txt");
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            int j = 0;
            int syc=0;
            boolean added=false;
            while(line != null)
            {
                Log.e("test", line);
                switch (j)
                {
                    case 0: myQuestions[syc].question = line;j++; break;
                    case 1: myQuestions[syc].answers = line;j++; break;
                    case 2: myQuestions[syc].wrong1 = line;j++; break;
                    case 3: myQuestions[syc].wrong2 = line;j++; break;
                    case 4: myQuestions[syc].wrong3 = line;j = 0;added=true;break;
                }
                if(added){
                    added=false;
                    syc++;
                }

                line = reader.readLine();
            }
        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void nextQues(){
        setTextOptions();
        enableCards();
        tTimer.start();
        //questionTxt.setText(myQuestions[questionCounter].question);
        questionTxt.setText(myQuestions[questionCounter].question);
        optionTxt[values[0]].setText(myQuestions[questionCounter].answers);
        optionTxt[values[1]].setText(myQuestions[questionCounter].wrong1);
        optionTxt[values[2]].setText(myQuestions[questionCounter].wrong2);
        optionTxt[values[3]].setText(myQuestions[questionCounter].wrong3);
        questionCounter++;
    }
    final CountDownTimer tTimer=new CountDownTimer(milisfuture, downinter) {
        @Override
        public void onTick(long l) {
            reTime-=5;
            queTimer.setProgress(reTime);
        }

        @Override
        public void onFinish() {
            tTimer.cancel();
            dialog.title("Game Over").content("Your total Point:"+userPoint)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).show();
        }
    };
    /* public void loadQuestion(){

        final ValueEventListener
                 getQues=new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                     if(singleSnapshot.hasChild("question")){

                         String test = singleSnapshot.child("question").getValue(String.class);
                         Log.d("test", "onDataChange: "+test);
                         Log.d("test", "aaaaaaa: "+sl);
                         gizliT.setText(gizliT.getText()+test+",");

                     }
                     if(singleSnapshot.hasChild("answers")){
                         answer=singleSnapshot.child("answers").getValue(String.class);
                         Log.d("test", "aaaaaaa: "+myQuestions[sl].answers);
                         //optionTxt[values[0]].setText(answer);
                         gizliT.setText(gizliT.getText()+answer+",");
                     }
                     if(singleSnapshot.hasChild("wrong1")){
                         option1=singleSnapshot.child("wrong1").getValue(String.class);
                         //optionTxt[values[1]].setText(option1);
                         gizliT.setText(gizliT.getText()+option1+",");
                     }
                     if(singleSnapshot.hasChild("wrong2")){
                         option2=singleSnapshot.child("wrong2").getValue(String.class);
                        //optionTxt[values[2]].setText(option2);
                         gizliT.setText(gizliT.getText()+option2+",");
                     }
                     if(singleSnapshot.hasChild("wrong3")){
                         option3=singleSnapshot.child("wrong3").getValue(String.class);
                        //optionTxt[values[3]].setText(option3);
                         gizliT.setText(gizliT.getText()+option3+",");
                     }

                 }
             }
             @Override
             public void onCancelled(DatabaseError databaseError) {
                 Log.e("test", "onCancelled", databaseError.toException());
             }
         };
         mDatabase.child("questions").child(categori).addValueEventListener(getQues);
         mDatabase.child("questions").addChildEventListener(new ChildEventListener() {
             public void onChildAdded(DataSnapshot dataSnapshot, String previousKey) {

                tTimer.start();
                 mDatabase.child("questions").child(categori).removeEventListener(getQues);
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
     }*/
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
}
class TheQuestion {

    public String question;
    public String answers;
    public int point;
    public String wrong1;
    public String wrong2;
    public String wrong3;

    public TheQuestion() {
        this.question = " ";
        this.answers = " ";
        this.point = 0;
        this.wrong1 = " ";
        this.wrong2 =  " " ;
        this.wrong3 = " ";
    }

    public TheQuestion(String question, String answers,String wrong1,String wrong2,String wrong3, int point, String categori) {
        this.question = question;
        this.answers = answers;
        this.point = point;
        this.wrong1 = wrong1;
        this.wrong2 = wrong2;
        this.wrong3 = wrong3;
    }
}