package com.example.beastchat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.entities.User;
import com.example.beastchat.services.LiveFriendsServices;
import com.example.beastchat.utils.Constants;
import com.example.beastchat.views.FindFriendViews.FindFriendsAdapter;
import com.example.beastchat.views.UserFriendViews.UserFriendAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UserFriendsFragment extends BaseFragment implements UserFriendAdapter.UserClickedListener
{
    @BindView(R.id.fragment_user_friend_RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_user_friend_message)
    TextView mTextView;
    private LiveFriendsServices mLiveFriendsServices;
    private String mUserEmailString;
    private ValueEventListener mGetAllCurrentUsersFriendsListener;
    private DatabaseReference mGetAllCurrentUsersFriendsRef;
    private Unbinder mUnbinder;



    public static  UserFriendsFragment newInsatnce()
    {
        return new UserFriendsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLiveFriendsServices=LiveFriendsServices.getInstance();
        mUserEmailString=mSharedPreferences.getString(Constants.USER_EMAIL,"");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_user_friends,container,false);
        mUnbinder= ButterKnife.bind(this,rootView);
        UserFriendAdapter adapter=new UserFriendAdapter((BaseFragmentActivity)getActivity(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGetAllCurrentUsersFriendsRef= FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_PATH_USER_FRIENDS).child(Constants.encodeEmail(mUserEmailString));
        mGetAllCurrentUsersFriendsListener=mLiveFriendsServices.getAllFriends(mRecyclerView,adapter,mTextView);
        mGetAllCurrentUsersFriendsRef.addValueEventListener(mGetAllCurrentUsersFriendsListener);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(mGetAllCurrentUsersFriendsListener!=null)
        {
            mGetAllCurrentUsersFriendsRef.removeEventListener(mGetAllCurrentUsersFriendsListener);
        }
    }

    @Override
    public void OnUserClicked(User user) {

    }
}
