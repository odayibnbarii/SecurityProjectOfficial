package com.example.securityprojectofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, Blind.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        }
    }
    public void blindButton(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userType", "blindUser");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void friendButton(View view){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("userType", "friendUser");
        intent.putExtras(bundle);
        intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
    }

}



