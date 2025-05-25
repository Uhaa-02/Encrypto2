package com.example.encrypto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class activity_signup extends AppCompatActivity {

    EditText userName,email,password;
    String userNameStr,emailStr,passwordStr;
    Button signUpBtn;
    TextView loginRedirect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("IsLoggedIn", false)) startActivity(new Intent(activity_signup.this,MainActivity.class));



        userName = findViewById(R.id.usernameInput);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);


        signUpBtn = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);

        loginRedirect.setOnClickListener(View -> {
            startActivity(new Intent(activity_signup.this,activity_login.class));
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameStr = userName.getText().toString();
                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();

                if (!userNameStr.isEmpty()) {
                    if (!emailStr.isEmpty()) {
                        if (!passwordStr.isEmpty()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(activity_signup.this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(activity_signup.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("IsLoggedIn", true);
                                    editor.putString("userName",userNameStr);
                                    editor.putString("userEmail", emailStr);
                                    editor.apply();

                                    startActivity(new Intent(activity_signup.this, MainActivity.class));
                                    finish();
                                } else {
                                    Log.d("error", "Failure : " + task.getException());
                                    Toast.makeText(activity_signup.this, "Failure : " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(e -> {
                                if (e instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(activity_signup.this, "Account Already Exist", Toast.LENGTH_SHORT).show();
                                if (e instanceof FirebaseAuthWeakPasswordException)
                                    Toast.makeText(activity_signup.this, "Password too weak", Toast.LENGTH_SHORT).show();
                                if (e instanceof FirebaseAuthInvalidCredentialsException)
                                    Toast.makeText(activity_signup.this, "Email not valid", Toast.LENGTH_SHORT).show();
                            });
                        } else
                            Toast.makeText(activity_signup.this, "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(activity_signup.this, "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(activity_signup.this, "UserName Cannot be Empty", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}