package com.alp4.vidhiwar.healthpredictordataset;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView textViewEmail;
    private ListView listViewMeasurementData;
    private List<measurementData> listData;
    private DatabaseReference dataset;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        dataset = FirebaseDatabase.getInstance().getReference(user.getUid());

        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SigninActivity.class));
        }

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        listViewMeasurementData = (ListView) findViewById(R.id.listViewMeasurementData);

        textViewEmail.setText("Welcome  "+user.getEmail());

        listData = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection

        switch (item.getItemId()) {
            case R.id.menu_item_new_entry:
                startActivity(new Intent(this, DataActivity.class));
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, SigninActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        dataset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listData.clear();
                for(DataSnapshot dataEntrySnapshot : dataSnapshot.getChildren())
                {
                    measurementData newData = dataEntrySnapshot.getValue(measurementData.class);
                    listData.add(newData);
                }

                DataList adapter = new DataList(UserActivity.this,listData);
                listViewMeasurementData.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
