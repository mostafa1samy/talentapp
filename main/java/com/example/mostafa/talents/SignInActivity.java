package com.example.mostafa.talents;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
ConstraintLayout layout;
    EditText email_field,password_field;
    Button register;
    FirebaseAuth auth;

    ProgressDialog progressDialog;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email_field = findViewById(R.id.email1);
        password_field = findViewById(R.id.password1);
        register = findViewById(R.id.reg1);
        layout = findViewById(R.id.lay);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                email = email_field.getText().toString();
                password = password_field.getText().toString();

                if (TextUtils.isEmpty(email))
                {
                    Snackbar snackbar = Snackbar.make(layout, "enter your email", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    email_field.requestFocus();

                    return;
                }

                if (password.length() < 6)
                {
                    Snackbar snackbar = Snackbar.make(layout, "password is too short", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    password_field.requestFocus();

                    return;
                }

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("please wait ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                signIn(email,password);
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

    private void signIn(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();



                            Intent intent = new Intent(getApplicationContext(), StartActivity.class);

                            startActivity(intent);
                            finish();
                        } else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}
