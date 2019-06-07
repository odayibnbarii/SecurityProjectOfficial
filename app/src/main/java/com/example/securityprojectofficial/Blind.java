package com.example.securityprojectofficial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import com.example.securityprojectofficial.Security.CipherDatabase;
import com.example.securityprojectofficial.users.Friends;
import com.example.securityprojectofficial.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Blind extends AppCompatActivity {
    public String phone;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blind);
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");


    }
    public void logout(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(Blind.this);
        alert.setMessage("Do you want to log out?").setTitle("Log out")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();
                        Intent intent =new Intent(Blind.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();// Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();


    }


    public void start(View v){
        Intent intent =new Intent(Blind.this,NaviActivity.class);
        Bundle b = new Bundle();
        b.putString("phone", phone);
        intent.putExtras(b);

        startActivity(intent);
        finish();

    }

}
