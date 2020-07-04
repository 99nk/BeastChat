package com.example.beastchat.activities;

import androidx.fragment.app.Fragment;

import com.example.beastchat.fragments.FriendsFragment;

public class FriendsActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return FriendsFragment.newInstance();
    }
}
