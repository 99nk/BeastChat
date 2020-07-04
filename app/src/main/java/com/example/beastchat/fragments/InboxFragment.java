package com.example.beastchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beastchat.R;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class InboxFragment extends BaseFragment
{
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private Unbinder mUbinder;


    public static InboxFragment newInstance()
    {
        return new InboxFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_inbox,container,false);
        mUbinder= ButterKnife.bind(this,rootView);
        mBottomBar.selectTabWithId(R.id.tab_messages);
        setUpBottomBar(mBottomBar,1);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUbinder.unbind();
    }
}
