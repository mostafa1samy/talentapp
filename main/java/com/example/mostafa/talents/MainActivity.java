package com.example.mostafa.talents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mostafa.talents.Models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText email_field,username_field,password_field,mobile_field,address_field;
    Button register;
    ConstraintLayout layout;
    Spinner spinner;
    String user_id ;

    String email,username,password,mobile,address;
List<String> talents;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_field = findViewById(R.id.email);
        username_field = findViewById(R.id.user_name);
        password_field = findViewById(R.id.password);

        mobile_field = findViewById(R.id.phone);
        address_field = findViewById(R.id.address);
        register = findViewById(R.id.reg);
       layout=findViewById(R.id.layout);

        auth = FirebaseAuth.getInstance();
        spinner=findViewById(R.id.talents);
        talents= new ArrayList<>();
        talents.add("play sport");
        talents.add("Art");
        talents.add("Reading");
        talents.add("Write");
        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,talents);
        spinner.setAdapter(adapter);

         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (talents.get(position).equals("Write")){
                    user_id="Write";
                   // return;
                }
                else if (talents.get(position).equals("play sport")){
                    user_id="play sport";
                  //  return;
                }
                else if (talents.get(position).equals("Reading")){
                    user_id="Reading";
                  //  return;
                }
                else if (talents.get(position).equals("Art")){
                    user_id="Art";
                 //   return;
                }
                SharedPreferences.Editor editor=getSharedPreferences("mostafa",MODE_PRIVATE).edit();
                editor.putString("talent",user_id);
                editor.apply();
                /*
                Intent intent=new Intent(getApplicationContext(),details.class);
                intent.putExtra("mos",user_id);
                startActivity(intent);


                */


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);

            }
        });





        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = email_field.getText().toString();
                username = username_field.getText().toString();
                password = password_field.getText().toString();

                mobile = mobile_field.getText().toString();
                address = address_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your email", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    email_field.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(username))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your name", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    username_field.requestFocus();

                    return;
                }

                if (password.length() < 6)
                {
                    Snackbar snackbar = Snackbar.make(layout, "password is too short", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    password_field.requestFocus();

                    return;
                }




                if (TextUtils.isEmpty(mobile))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your mobile number", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    mobile_field.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(address))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your address", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                   // address_field.setError("enter your address");
                    address_field.requestFocus();

                    return;
                }

                createUser(email,password,username,mobile,address);
            }
        });
    }

    private void createUser(final String email, String password, final String username, final String mobile, final String address)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            sendemailverfiy();

                            adduser(email,username,mobile,address,user_id);
                        } else
                        {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void adduser(String email, String username, String mobile, String address, String user__id)
    {

        UserModel userModel = new UserModel(email,username,mobile,address);

        databaseReference.child("Users").child(user__id).setValue(userModel);

        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=item.getItemId();
        if (i==R.id.back){
            FirebaseAuth.getInstance().signOut();

            onBackPressed();
            startActivity(new Intent(getApplicationContext(),splash.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void already(View view) {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
    public  void sendemailverfiy()
    {

        FirebaseUser user=auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"email verfiy send to email please verify email",Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(register.this,Login.class));
                }
                else {
                    Toast.makeText(MainActivity.this,task.getException()+"",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


}


