package com.example.mostafa.talents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mostafa.talents.Models.UserModel;
import com.example.mostafa.talents.Models.scoutmodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class scout extends AppCompatActivity {
    EditText emails_fields,talents_field,passwords_field;
    Button registers;
    ConstraintLayout layout;



    String emailss,talentss,passwordss;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        emails_fields = findViewById(R.id.emails);
        talents_field = findViewById(R.id.talents);
        passwords_field = findViewById(R.id.passwords);


        registers = findViewById(R.id.regs);
        layout=findViewById(R.id.layouts);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);
        registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailss = emails_fields.getText().toString();
                talentss = talents_field.getText().toString();
                passwordss = passwords_field.getText().toString();


                if (TextUtils.isEmpty(emailss))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your email", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    emails_fields.requestFocus();

                    return;
                }
                if (TextUtils.isEmpty(talentss))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your talent", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    talents_field.requestFocus();

                    return;
                }
                if (passwordss.length()>6)             {
                    Snackbar snackbar = Snackbar.make(layout, "your password short", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    passwords_field.requestFocus();

                    return;
                }




                createUser(emailss,passwordss,talentss);

            }
        });
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
    private void createUser(final String emaill, String passwordd, final String talentt) {
        auth.createUserWithEmailAndPassword(emaill, passwordd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendemailverfiy();

                            adduser(emaill, talentt);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void adduser(String e, String t)
    {

        scoutmodel userModel1= new scoutmodel(e,t);

        databaseReference.child("SCouts").setValue(userModel1);

        Intent intent = new Intent(getApplicationContext(), allposts.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            Intent intent = new Intent(getApplicationContext(), allposts.class);
            startActivity(intent);
            finish();
        }
    }



    public  void sendemailverfiy()
    {

        FirebaseUser user=auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(scout.this,"email verfiy send to email please verify email",Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(register.this,Login.class));
                }
                else {
                    Toast.makeText(scout.this,task.getException()+"",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void alreadys(View view) {
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}
