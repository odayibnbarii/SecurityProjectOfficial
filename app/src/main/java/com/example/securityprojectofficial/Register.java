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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void regButton(View v) {
        final EditText userEmail = (EditText) findViewById(R.id.emailR);
        final EditText password = (EditText) findViewById(R.id.passwordR);
        final EditText repassword = (EditText) findViewById(R.id.repasswordR);
        final String sUserEmail = userEmail.getText().toString();
        final String sPassword = password.getText().toString();
        final String sRePassword = password.getText().toString();

        userEmail.setError(null);
        password.setError(null);
        repassword.setError(null);
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("registering...");
        progressDialog.setMessage("Please wait.");
        if (sUserEmail.isEmpty()) {
            userEmail.setError("Username is empty!");
            userEmail.requestFocus();
        }
        if (sPassword.isEmpty()) {
            password.setError("Password is empty!");
            password.requestFocus();
        }
        if(sRePassword.isEmpty()) {
            repassword.setError("re-Password is empty!");
            repassword.requestFocus();
        }
        final FirebaseAuth mAuth= FirebaseAuth.getInstance();;

        if(!sUserEmail.isEmpty() && !sPassword.isEmpty() &&!sRePassword.isEmpty() && sPassword.equals(sRePassword) ) {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(sUserEmail, sPassword).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            password.setError("Password must be at least 6 characters!");
                            password.requestFocus();
                            progressDialog.dismiss();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            userEmail.setError("Email address is badly formatted!");
                            userEmail.requestFocus();
                            progressDialog.dismiss();
                        } catch (FirebaseAuthUserCollisionException e) {
                            userEmail.setError("Email address is exist!");
                            userEmail.requestFocus();
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            Log.e("", e.getMessage());
                            progressDialog.dismiss();
                        }
                    }
                }

             });
        }
        }






    }


