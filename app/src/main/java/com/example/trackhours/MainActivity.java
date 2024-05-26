package com.example.trackhours;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView profileusername, profilename, profilepass, profileEmail;
    TextView titlename, titleUsername;

    Button signout, editProfile, deleteProfile, trackTime, payCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileusername = findViewById(R.id.profileusername);
        profilename = findViewById(R.id.profilename);
        profilepass = findViewById(R.id.profilepass);
        profileEmail = findViewById(R.id.profileemail);

        titlename = findViewById(R.id.titlename);

        signout = findViewById(R.id.SignOut);
        editProfile = findViewById(R.id.editprofile);
        deleteProfile = findViewById(R.id.deleteprofile);
        trackTime = findViewById(R.id.buttonTrackTime);
        payCalculator = findViewById(R.id.buttonPayCalculator);

        showDatauser();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        trackTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameMain = profileusername.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkuserData = reference.orderByChild("username").equalTo(usernameMain);

                checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String usernameDB = snapshot.child(usernameMain).child("username").getValue(String.class);

                            Intent intent = new Intent(MainActivity.this, TrackTimeActivity.class);
                            intent.putExtra("username", usernameDB);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        });



        payCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PayCalculatorActivity.class);
                startActivity(intent);
            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProfile();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDatauser();
            }
        });
    }

    public void showDatauser() {
        Intent intent = getIntent();
        titlename.setText("Welcome  " + intent.getStringExtra("name"));
        profilename.setText(intent.getStringExtra("name"));
        profileEmail.setText(intent.getStringExtra("email"));
        profileusername.setText(intent.getStringExtra("username"));
        profilepass.setText(intent.getStringExtra("password"));
    }

    public void passDatauser() {
        String usernameMain = profileusername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuserData = reference.orderByChild("username").equalTo(usernameMain);

        checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String usernameDB = snapshot.child(usernameMain).child("username").getValue(String.class);
                    String passDB = snapshot.child(usernameMain).child("pass").getValue(String.class);
                    String nameDB = snapshot.child(usernameMain).child("name").getValue(String.class);
                    String emailDb = snapshot.child(usernameMain).child("email").getValue(String.class);
                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                    intent.putExtra("name", nameDB);
                    intent.putExtra("email", emailDb);
                    intent.putExtra("password", passDB);
                    intent.putExtra("username", usernameDB);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteProfile() {
        String usernameMain = profileusername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuserData = reference.orderByChild("username").equalTo(usernameMain);
        checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    snapshot.child(usernameMain).getRef().removeValue();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
