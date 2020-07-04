package com.example.beastchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.beastchat.R;
import com.example.beastchat.views.FriendsViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.roughike.bottombar.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendsFragment  extends BaseFragment{
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    @BindView(R.id.fragment_friends_tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.fragment_friends_viewPager)
    ViewPager mViewPager;

    private Unbinder mUbinder;

    public static FriendsFragment newInstance()
    {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_friends,container,false);
        mUbinder= ButterKnife.bind(this,rootView);
        mBottomBar.selectTabWithId(R.id.tab_friends);
        setUpBottomBar(mBottomBar,2);

        FriendsViewPagerAdapter friendsViewPagerAdapter=new FriendsViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(friendsViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUbinder.unbind();
    }
}

