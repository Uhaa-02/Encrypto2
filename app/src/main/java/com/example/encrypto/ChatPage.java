package com.example.encrypto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.HardwareRenderer;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {
    String frndName;
    DatabaseReference reference;
    String roomId;
    ArrayList<MessageDataModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;
            if (keypadHeight > screenHeight * 0.15) {
                // keyboard is opened
                Log.d("Keyboard", "Keyboard is visible");
            } else {
                Log.d("Keyboard", "Keyboard is hidden");
            }
        });



        Intent i = getIntent();
        if(i != null){
            frndName = i.getStringExtra("FrndName");
        }

        ImageButton sendBtn = findViewById(R.id.sendBtn);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TextInputEditText msg = findViewById(R.id.Msg);


        SharedPreferences sharedPreferences = getSharedPreferences("Users",MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName",null);

        if (userName != null && frndName != null) {
            if (userName.compareTo(frndName) > 0) {
                roomId = userName + "_" + frndName;
            } else {
                roomId = frndName + "_" + userName;
            }
        } else {
            // Fallback or log error
            Log.e("ChatPage", "userName or frndName is null");
            roomId = "default_room"; // or handle gracefully
        }

        reference = FirebaseDatabase.getInstance().getReference("Conversations").child(roomId);



        recyclerView.setLayoutManager(new LinearLayoutManager(ChatPage.this));

        MessageAdapter adapter = new MessageAdapter(ChatPage.this,arrayList,sharedPreferences);

        recyclerView.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                arrayList.clear();
                for(DataSnapshot snapshot:data.getChildren()){
                    arrayList.add(new MessageDataModel(snapshot.child("message").getValue(String.class),snapshot.child("sender").getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(arrayList.size()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!msg.getText().toString().isEmpty()) {
                    reference.push().setValue(new MessageDataModel(msg.getText().toString(),userName)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                msg.setText("");
                            }
                            else {
                                Toast.makeText(ChatPage.this, "An Error Occured sending the Message", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChatPage.this, "An Error Occured sending the Message", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}