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
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }
    public void registerButton(View v) {
        Bundle bundle = getIntent().getExtras();
        String userType = bundle.getString("userType");
        bundle.putString("userType", userType);
        Intent intent = new Intent(LoginActivity.this, Register.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void loginButton(View v) {

        final EditText userEmail = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final String sUserEmail = userEmail.getText().toString();
        final String sPassword = password.getText().toString();
        if(sPassword.length() > 0 && userEmail.length() > 0) {
            userEmail.setError(null);
            password.setError(null);
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("logging in ...");
            progressDialog.setMessage("Please wait.");
            if (sUserEmail.isEmpty())
                userEmail.setError("username is empty!");
            if (sPassword.isEmpty())
                password.setError("password is empty!");

            mAuth.signInWithEmailAndPassword(sUserEmail, sPassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("jj", "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "login success.",
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
        else{
            //Error message, the fields is empty
            Toast.makeText(LoginActivity.this, "one or more fields are empty!",
                    Toast.LENGTH_SHORT).show();

        }


    }


    private void login() {
        Intent intent = new Intent(LoginActivity.this, Blind.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }
}
