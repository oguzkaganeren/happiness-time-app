package com.example.tav.happinesstime;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PointCampaign extends AppCompatActivity {
    private String TAG = PointCampaign.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Integer point;
    // URL to get contacts JSON
    private static String url = "http://35.159.15.121:8080/foodAndBeverage/restaurantList?language=tr&location=ist";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_campaign);
        contactList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("points") && mAuth != null) {
                    point = snapshot.child("points").getValue(Integer.class);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }


    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PointCampaign.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("title");
                        String email = c.getString("rate");
                        String puan = c.getString("distance");
                        String status = c.getString("elevation");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value


                        int a=0;

                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        if(email.equalsIgnoreCase("1")){
                            contact.put("puan", "1000");
                            a = Integer.parseInt(contact.put("puan", "1000"));
                            if(point>a){contact.put("email","%25 indirim kullanılabilir");}
                            else{contact.put("email","Herhangi bir indirim bulunmamaktadır.");}

                        }
                        else if(email.equalsIgnoreCase("2")){
                            contact.put("puan", "2000");
                            a = Integer.parseInt(contact.put("puan", "2000"));
                            if(point>=a){contact.put("email","%20 indirim kullanılabilir");}
                            else  if(point<2000 && point>=1000 ){contact.put("email","1000 puanlara bakın");}
                            else{contact.put("email","Herhangi bir indirim bulunmamaktadır.");}
                        }
                        else if(email.equalsIgnoreCase("3")){
                            contact.put("puan", "3000");
                            a = Integer.parseInt(contact.put("puan", "3000"));
                            if(point>=a){contact.put("email","%15 indirim kullanılabilir");}
                            else if(point<2000 && point>=1000 ){contact.put("email","1000 puanlara bakın");}
                            else if(point<3000 && point>=2000 ){contact.put("email","1000 ve 2000 puanlara bakın");}
                            else{contact.put("email","Herhangi bir indirim bulunmamaktadır.");}
                        }
                        else if(email.equalsIgnoreCase("4")){
                            contact.put("puan", "4000");
                            a = Integer.parseInt(contact.put("puan", "4000"));
                            if(point>=a){contact.put("email","%10 indirim kullanılabilir");}
                            else if(point<2000 && point>=1000 ){contact.put("email","1000 puanlara bakın");}
                            else if(point<3000 && point>=2000 ){contact.put("email","1000 ve 2000 puanlara bakın");}
                            else if(point<4000 && point>=3000 ){contact.put("email","1000,2000 ve 3000 puanlara bakın");}

                            else{contact.put("email","Herhangi bir indirim bulunmamaktadır.");}
                        }

                        else{
                            contact.put("puan", "5000 puan");
                        }


                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog


            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(

                    PointCampaign.this, contactList,
                    R.layout.list_item, new String[]{"name","puan","email"}, new int[]{R.id.name,
                    R.id.puan,R.id.email});





            lv.setAdapter(adapter);
        }

    }
}