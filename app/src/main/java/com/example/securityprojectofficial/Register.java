package com.example.securityprojectofficial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import com.example.securityprojectofficial.Security.CipherDatabase;
import com.example.securityprojectofficial.users.BlindUser;
import com.example.securityprojectofficial.users.FriendUser;
import com.example.securityprojectofficial.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {
    private String userType;
    private String phonee;
    private FirebaseDatabase rlDb;
    private FirebaseAuth mAuth;
    private CipherDatabase CD = new CipherDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bundle b = getIntent().getExtras();
        userType = b.getString("userType");
    }

    public void regButton(View v) {
        final EditText fName = ((EditText) findViewById(R.id.fName));
        final EditText lName = ((EditText) findViewById(R.id.lName));
        final EditText phone = ((EditText) findViewById(R.id.phone));
        final EditText pass = ((EditText) findViewById(R.id.pass));
        final EditText rePass = ((EditText) findViewById(R.id.rePass));

        final String sFName = fName.getText().toString();
        final String sLName = lName.getText().toString();
        final String sPhone = phone.getText().toString();
        phonee=sPhone;
        final String SPass = pass.getText().toString();
        String SRePass = rePass.getText().toString();
        fName.setError(null);
        lName.setError(null);
        phone.setError(null);
        pass.setError(null);
        rePass.setError(null);
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Please wait...");
        if (sFName.isEmpty()) {
            fName.setError("First Name is empty!");
            fName.requestFocus();
        }
        if (sLName.isEmpty()) {
            lName.setError("Last Name is empty!");
            lName.requestFocus();
        }
        if (sPhone.isEmpty()) {
            phone.setError("Phone is empty!");
            phone.requestFocus();
        }
        if (SPass.isEmpty()) {
            pass.setError("Password is empty!");
            pass.requestFocus();
        }
        if (SRePass.isEmpty()) {
            rePass.setError("re-Password is empty!");
            rePass.requestFocus();
        }
        if(isCorrect(sPhone, SPass, SRePass, sFName, sLName)) {
            if (SPass.equals(SRePass)) {
                progressDialog.show();
                rlDb = FirebaseDatabase.getInstance();
                DatabaseReference ref = rlDb.getReference().child(userType).child(sPhone);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(Register.this, "Phone has registered before!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            User usr;
                            if (userType.equals("blindUser")) {
                                usr = CD.encryptUser(new BlindUser(sPhone, SPass, sFName, sLName, userType));
                            } else {
                                usr = CD.encryptUser(new FriendUser(sPhone, SPass, sFName, sLName, userType));
                            }
                            DatabaseReference ref = rlDb.getReference();
                            ref.child(userType).child(sPhone).setValue(usr);
                            login();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Register.this, "passwords doesn't match!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
            } else {
                Toast.makeText(Register.this, "passwords doesn't match!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isCorrect(String phone, String password,String rePass, String fName, String lName){
        return !phone.isEmpty() &&
                !password.isEmpty() &&
                !rePass.isEmpty() &&
                !fName.isEmpty() &&
                !lName.isEmpty();
    }
    private void login() {
        Intent intent;
        if(userType.equals("blindUser")){
            intent = new Intent(Register.this, Blind.class);
        }else{
            intent = new Intent(Register.this, FriendActivity.class);
        }
        Bundle b = new Bundle();
        b.putString("phone", phonee);
        intent.putExtras(b);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }


}


