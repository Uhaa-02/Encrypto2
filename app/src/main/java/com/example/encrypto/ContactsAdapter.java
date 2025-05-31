package com.example.encrypto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    DatabaseReference reference;
    Context context;
    int pos;
    ArrayList<ContactsDataModel> arrayList;
    boolean flag;
    ContactsAdapter(Context context, ArrayList<ContactsDataModel> arrayList,boolean flag){
        this.context = context;
        this.arrayList = arrayList;
        this.flag = flag;

    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.each_contact,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        holder.name.setText(arrayList.get(position).name);
        holder.msg.setText(arrayList.get(position).msg);
        holder.time.setText("0"+arrayList.get(position).time+" AM");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    Intent i = new Intent(context, ChatPage.class);
                    i.putExtra("FrndName",arrayList.get(holder.getAdapterPosition()).name);
                    context.startActivity(i);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context,view);

                if(!flag){
                    popupMenu.getMenu().add("Add Contact to Friends");

                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("Add Contact to Friends")) {

                                SharedPreferences sharedPreferences = context.getSharedPreferences("Users", Context.MODE_PRIVATE);

                                String userName = sharedPreferences.getString("userName", null);

                                reference = FirebaseDatabase.getInstance().getReference("Users");

                                reference.child(userName).child("Friends").child(arrayList.get(holder.getAdapterPosition()).name).setValue(arrayList.get(holder.getAdapterPosition()).name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Added to Friend Succesfully", Toast.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(context, ActivityForFragments.class));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failure " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            return true;
                        }
                    });
                }
                else{
                    popupMenu.getMenu().add("Remove Contact from Friends");

                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getTitle().equals("Remove Contact from Friends")) {

                                SharedPreferences sharedPreferences = context.getSharedPreferences("Users", Context.MODE_PRIVATE);

                                String userName = sharedPreferences.getString("userName", null);

                                reference = FirebaseDatabase.getInstance().getReference("Users");

                                reference.child(userName).child("Friends").child(arrayList.get(holder.getAdapterPosition()).name).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Friend Removed from Friend Succesfully", Toast.LENGTH_SHORT).show();

                                            context.startActivity(new Intent(context, HomeScreen.class));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failure " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            return true;
                        }
                    });
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView name,msg,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.Profile);
            name = itemView.findViewById(R.id.Name);
            msg = itemView.findViewById(R.id.Message);
            time = itemView.findViewById(R.id.Time);
        }
    }
}
