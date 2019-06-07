package com.example.securityprojectofficial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securityprojectofficial.Security.CipherDatabase;
import com.example.securityprojectofficial.users.Friends;
import com.example.securityprojectofficial.users.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;


public class FriendActivity extends AppCompatActivity {
    public FirebaseDatabase realTimeDB;
    public CipherDatabase CD = new CipherDatabase();

    public EditText frUser;
    public User user;
    public String phone;
    public String userType;
    public boolean userIsHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        frUser = (EditText)findViewById(R.id.editText);
        Button b = (Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend();
            }
        });
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
        userType=bundle.getString("userType");
        userIsHere=false;
        setUser();
        getRequest();
    }
    public void AddFriend(){
        final String username = frUser.getText().toString();
        frUser.setError(NULL);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference ref = db.getReference().child("blindUser").child(username);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(FriendActivity.this, "User Not founded"
                            , Toast.LENGTH_SHORT).show();
                }else{
                    while(!userIsHere){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    final FirebaseDatabase tmpDb = FirebaseDatabase.getInstance();
                    DatabaseReference tmpRef = tmpDb.getReference().child("friends").child(username);
                    tmpRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Friends f =dataSnapshot.getValue(Friends.class);
                                if(f.friends.size()==2){
                                    Toast.makeText(FriendActivity.this, "Max. 2 friends for user!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    f.addFriend(user.getPhone());
                                    DatabaseReference refe = tmpDb.getReference();
                                    refe.child("friends").child(username).setValue(f);

                                }
                            } else {

                                Friends f = new Friends();
                                f.addFriend(user.getPhone());
                                DatabaseReference refe = tmpDb.getReference();
                                refe.child("friends").child(username).setValue(f);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(FriendActivity.this, "Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(FriendActivity.this, "User Added Successfuly"
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendActivity.this, "network error"
                        ,Toast.LENGTH_SHORT).show();
                Log.e("Cancelled", "Cancelled method");
            }
        });


    }
    private void setUser(){
        realTimeDB = FirebaseDatabase.getInstance();
        DatabaseReference ref = realTimeDB.getReference().child(userType).child(phone);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Log.e(correct method, correct method)");

                if(userType.equals("friendUser")){
                    user = CD.decryptUser(dataSnapshot.getValue(User.class));
                }else{
                    user = CD.decryptUser(dataSnapshot.getValue(User.class));
                }
                userIsHere=true;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendActivity.this, "network error"
                        ,Toast.LENGTH_SHORT).show();
                Log.e("Cancelled", "Cancelled method");
            }
        });

    }
    public void  getRequest(){
        realTimeDB = FirebaseDatabase.getInstance();
        DatabaseReference ref = realTimeDB.getReference().child("request").child(phone);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    while (!userIsHere) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    request r = dataSnapshot.getValue(request.class);
                    TextView f = (TextView) findViewById(R.id.fText);
                    TextView l = (TextView) findViewById(R.id.lText);
                    TextView d = (TextView) findViewById(R.id.dText);
                    f.setText(r.name);
                    l.setText(r.location);
                    d.setText(r.date);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendActivity.this, "network error"
                        ,Toast.LENGTH_SHORT).show();
                Log.e("Cancelled", "Cancelled method");
            }
        });

    }
}
