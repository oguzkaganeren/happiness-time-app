package com.example.tav.happinesstime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Point_home extends AppCompatActivity implements View.OnClickListener{
    private CardView pointCard,campaignCard,orderCard;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Integer point;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_home);
        pointCard = (CardView) findViewById(R.id.point_card);
        pointCard.setOnClickListener(this);
         tv= (TextView) findViewById(R.id.txtView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("points") && mAuth != null) {
                    point = snapshot.child("points").getValue(Integer.class);
                    String a=String.valueOf(point);
                    tv.setText(a);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }
    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){
            case R.id.point_card : i = new Intent(this,PointCampaign.class); startActivity(i); break;


            default:break;
        }
    }
}
