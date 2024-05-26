package com.example.trackhours;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPass;
    Button loginbtn;
    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.usernamelogin);
        loginPass = findViewById(R.id.passwordlogin);
        loginbtn = findViewById(R.id.login);
        signupLink = findViewById(R.id.signuplink);

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    public void checkUser() {
        String usernameLogin = loginUsername.getText().toString().trim();
        String passLogin = loginPass.getText().toString().trim();

        if (usernameLogin.isEmpty() || passLogin.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserData = reference.orderByChild("username").equalTo(usernameLogin);

        checkUserData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (user != null && user.getPass().equals(passLogin)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", user.getName());
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("password", user.getPass());
                            intent.putExtra("username", user.getUsername());
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
