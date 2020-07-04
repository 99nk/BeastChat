package com.example.beastchat.views.FindFriendViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.entities.User;
import com.example.beastchat.utils.Constants;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindFriendsViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.list_user_userPicture)
    RoundedImageView mUserPicture;

    @BindView(R.id.list_user_addFriend)
    public ImageView mAddFriend;

    @BindView(R.id.list_user_userName)
    TextView mUserName;
    @BindView(R.id.list_user_userStatus)
    TextView mUserStatus;



    public FindFriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
    public void populate(Context context, User user,
                         HashMap<String,User> friendRequestSentMap,
                         HashMap<String,User> friendRequestReceivedMap,
                         HashMap<String,User> currentUsersFriendMap)
    {
        itemView.setTag(user);
        mUserName.setText(user.getUsername());
        Picasso.with(context)
                .load(user.getUserPicture())
                .into(mUserPicture);

        if(Constants.isIncludedInMap(friendRequestSentMap,user))
        {
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("Friend Request Sent!");
            mAddFriend.setImageResource(R.drawable.ic_baseline_highlight_off_24);
            mAddFriend.setVisibility(View.VISIBLE);
        }
        else if(Constants.isIncludedInMap(friendRequestReceivedMap,user))
        {
            mUserStatus.setVisibility(View.VISIBLE);
            mAddFriend.setVisibility(View.GONE);
            mUserStatus.setText("This user has requested you..");
        }
        else if(Constants.isIncludedInMap(currentUsersFriendMap,user))
        {
            mUserStatus.setVisibility(View.VISIBLE);
            mUserStatus.setText("You are already friends");
            mAddFriend.setVisibility(View.GONE);
        }
        else
        {
            mAddFriend.setVisibility(View.VISIBLE);
            mAddFriend.setImageResource(R.drawable.ic_baseline_person_add_24);
            mUserStatus.setVisibility(View.GONE);
        }



    }
}
