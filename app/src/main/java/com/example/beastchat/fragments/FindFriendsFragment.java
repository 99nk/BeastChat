package com.example.beastchat.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.beastchat.views.FindFriendViews.FindFriendsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class FindFriendsFragment extends BaseFragment implements FindFriendsAdapter.UserListener
{
    @BindView(R.id.fragment_find_friends_SearchBar)
    EditText mSearchBar;

    @BindView(R.id.fragment_find_friends_RecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.fragment_find_friends_noResults)
    TextView mTextView;

    private Unbinder mUnbinder;

    private ValueEventListener mGetAllUsersListeners;
    private DatabaseReference mGetAllUsersReference, mGetAllCurrentUsersFriendsRef,mGetAllFriendRequestsSentReference,mGetAllFriendRequestsReceivedReference;
    private String mUserEmailString;
    private List<User> mAllUsers;
    private FindFriendsAdapter mAdapter;
    private ValueEventListener mGetAllFriendRequestsSentListener,mGetAllCurrentUsersFriendsListener,mGetAllFriendRequestsReceivedListener;
    private LiveFriendsServices mLiveFriendsServices;

    public HashMap<String,User> mFriendRequestsSentMap;

    private Socket mSocket;
    private PublishSubject<String> mSearchBarString;


    public static FindFriendsFragment newInstance()
    {
        return new FindFriendsFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            mSocket = IO.socket(Constants.IP_LOCAL_HOST);
        }
        catch (URISyntaxException e)
        {
            Log.i(RegisterFragment.class.getSimpleName(),e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }
        mSocket.connect();

        mUserEmailString=mSharedPreferences.getString(Constants.USER_EMAIL,"");
        mLiveFriendsServices=LiveFriendsServices.getInstance();
        mFriendRequestsSentMap=new HashMap<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_find_friends,container,false);
        mUnbinder= ButterKnife.bind(this,rootView);
        mAllUsers=new ArrayList<>();

        mAdapter=new FindFriendsAdapter((BaseFragmentActivity)getActivity(),this);
        mGetAllUsersListeners=getGetAllUsers(mAdapter,mUserEmailString);
        mGetAllUsersReference= FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PATH_USERS);
        mGetAllUsersReference.addValueEventListener(mGetAllUsersListeners);

        mGetAllFriendRequestsSentReference =FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PATH_FRIEND_REQUEST_SENT)
                .child(Constants.encodeEmail(mUserEmailString));

        mGetAllFriendRequestsSentListener=mLiveFriendsServices.getFriendRequestSent(mAdapter,this);
        mGetAllFriendRequestsReceivedListener=mLiveFriendsServices.getFriendRequestReceived(mAdapter);

        mGetAllFriendRequestsReceivedReference=FirebaseDatabase.getInstance().getReference()
                .child(Constants.FIREBASE_PATH_FRIEND_REQUEST_RECEIVED)
                .child(Constants.encodeEmail(mUserEmailString));
        mGetAllFriendRequestsSentReference.addValueEventListener(mGetAllFriendRequestsSentListener);
        mGetAllFriendRequestsReceivedReference.addValueEventListener(mGetAllFriendRequestsReceivedListener);


        mGetAllCurrentUsersFriendsListener=mLiveFriendsServices.getAllCurrentUsersFriendMap(mAdapter);
        mGetAllCurrentUsersFriendsRef=FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_PATH_USER_FRIENDS)
                .child(Constants.encodeEmail(mUserEmailString));
        mGetAllCurrentUsersFriendsRef.addValueEventListener(mGetAllCurrentUsersFriendsListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mCompositeSubscription.add(createSearchBarSubscription());
        listenToSearchBar();

        return rootView;
    }


    private Subscription createSearchBarSubscription()
    {
        mSearchBarString=PublishSubject.create();
        return mSearchBarString
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<User>>() {
                    @Override
                    public List<User> call(String s) {
                        return mLiveFriendsServices.getMatchongUsers(mAllUsers,s);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<User> users)
                    {

                        if(users.isEmpty())
                        {
                            mRecyclerView.setVisibility(View.GONE);
                            mTextView.setVisibility(View.VISIBLE);
                        }
                        else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mTextView.setVisibility(View.GONE);
                            //mRecyclerView
                        }
                        mAdapter.setmUsers(users);

                    }
                });
    }
    private void listenToSearchBar()
    {
        mSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mSearchBarString.onNext(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void setmFriendRequestsSentMap(HashMap<String, User> friendRequestsSentMap) {
        mFriendRequestsSentMap.clear();
        mFriendRequestsSentMap.putAll(friendRequestsSentMap);
    }

    public ValueEventListener getGetAllUsers(FindFriendsAdapter adapter, String currentUsersEmail)
    {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAllUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    if(!user.getEmail().equals(mUserEmailString))
                    {
                        mAllUsers.add(user);
                    }
                }
                adapter.setmUsers(mAllUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Cant load Users", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        if(mGetAllUsersListeners!=null)
        {
            mGetAllUsersReference.removeEventListener(mGetAllUsersListeners);
        }
        if(mGetAllFriendRequestsSentListener!=null)
        {
            mGetAllFriendRequestsSentReference.removeEventListener(mGetAllFriendRequestsSentListener);
        }
        if(mGetAllFriendRequestsReceivedListener!=null)
        {
            mGetAllFriendRequestsReceivedReference.removeEventListener(mGetAllFriendRequestsReceivedListener);
        }
        if(mGetAllCurrentUsersFriendsListener!=null)
        {
            mGetAllCurrentUsersFriendsRef.removeEventListener(mGetAllCurrentUsersFriendsListener);
        }
    }

    @Override
    public void OnUserClicked(User user)
    {
        if(Constants.isIncludedInMap(mFriendRequestsSentMap,user))//cancelling friendRequest
        {
            mGetAllFriendRequestsSentReference.child(Constants.encodeEmail(user.getEmail())).removeValue();
            mCompositeSubscription.add(mLiveFriendsServices.addOrRemoveFriendRequest(mSocket,mUserEmailString,user.getEmail(),"1"));
        }
        else
        {
            mGetAllFriendRequestsSentReference.child(Constants.encodeEmail(user.getEmail())).setValue(user);
            mCompositeSubscription.add(mLiveFriendsServices.addOrRemoveFriendRequest(mSocket,mUserEmailString,user.getEmail(),"0"));
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
