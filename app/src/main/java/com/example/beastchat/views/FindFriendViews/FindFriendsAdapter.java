package com.example.beastchat.views.FindFriendViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindFriendsAdapter extends RecyclerView.Adapter {
    private BaseFragmentActivity mActivity;
    private List<User> mUsers;
    private LayoutInflater mLayoutInflater;
    private UserListener mListener;
    private HashMap<String,User> mFriendRequestSentMap;
    private HashMap<String,User> mFriendRequestReceivedMap;
    private HashMap<String,User> mCurrentUserFriendsMap;

    public FindFriendsAdapter(BaseFragmentActivity mActivity,UserListener mListener) {
        this.mListener = mListener;
        this.mActivity=mActivity;
        mLayoutInflater=mActivity.getLayoutInflater();
        mUsers=new ArrayList<>();
        mFriendRequestSentMap=new HashMap<>();
        mFriendRequestReceivedMap=new HashMap<>();
        mCurrentUserFriendsMap=new HashMap<>();
    }

    public void setmUsers(List<User> users)
    {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    public void setmFriendRequestSentMap(HashMap<String, User> friendRequestSentMap) {
        mFriendRequestSentMap.clear();
        mFriendRequestSentMap.putAll(friendRequestSentMap);
        notifyDataSetChanged();
    }

    public void setmCurrentUserFriendsMap(HashMap<String, User> currentUserFriendsMap) {
        mCurrentUserFriendsMap.clear();
        mCurrentUserFriendsMap.putAll(currentUserFriendsMap);
        notifyDataSetChanged();
    }

    public void setmFriendRequestReceivedMap(HashMap<String, User> friendRequestReceivedMap) {
        mFriendRequestReceivedMap.clear();
        mFriendRequestReceivedMap.putAll(friendRequestReceivedMap);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView=mLayoutInflater.inflate(R.layout.list_user,parent,false);
        FindFriendsViewHolder findFriendsViewHolder=new FindFriendsViewHolder(userView);

        findFriendsViewHolder.mAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=(User) findFriendsViewHolder.itemView.getTag();
                mListener.OnUserClicked(user);

            }
        });
        return findFriendsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ((FindFriendsViewHolder)holder).populate(mActivity,mUsers.get(position),mFriendRequestSentMap,mFriendRequestReceivedMap,mCurrentUserFriendsMap);

    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public interface UserListener{
        void OnUserClicked(User user);
    }
}
