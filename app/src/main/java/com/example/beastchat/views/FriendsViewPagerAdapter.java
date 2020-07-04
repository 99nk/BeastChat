package com.example.beastchat.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.beastchat.fragments.FindFriendsFragment;
import com.example.beastchat.fragments.FriendRequestsFragment;
import com.example.beastchat.fragments.UserFriendsFragment;

public class FriendsViewPagerAdapter extends FragmentStatePagerAdapter {
    public FriendsViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment returnFragment;
        switch (position)
        {
            case 0:
                returnFragment= UserFriendsFragment.newInsatnce();
                break;
            case 1:
                returnFragment= FriendRequestsFragment.newInstance();
                break;
            case 2:
                returnFragment= FindFriendsFragment.newInstance();
                break;
            default:
                return null;
        }
        return returnFragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;
        switch (position)
        {
            case 0:
                title="Friends";
                break;
            case 1:
                title="Requests";
                break;
            case 2:
                title="Find Friends";
                break;
            default:
                return null;
        }
        return title;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
