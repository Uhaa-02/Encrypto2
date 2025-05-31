package com.example.encrypto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeScreen extends Fragment {

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home_screen, container, false);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Users", Context.MODE_PRIVATE);

        String userName = sharedPreferences.getString("userName",null);




        ImageButton addBtn = v.findViewById(R.id.AddBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddNewFriend.class));
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ContactsDataModel> arrayList = new ArrayList<>();


        ContactsAdapter adapter = new ContactsAdapter(getContext(),arrayList,true);

        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userName).child("Friends");

        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){
                    for (DataSnapshot snapshot:task.getResult().getChildren()){
                        arrayList.add(new ContactsDataModel(snapshot.getValue(String.class),"Hi Harish","7:22"));
                    }
                    adapter.notifyDataSetChanged();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }
}