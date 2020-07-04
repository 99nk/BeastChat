package com.example.beastchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;

public class UserFriendsFragment extends BaseFragment
{
    public static  UserFriendsFragment newInsatnce()
    {
        return new UserFriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_user_friends,container,false);
        return rootView;
    }
}
