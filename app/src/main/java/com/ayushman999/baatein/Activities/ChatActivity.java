package com.ayushman999.baatein.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.ayushman999.baatein.Adapters.MessagesAdapter;
import com.ayushman999.baatein.Models.Message;
import com.ayushman999.baatein.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    MessagesAdapter adapter;
    ArrayList<Message> messages;
    String senderRoom,receiverRoom;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        Intent data=getIntent();
        String name=data.getStringExtra("name");
        String senderUID=data.getStringExtra("uid");
        String receiverUID= FirebaseAuth.getInstance().getUid();
        senderRoom=senderUID+receiverUID;
        receiverRoom=receiverUID+senderUID;

        getSupportActionBar().setTitle(name);
        binding.sendButton.setOnClickListener(v -> {
            Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
            String messageText=binding.messageBox.getText().toString();
            binding.messageBox.setText("");
            Date date=new Date();
            Message message=new Message(messageText,senderUID,date.getTime());
            database.getReference().child("Chats").child(senderRoom)
                    .child("messages")
                    .push()
                    .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference().child("Chats").child(receiverRoom)
                            .child("messages")
                            .push()
                            .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            });

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messages=new ArrayList<>();
        adapter=new MessagesAdapter(this,messages);
        binding.recyclerView.setAdapter(adapter);

        database.getReference().child("Chats")
                .child(receiverRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        if(snapshot.exists()) {
                            for (DataSnapshot ss : snapshot.getChildren()) {
                                Message message = ss.getValue(Message.class);
                                messages.add(message);

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}