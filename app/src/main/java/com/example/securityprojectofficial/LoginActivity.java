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

import com.example.securityprojectofficial.users.BlindUser;
import com.example.securityprojectofficial.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase realTimeDB;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        userType = bundle.getString("userType");
    }

    public void registerButton(View v) {
        Bundle b = new Bundle();
        b.putString("userType", userType);
        Intent intent = new Intent(LoginActivity.this, Register.class);
        intent.putExtras(b);
        startActivity(intent);

    }

    public void loginButton(View v) {

        final EditText phone = (EditText) findViewById(R.id.phone);
        final EditText password = (EditText) findViewById(R.id.password);
        final String sPhone = phone.getText().toString();
        final String sPassword = password.getText().toString();

        if (sPassword.length() > 0 && phone.length() > 0) {
            phone.setError(null);
            password.setError(null);
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("logging in ...");
            progressDialog.setMessage("Please wait.");
            if(sPhone.isEmpty()){
                phone.setError("Email field is empty!");
                phone.requestFocus();
            }
            if(sPassword.isEmpty()){
                password.setError("Password field is empty!");
                password.requestFocus();
            }
            realTimeDB = FirebaseDatabase.getInstance();
            DatabaseReference ref = realTimeDB.getReference().child(userType).child(sPhone);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        Toast.makeText(LoginActivity.this, "Incorrect email"
                                , Toast.LENGTH_SHORT).show();
                    }else{
                        User tmp = dataSnapshot.getValue(User.class);
                        User usr;
                        String type = tmp.getUsrType();
                        if(type.equals("blindUser")){
                            usr = User.blind(tmp);
                        }else{
                            usr = User.friend(tmp);
                        }
                        if(!usr.getPassword().equals(sPassword)){
                            Toast.makeText(LoginActivity.this, "Incorrect password"
                                    , Toast.LENGTH_SHORT).show();
                        }else{
                            login();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "network error"
                            ,Toast.LENGTH_SHORT).show();
                }
            });


        }
    }





    private void login() {
        Intent intent;
        if(userType.equals("blindUser")){
            intent = new Intent(LoginActivity.this, Blind.class);
        }else{
            intent = new Intent(LoginActivity.this, FriendActivity.class);
        }
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }
}
