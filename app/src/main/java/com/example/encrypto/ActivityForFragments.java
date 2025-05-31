package com.example.encrypto;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityForFragments extends AppCompatActivity {
    Button allChats,groups,contacts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_for_fragments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        HomeScreen homeFragment = new HomeScreen();
        GroupsFragment groupsFragment = new GroupsFragment();
        FragmentContacts contactsFragment = new FragmentContacts();

        allChats = findViewById(R.id.allChats);
        groups = findViewById(R.id.Groups);
        contacts = findViewById(R.id.Contacts);

        TextView name = findViewById(R.id.Name);

        SharedPreferences sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);

        name.setText(sharedPreferences.getString("userName","No Name"));



        setFragment(false,homeFragment,allChats);

        allChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(true,homeFragment,allChats);
            }
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(true,groupsFragment,groups);
            }
        });
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(true,contactsFragment,contacts);
            }
        });





    }

    private void setFragment(boolean flag, Fragment fragment,Button btn) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(flag){
            ft.replace(R.id.FrameLayout,fragment);
        }
        else{
            ft.add(R.id.FrameLayout,fragment);
        }

        allChats.setTextColor(getResources().getColor(R.color.paleBlack));
        allChats.setBackgroundColor(ContextCompat.getColor(ActivityForFragments.this, R.color.paleWhite));

        groups.setTextColor(getResources().getColor(R.color.paleBlack));
        groups.setBackgroundColor(ContextCompat.getColor(ActivityForFragments.this, R.color.paleWhite));

        contacts.setTextColor(getResources().getColor(R.color.paleBlack));
        contacts.setBackgroundColor(ContextCompat.getColor(ActivityForFragments.this, R.color.paleWhite));

        btn.setTextColor(getResources().getColor(R.color.white));
        btn.setBackgroundColor(ContextCompat.getColor(ActivityForFragments.this, R.color.purple));




        ft.commit();
    }
}