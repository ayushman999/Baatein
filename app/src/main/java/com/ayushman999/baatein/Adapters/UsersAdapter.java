package com.ayushman999.baatein.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushman999.baatein.Activities.ChatActivity;
import com.ayushman999.baatein.R;
import com.ayushman999.baatein.Models.User;
import com.ayushman999.baatein.databinding.RowConversationBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    ArrayList<User> users;
    Context context;
    public UsersAdapter(Context context, ArrayList<User> users)
    {
        this.context=context;
        this.users=users;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conversation,parent,false);

        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserHolder holder, int position) {
        User user=users.get(position);
        String senderUID= FirebaseAuth.getInstance().getUid();
        String senderRoom=senderUID+user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String lastmessage=(String)snapshot.child("lastMessage").getValue(String.class);
                            if(snapshot.child("lastMessageTime").getValue()!=null) {
                                long time = (long) snapshot.child("lastMessageTime").getValue(Long.class);
                            }
                            if(lastmessage!=null) {
                                holder.binding.lastMessage.setText(lastmessage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
        holder.binding.username.setText(user.getName());
        Glide.with(context).load(user.getProfileImg())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.imageView2);
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, ChatActivity.class);
            intent.putExtra("name",user.getName());
            intent.putExtra("uid",user.getUid());

            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        RowConversationBinding binding;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            binding=RowConversationBinding.bind(itemView);

        }
    }
}
