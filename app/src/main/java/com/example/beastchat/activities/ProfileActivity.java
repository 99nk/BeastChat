package com.example.beastchat.activities;

import androidx.fragment.app.Fragment;

import com.example.beastchat.fragments.ProfileFragment;

public class ProfileActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return ProfileFragment.newInstance();
    }
}
