package com.example.beastchat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.entities.User;
import com.example.beastchat.services.LiveFriendsServices;
import com.example.beastchat.utils.Constants;
import com.example.beastchat.views.FriendRequestsViews.FriendRequestsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;

public class FriendRequestsFragment extends  BaseFragment implements FriendRequestsAdapter.onOptionListener {

    @BindView(R.id.fragment_friend_request_RecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_friend_request_message)
    TextView mTextView;
    private LiveFriendsServices mLiveFriendsServices;
    private DatabaseReference mGetAllUserFriendRequestsReference;
    private ValueEventListener mGetAllUserFriendRequestsListener;

    private Unbinder mUnbinder;
    private String mUserEmailString;
    private Socket mSocket;


    public static FriendRequestsFragment newInstance()
    {
        return new FriendRequestsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket= IO.socket(Constants.IP_LOCAL_HOST);
        }
        catch (URISyntaxException e)
        {
            Log.i(LoginFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();
        mLiveFriendsServices=LiveFriendsServices.getInstance();
        mUserEmailString=mSharedPreferences.getString(Constants.USER_EMAIL,"");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_friend_request,container,false);
        mUnbinder= ButterKnife.bind(this,rootView);
        FriendRequestsAdapter adapter=new FriendRequestsAdapter((BaseFragmentActivity)getActivity(),this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGetAllUserFriendRequestsReference= FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PATH_FRIEND_REQUEST_RECEIVED).child(Constants.encodeEmail(mUserEmailString));
        mGetAllUserFriendRequestsListener=mLiveFriendsServices.getAllFriendRequests(adapter,mRecyclerView,mTextView);
        mGetAllUserFriendRequestsReference.addValueEventListener(mGetAllUserFriendRequestsListener);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(mGetAllUserFriendRequestsListener!=null)
        {
            mGetAllUserFriendRequestsReference.removeEventListener(mGetAllUserFriendRequestsListener);
        }
    }

    @Override
    public void onOptionClicked(User user, String result)
    {
        if(result.equals("0"))
        {
            DatabaseReference userFriendRef=FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PATH_USER_FRIENDS)
                    .child(Constants.encodeEmail(mUserEmailString))
                    .child(Constants.encodeEmail(user.getEmail()));
            userFriendRef.setValue(user.getEmail());
            mGetAllUserFriendRequestsReference.child(Constants.encodeEmail(user.getEmail()))
                    .removeValue();
            //server...
            mCompositeSubscription.add(mLiveFriendsServices.approveDeclineFriendREquest(mSocket,mUserEmailString,user.getEmail(),"0"));
        }
        else {mGetAllUserFriendRequestsReference.child(Constants.encodeEmail(user.getEmail()))
                .removeValue();
            //server...
            mCompositeSubscription.add(mLiveFriendsServices.approveDeclineFriendREquest(mSocket,mUserEmailString,user.getEmail(),"1"));


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
