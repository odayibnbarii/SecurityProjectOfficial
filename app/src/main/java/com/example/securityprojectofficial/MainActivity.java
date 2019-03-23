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
    private FirebaseAuth mAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, Blind.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();

        }
    }

    public void registerButton(View v) {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);

    }

    public void loginButton(View v) {

        final EditText userEmail = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final String sUserEmail = userEmail.getText().toString();
        final String sPassword = password.getText().toString();
        userEmail.setError(null);
        password.setError(null);
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("loging...");
        progressDialog.setMessage("Please wait.");
        if (sUserEmail.isEmpty())
            userEmail.setError("Username is empty!");
        if (sPassword.isEmpty())
            password.setError("Password is empty!");

        mAuth.signInWithEmailAndPassword(sUserEmail, sPassword)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("jj", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "login success.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            login();
                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                progressDialog.dismiss();
                                password.setError("Invalid password!");
                                password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                progressDialog.dismiss();
                                password.setError("Invalid password!");
                                password.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                progressDialog.dismiss();
                                Log.e("EMAIL", e.getMessage());
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Log.e("", e.getMessage());
                            }


                        }

                        // ...
                    }
                });
    }


    private void login() {
        Intent intent = new Intent(MainActivity.this, Blind.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }
}



