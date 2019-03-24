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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private String userType;


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
        final EditText userPNum = ((EditText) findViewById(R.id.pNum));
        final EditText pass = ((EditText) findViewById(R.id.pass));
        final EditText rePass = ((EditText) findViewById(R.id.rePass));

        String sFName = fName.getText().toString();
        String sLName = lName.getText().toString();
        String SUserPNum = userPNum.getText().toString();
        String SPass = pass.getText().toString();
        String SRePass = rePass.getText().toString();
        fName.setError(null);
        lName.setError(null);
        userPNum.setError(null);
        pass.setError(null);
        rePass.setError(null);
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        progressDialog.setTitle("registering...");
        progressDialog.setMessage("Please wait.");
        if (sFName.isEmpty()) {
            userPNum.setError("First Name is empty!");
            userPNum.requestFocus();
        }
        if (sLName.isEmpty()) {
            userPNum.setError("Last Name is empty!");
            userPNum.requestFocus();
        }
        if (SUserPNum.isEmpty()) {
            userPNum.setError("Username is empty!");
            userPNum.requestFocus();
        }
        if (SPass.isEmpty()) {
            pass.setError("Password is empty!");
            pass.requestFocus();
        }
        if (SRePass.isEmpty()) {
            rePass.setError("re-Password is empty!");
            rePass.requestFocus();
        }

        Map<String, String> user = new HashMap<>();
        user.put("fName", sFName);
        user.put("lName", sLName);
        user.put("pNum", SUserPNum);
        user.put("pass", SPass);


        if (SPass.equals(SRePass)) {
            progressDialog.show();
            db.collection(userType)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Blind.class);
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("", e.getMessage());
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}


