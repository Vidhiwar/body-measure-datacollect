package com.alp4.vidhiwar.healthpredictordataset;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

/*
Author: Vidhiwar Singh
Email: vidhiwarsingh@gmail.com
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressdialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressdialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){

            //start profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),UserActivity.class));

        }
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        editTextPassword.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){
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

        progressdialog.setMessage("Registering User...");
        progressdialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            if (progressdialog.isShowing())
                                progressdialog.dismiss();

                            finish();
                            startActivity(new Intent(getApplicationContext(),UserActivity.class));

                            Toast.makeText(SignupActivity.this,"Successfull Registration",Toast.LENGTH_SHORT).show();

                        }

                        else{
                            if (progressdialog.isShowing())
                                progressdialog.dismiss();

                            Toast.makeText(SignupActivity.this,"Registration Failed! Try again",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {

        if (view == buttonRegister){

            registerUser();
        }

        if(view == textViewSignin){
            //open login activity
            finish();
            startActivity(new Intent(this,SigninActivity.class));
        }
    }
}
