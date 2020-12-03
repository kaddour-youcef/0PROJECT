package com.example.a0project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity {
        // firebase auth
    FirebaseAuth firebaseAuth;
    //view
    TextView mProfilTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //init views
        mProfilTv = findViewById(R.id.profile);

    }

    private void chekUsersStatus(){
    // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null){
            // usier is signed stay here
            // set mail of logged user
            mProfilTv.setText(user.getEmail());
        }else{
            //user not signed in go to main
            startActivity(new Intent(ProfilActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        // chek on start of app
        chekUsersStatus();
        super.onStart();
    }

    //back

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    /*inflate options menu*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // get item id
        int id = item.getItemId();
        if(id == R.id.action_logout){

            firebaseAuth.signOut();
            chekUsersStatus();
        }

        return super.onOptionsItemSelected(item);
    }
}