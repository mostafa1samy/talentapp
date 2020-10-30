package com.example.mostafa.talents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mostafa.talents.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class details extends AppCompatActivity {
    TextView demail1,dphone1,duser1;
    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
      //  String  s=getIntent().getExtras().getString("mos");
        demail1=findViewById(R.id.demail);
        dphone1=findViewById(R.id.dphone);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1= firebaseDatabase1.getReference();

        duser1=findViewById(R.id.duser);
        SharedPreferences sharedPreferences=getSharedPreferences("mostafa",MODE_PRIVATE);
        String id=sharedPreferences.getString("talent",null);
       //
        getData(id);



    }

    private void getData(String id)
    {
        databaseReference1.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                demail1.setText(userModel.getEmail());
                duser1.setText(userModel.getUsername());
                dphone1.setText(userModel.getMobile());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.back) {
            FirebaseAuth.getInstance().signOut();

            //  onBackPressed();
            startActivity(new Intent(getApplicationContext(), allposts.class));
        }
        return super.onOptionsItemSelected(item);

    }
}
