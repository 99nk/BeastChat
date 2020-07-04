package com.example.beastchat.views.FriendRequestsViews;

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

public class FriendRequestsAdapter extends RecyclerView.Adapter
{
    private BaseFragmentActivity mActivity;
    private LayoutInflater mInflater;
    private List<User> mUsers;
    private onOptionListener mListener;

    public FriendRequestsAdapter(BaseFragmentActivity mActivity, onOptionListener mListener) {
        this.mActivity = mActivity;
        this.mListener = mListener;
        mInflater=mActivity.getLayoutInflater();
        mUsers=new ArrayList<>();
    }

    public void setmUsers(List<User> users)
    {
        mUsers.clear();
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.list_friend_request,parent,false);
        FriendRequestViewHolder friendRequestViewHolder=new FriendRequestViewHolder(view);
        friendRequestViewHolder.approveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=(User)friendRequestViewHolder.itemView.getTag();
                mListener.onOptionClicked(user,"0");
            }
        });
        friendRequestViewHolder.rejectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=(User)friendRequestViewHolder.itemView.getTag();
                mListener.onOptionClicked(user,"1");
            }
        });
        return friendRequestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendRequestViewHolder)holder).populate(mActivity,mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public interface onOptionListener{
        void onOptionClicked(User user,String result);
    }
}
