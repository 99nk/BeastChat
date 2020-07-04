package com.example.beastchat.services;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beastchat.entities.User;
import com.example.beastchat.fragments.FindFriendsFragment;
import com.example.beastchat.views.FindFriendViews.FindFriendsAdapter;
import com.example.beastchat.views.FriendRequestsViews.FriendRequestsAdapter;
import com.example.beastchat.views.FriendsViewPagerAdapter;
import com.example.beastchat.views.UserFriendViews.UserFriendAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LiveFriendsServices
{
    public static LiveFriendsServices mLiveFriendsServices;
    private final int SERVER_SUCCESS=6;
    private final int SERVER_FAILURE=7;

    public static LiveFriendsServices getInstance()
    {
        if(mLiveFriendsServices==null)
            return  new LiveFriendsServices();
        else
            return mLiveFriendsServices;

    }
    public ValueEventListener getAllFriends(RecyclerView recyclerView,UserFriendAdapter adapter,TextView textView)
    {
        List<User> users=new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    users.add(user);
                }
                if(users.isEmpty())
                {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
                else
                {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmUser(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


    public ValueEventListener getAllFriendRequests(FriendRequestsAdapter adapter, RecyclerView recyclerView, TextView textView)
    {
        List<User> users=new ArrayList<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                users.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    users.add(user);
                }
                if(users.isEmpty())
                {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    adapter.setmUsers(users);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    public Subscription approveDeclineFriendREquest(Socket socket,String userName,String friendEmail,String requestCode)
    {
        List<String> details=new ArrayList<>();
        details.add(userName);
        details.add(friendEmail);
        details.add(requestCode);
        Observable<List<String>> listObservable=Observable.just(details);
        return listObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                       JSONObject sendData=new JSONObject();
                        try {
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("friendEmail",strings.get(1));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("friendRequestResponse",sendData);
                            return SERVER_SUCCESS;


                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                    }

                    @Override
                    public void onNext(Integer integer)
                    {
                    }
                });
    }


    //sending data to server
    public Subscription addOrRemoveFriendRequest(final Socket socket, String userEmail, String friendEmail, String requestCode)
    {
        List<String> details=new ArrayList<>();
        details.add(userEmail);
        details.add(friendEmail);
        details.add(requestCode);

        Observable<List<String>> listObservable=Observable.just(details);
        return listObservable.subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        JSONObject sendData=new JSONObject();
                        try {
                            sendData.put("email",strings.get(1));
                            sendData.put("userEmail",strings.get(0));
                            sendData.put("requestCode",strings.get(2));
                            socket.emit("friendRequest",sendData);

                            return SERVER_SUCCESS;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return SERVER_FAILURE;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer)
                    {

                    }
                });
    }

    public ValueEventListener getAllCurrentUsersFriendMap(FindFriendsAdapter adapter)
    {
        HashMap<String,User> userHashMap=new HashMap<>();
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHashMap.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(),user);

                }
                adapter.setmCurrentUserFriendsMap(userHashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


    public ValueEventListener getFriendRequestSent(FindFriendsAdapter adapter, FindFriendsFragment fragment)
    {
        HashMap<String, User> userHashMap=new HashMap<>();

        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userHashMap.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(),user);
                }
                adapter.setmFriendRequestSentMap(userHashMap);
                fragment.setmFriendRequestsSentMap(userHashMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    public ValueEventListener getFriendRequestReceived(FindFriendsAdapter adapter)
    {
        HashMap<String, User> userHashMap=new HashMap<>();

        return new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                userHashMap.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);
                    userHashMap.put(user.getEmail(),user);
                }
                adapter.setmFriendRequestReceivedMap(userHashMap);
                //fragment.setmFriendRequestsSentMap(userHashMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    public List<User> getMatchongUsers(List<User> users,String userEmail)
    {
        if(userEmail.isEmpty())
        {
            return users;
        }
        List<User> usersFound=new ArrayList<>();
        for(User user:users)
        {
            if(user.getEmail().toLowerCase().startsWith(userEmail.toLowerCase()))
            {
                usersFound.add(user);
            }
        }
        return usersFound;
    }
}
