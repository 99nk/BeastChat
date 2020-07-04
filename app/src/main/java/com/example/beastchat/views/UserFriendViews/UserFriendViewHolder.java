package com.example.beastchat.views.UserFriendViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.entities.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserFriendViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_user_friends_friendImageView)
    RoundedImageView mUserPicture;
    @BindView(R.id.list_user_friends_userName)
    TextView mUserName;
    @BindView(R.id.list_user_friends_startChat)
    ImageView mStartChat;

    public UserFriendViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
    public void populate(Context context, User user)
    {
        itemView.setTag(user);
        Picasso.with(context)
        .load(user.getUserPicture())
                .into(mUserPicture);
        mUserName.setText(user.getUsername());
    }

}
