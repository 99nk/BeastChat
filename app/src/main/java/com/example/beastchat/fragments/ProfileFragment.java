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

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private Unbinder mUbinder;

    public static ProfileFragment newInstance()
    {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_profile,container,false);
        mUbinder= ButterKnife.bind(this,rootView);
        mBottomBar.selectTabWithId(R.id.tab_profile);
        setUpBottomBar(mBottomBar,3);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUbinder.unbind();
    }
}
