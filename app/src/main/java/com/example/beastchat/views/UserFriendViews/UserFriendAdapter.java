package com.example.beastchat.views.UserFriendViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserFriendAdapter extends RecyclerView.Adapter {
    private BaseFragmentActivity mActivity;
    List<User> mUser;
    LayoutInflater mInflator;
    private UserClickedListener mListener;

    public UserFriendAdapter(BaseFragmentActivity mActivity, UserClickedListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mInflator=mActivity.getLayoutInflater();
        mUser=new ArrayList<>();
    }

    public void setmUser(List<User> user) {
        mUser.clear();
        mUser.addAll(user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflator.inflate(R.layout.list_users_friends,parent,false);
        UserFriendViewHolder userFriendViewHolder=new UserFriendViewHolder(view);
        userFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=(User)userFriendViewHolder.itemView.getTag();
                mListener.OnUserClicked(user);
            }
        });
        return userFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UserFriendViewHolder)holder).populate(mActivity,mUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }
    public  interface UserClickedListener
    {
        void OnUserClicked(User user);


    }
}
