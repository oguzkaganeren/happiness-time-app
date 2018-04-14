package com.example.tav.happinesstime;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private CardView pointCard,gameCard,addFlyCard,historyFlyCard,timerCard;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private AccountHeader headerResult;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSupportActionBar().hide();//Ocultar ActivityBar anterior
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            //Defining Cards
            loadDrawer(savedInstanceState);
            pointCard = (CardView) findViewById(R.id.point_card);
            gameCard = (CardView) findViewById(R.id.game_card);
            addFlyCard = (CardView) findViewById(R.id.add_fly_card);
            historyFlyCard = (CardView) findViewById(R.id.history_fly_card);
            timerCard = (CardView) findViewById(R.id.timer_card);

            pointCard.setOnClickListener(this);
            gameCard.setOnClickListener(this);
            addFlyCard.setOnClickListener(this);
            historyFlyCard.setOnClickListener(this);
        }

    }
    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){
            case R.id.point_card : i = new Intent(this,PointCampaign.class); startActivity(i); break;
            case R.id.game_card : i = new Intent(MainActivity.this,GameActivity.class); startActivity(i); break;
            case R.id.add_fly_card : i = new Intent(this,AddFly.class); startActivity(i); break;
            case R.id.history_fly_card : i = new Intent(this,HistoryFly.class); startActivity(i); break;
            default:break;
        }
    }
    private void loadDrawer(Bundle savedInstanceState){
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.gradient_backcolor)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                /*Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                MainActivity.this.startActivity(intent);*/
                        return false;
                    }
                })
                .build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Puan ve Kampanya").withIcon(R.drawable.ic_local_airport_black_24dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("game");
        //SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(2).withName("Event History").withIcon(R.drawable.event_24dp);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(2).withName("Geçmiş Uçuş");
        //SecondaryDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(2).withName("Settings").withIcon(R.drawable.setting_24dp);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(2).withName("Uçuş Ekle");
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(2).withName("Ayarlar");
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(2).withName("Çıkış Yap");
//create the drawer and remember the `Drawer` result object

        final Drawer left = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        // item3,
                        item4,
                        new DividerDrawerItem(),
                        //item5,
                        item6,
                        item7,
                        item8
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position){
                            case 2:

                                break;
                            case 3:
                                break;
                            case 4:

                                break;
                            case 8:
                                mAuth.signOut();
                                setDrawerState(false);
                                Toast.makeText(MainActivity.this, "Signout successful", Toast.LENGTH_SHORT).show();
                                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(refresh);
                                MainActivity.this.finish();
                                break;

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        drawerLayout = left.getDrawerLayout();
        actionBarDrawerToggle=  new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.material_drawer_open, R.string.material_drawer_close);
        if (mAuth.getCurrentUser() != null) {
            // User is logged in
            loadDataOnFirebase();
            setDrawerState(true);
        }else {
            setDrawerState(false);
        }
    }
    public void setDrawerState(boolean isEnabled) {
        if ( isEnabled ) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_SETTLING);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();

        }
        else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_SETTLING);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.syncState();
        }
    }
    public void loadDataOnFirebase() {
        final IProfile profile =  new ProfileDrawerItem();
        profile.withIdentifier(0);
        profile.withEmail(mAuth.getCurrentUser().getEmail().toString());
        headerResult.addProfile(profile,0);
        /*mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("name-surname")) {
                    profile.withName(snapshot.child("name-surname").getValue().toString());
                    headerResult.updateProfile(profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(mAuth==null){
                    Toast.makeText(MainActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        myStorage= FirebaseStorage.getInstance();
        storageRef= myStorage.getReference();
        StorageReference image = storageRef.child("images/profiles/"+mAuth.getCurrentUser().getUid());

        image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                profile.withIcon(uri);
                headerResult.updateProfile(profile);
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                profile.withIcon(R.mipmap.logo);
                headerResult.updateProfile(profile);
            }
        });*/
    }
}
