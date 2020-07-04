package com.example.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.beastchat.R;
import com.example.beastchat.activities.FriendsActivity;
import com.example.beastchat.activities.InboxActivity;
import com.example.beastchat.activities.ProfileActivity;
import com.example.beastchat.utils.Constants;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import rx.subscriptions.CompositeSubscription;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;

public class BaseFragment extends Fragment
{

    protected CompositeSubscription mCompositeSubscription;

    protected SharedPreferences mSharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription=new CompositeSubscription();
        mSharedPreferences=getActivity().getSharedPreferences(Constants.USER_INFO_PREFERENCE,
                Context.MODE_PRIVATE);
    }
    public void setUpBottomBar(BottomBar bottomBar,int index)
    {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch ((index))
                {
                    case 1:
                        if(tabId== R.id.tab_profile)
                        {
                            Intent intent=new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if(tabId==R.id.tab_friends)
                        {
                            Intent intent=new Intent(getActivity(), FriendsActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                        }
                        break;
                    case 2:
                        if(tabId== R.id.tab_messages)
                        {
                            Intent intent=new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if(tabId==R.id.tab_profile)
                        {
                            Intent intent=new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                        }
                        break;
                    case 3:
                        if(tabId== R.id.tab_messages)
                        {
                            Intent intent=new Intent(getActivity(), InboxActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                        else if(tabId==R.id.tab_friends)
                        {
                            Intent intent=new Intent(getActivity(), FriendsActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                        }
                        break;

                }



            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.clear();//reuse subscription ...if unsubscribe then cannot use that subscription
    }
}
