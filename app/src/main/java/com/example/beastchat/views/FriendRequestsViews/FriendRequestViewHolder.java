package com.example.beastchat.views.FriendRequestsViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.entities.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_friend_request_userPicture)
    ImageView userPicture;

    @BindView(R.id.list_friend_request_userName)
    TextView userName;

    @BindView(R.id.list_friend_request_acceptRequest)
    ImageView approveImageView;

    @BindView(R.id.list_friend_request_rejectRequest)
    ImageView rejectImageView;

    public FriendRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
    public  void populate(Context context, User user)
    {
        itemView.setTag(user);
        userName.setText(user.getUsername());
        Picasso.with(context)
                .load(user.getUserPicture())
                .into(userPicture);
    }
}
