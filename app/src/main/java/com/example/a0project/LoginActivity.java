package com.example.a0project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText mEmailEt, mPasswordEt;
    TextView notHaveaccount;
    TextView RecoverPass;
    Button mLogin;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        //enable back boutton
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
       // init
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mLogin = findViewById(R.id. LoginBtn);
        notHaveaccount = findViewById(R.id.no_have_accountTv);
        RecoverPass = findViewById(R.id.Recover_Password);

        // fire base

        mAuth = FirebaseAuth.getInstance();
        // login button
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email = mEmailEt.getText().toString();
            String password = mPasswordEt.getText().toString();
            // chek mail valide

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailEt.setError("Invalide Email");
                    mEmailEt.setFocusable(true);
                }else if (password.length() < 6){
                    mPasswordEt.setError("password to short");
                    mPasswordEt.setFocusable(true);
                }else{

                    LoginUser(email,password);
                }

            }
        });

        // no account button
        notHaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,Reg_Activity.class));
                finish();
            }
        });

        // recover password

        RecoverPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             showRecoverPasswordDialog();
            }
        });
        //init progress dialog
        progressDialog = new ProgressDialog(this);

    }

    private void showRecoverPasswordDialog() {
        // Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Recover Password");
        // set layout linear layout
        LinearLayout linearLayout =  new LinearLayout(this);

        // views to set in dialog
        final AppCompatEditText emailEt = new AppCompatEditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);
        //Buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            // input email

                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });

        //Buttons cansel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             //dissmiss dialog
                dialogInterface.dismiss();
            }
        });
        // show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        // dialog
        progressDialog.setMessage("Sending email ...");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"Email sent", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
             // get and show proper error message
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void LoginUser(String email, String password) {
        progressDialog.setMessage("Logging in ...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // progress dialog dissmiss
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // start profile activity
                            startActivity(new Intent(LoginActivity.this, ProfilActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                // error get and show error message
                Toast.makeText(LoginActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();// go back
        return super.onSupportNavigateUp();
    }
}