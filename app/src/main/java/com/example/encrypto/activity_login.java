package com.example.encrypto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class activity_login extends AppCompatActivity {

    TextInputEditText email,password;
    String emailStr,passwordStr;
    Button logInBtn;
    LinearLayout signUpRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.userEmailInput);
        password = findViewById(R.id.userPasswordInput);

        logInBtn = findViewById(R.id.loginButton);
        signUpRedirect = findViewById(R.id.signupRedirect);

        signUpRedirect.setOnClickListener(View -> {
            startActivity(new Intent(activity_login.this,activity_signup.class));
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();

                if(!emailStr.isEmpty()){
                    if(!passwordStr.isEmpty()){
                        auth.signInWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(activity_login.this,task -> {

                            if(task.isSuccessful()){
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userName",emailStr.substring(0,emailStr.length()-10).toUpperCase());
                                editor.putBoolean("IsLoggedIn",true);
                                editor.apply();

                                startActivity(new Intent(activity_login.this,ActivityForFragments.class));
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                            if(e instanceof FirebaseAuthInvalidCredentialsException) Toast.makeText(activity_login.this,"Incorrect Password",Toast.LENGTH_SHORT).show();
                            else if(e instanceof FirebaseAuthInvalidUserException) Toast.makeText(activity_login.this,"Account not Registered",Toast.LENGTH_SHORT).show();
                            else Toast.makeText(activity_login.this,"Error :"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        });
                    }
                    else Toast.makeText(activity_login.this,"Password Cannot be Empty",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(activity_login.this,"Email Cannot be Empty",Toast.LENGTH_SHORT).show();
            }
        });

    }
}