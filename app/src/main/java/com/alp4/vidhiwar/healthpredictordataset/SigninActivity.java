package com.alp4.vidhiwar.healthpredictordataset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        progressdialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            //start profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),UserActivity.class));
        }

        buttonSignin = (Button) findViewById(R.id.buttonSignin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignup);

        buttonSignin.setOnClickListener(this);
        editTextPassword.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    private void userLogin(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){

            //empty email

            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();

            return;
        }

        if(TextUtils.isEmpty(password)){

            //empty password

            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();

            return;
        }
        //validated

        progressdialog.setMessage("Logging in...");
        progressdialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            if (progressdialog.isShowing())
                                progressdialog.dismiss();

                            //login profile
                            finish();
                            startActivity(new Intent(getApplicationContext(),UserActivity.class));

                        }

                        else{
                            if (progressdialog.isShowing())
                                progressdialog.dismiss();

                            Toast.makeText(SigninActivity.this,"Login Failed! Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        if (view == buttonSignin){

            userLogin();
        }

        if(view == textViewSignup){

            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }
    }
}

