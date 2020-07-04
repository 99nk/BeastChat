package com.example.beastchat.activities;

import androidx.fragment.app.Fragment;

import com.example.beastchat.fragments.InboxFragment;

public class InboxActivity extends BaseFragmentActivity
{

    @Override
    Fragment createFragment() {
        return InboxFragment.newInstance();
    }
}
