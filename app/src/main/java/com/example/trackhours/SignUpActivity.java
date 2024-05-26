package com.example.trackhours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText name, username, password, email;
    TextView loginlink;
    Button signupbtn;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        loginlink = findViewById(R.id.loginlink);
        signupbtn = findViewById(R.id.signup);

        loginlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String nameuser = name.getText().toString().trim();
                String emailUser = email.getText().toString().trim();
                String usernameUser = username.getText().toString().trim();
                String passUser = password.getText().toString().trim();

                if (nameuser.isEmpty() || emailUser.isEmpty() || usernameUser.isEmpty() || passUser.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(nameuser, emailUser, usernameUser, passUser);
                reference.child(usernameUser).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("name", nameuser);
                        intent.putExtra("email", emailUser);
                        intent.putExtra("password", passUser);
                        intent.putExtra("username", usernameUser);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
