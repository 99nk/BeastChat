package com.example.beastchat.activities;

import androidx.fragment.app.Fragment;

import com.example.beastchat.fragments.LoginFragment;

public class LoginActivity extends BaseFragmentActivity
{

    @Override
    Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}