package com.ayushman999.baatein.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushman999.baatein.Activities.MainActivity;
import com.ayushman999.baatein.Models.Status;
import com.ayushman999.baatein.Models.UserStatus;
import com.ayushman999.baatein.R;
import com.ayushman999.baatein.databinding.ItemStatusBinding;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewHolder> {
    Context context;
    ArrayList<UserStatus> userStatuses;

    public TopStatusAdapter(Context context, ArrayList<UserStatus> userStatuses) {
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @NotNull
    @Override
    public TopStatusViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status,parent,false);
        return new TopStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TopStatusViewHolder holder, int position) {
        UserStatus userStatus=userStatuses.get(position);
        Status lastStatus=userStatus.getStatuses().get(userStatus.getStatuses().size()-1);
        Glide.with(context).load(lastStatus.getImageURL()).into(holder.binding.userImage);
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());
        holder.binding.circularStatusView.setOnClickListener(v -> {
            ArrayList<MyStory> myStories = new ArrayList<>();
            for(Status status:userStatus.getStatuses())
            {
                myStories.add(new MyStory(status.getImageURL()));
            }
            new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                    .setStoriesList(myStories) // Required
                    .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                    .setTitleText(userStatus.getName()) // Default is Hidden
                    .setSubtitleText("") // Default is Hidden
                    .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            //your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {
                            //your action
                        }
                    }) // Optional Listeners
                    .build() // Must be called before calling show method
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewHolder extends RecyclerView.ViewHolder {
        ItemStatusBinding binding;
        public TopStatusViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=ItemStatusBinding.bind(itemView);
        }
    }
}
