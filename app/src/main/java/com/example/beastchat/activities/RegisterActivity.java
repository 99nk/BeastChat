package com.example.beastchat.activities;

import androidx.fragment.app.Fragment;

import com.example.beastchat.fragments.RegisterFragment;

public class RegisterActivity extends BaseFragmentActivity
{
    @Override
    Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}
