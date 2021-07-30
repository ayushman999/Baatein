package com.ayushman999.baatein.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushman999.baatein.Models.Message;
import com.ayushman999.baatein.R;
import com.ayushman999.baatein.databinding.ItemReceiveBinding;
import com.ayushman999.baatein.databinding.ItemSendBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;


    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT)
        {
            View view=LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new SentViewHolder(view);
        }
        else
        {
            View view=LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderID()))
        {
            return ITEM_SENT;
        }
        else
        {
            return ITEM_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=messages.get(position);
        int[] reactions=new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass().equals(SentViewHolder.class))
            {
                SentViewHolder viewHolder=(SentViewHolder)holder;
                viewHolder.binding.imageView3.setImageResource(reactions[pos]);
                viewHolder.binding.imageView3.setVisibility(View.VISIBLE);
            }
            {
                ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
                viewHolder.binding.imageView4.setImageResource(reactions[pos]);
                viewHolder.binding.imageView4.setVisibility(View.VISIBLE);
            }
            return true; // true is closing popup, false is requesting a new selection
        });

        if(holder.getClass().equals(SentViewHolder.class))
        {
            SentViewHolder viewHolder=(SentViewHolder)holder;
            viewHolder.binding.senderText.setText(message.getMessage());
            viewHolder.binding.senderText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }
        else
        {
            ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
            viewHolder.binding.recieverText.setText(message.getMessage());
            viewHolder.binding.recieverText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {
        ItemSendBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemSendBinding.bind(itemView);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ItemReceiveBinding binding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemReceiveBinding.bind(itemView);
        }
    }

}
