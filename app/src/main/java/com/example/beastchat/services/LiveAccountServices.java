package com.example.beastchat.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.activities.InboxActivity;
import com.example.beastchat.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


import io.socket.client.Socket;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LiveAccountServices
{
    private static LiveAccountServices mLiveAccountServices;
    private final int USER_ERROR_EMPTY_PASSWORD=1;
    private final int USER_ERROR_EMPTY_EMAIL=2;
    private final int USER_ERROR_EMPTY_USERNAME=3;
    private final int USER_ERROR_PASSWORD_SHORT=4;
    private final int USER_ERROR_EMAIL_BAD_FORMAT=5;

    private final int SERVER_SUCCESS=6;
    private final int SERVER_FAILURE=7;
    private final int USER_NO_ERRORS=8;

    public static LiveAccountServices getInstance()
    {
        if(mLiveAccountServices==null)
        {
            mLiveAccountServices=new LiveAccountServices();
        }
        return mLiveAccountServices;
    }

    public Subscription getAuthToken(JSONObject data, BaseFragmentActivity activity, SharedPreferences sharedPreferences)
    {
        Observable<JSONObject> jsonObjectObservable=Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<JSONObject, List<String>>() {
                    @Override
                    public List<String> call(JSONObject jsonObject) {
                        List<String> userDetails=new ArrayList<>();
                        try {
                            JSONObject serverData=jsonObject.getJSONObject("token");
                            String token=(String)serverData.get("authToken");
                            String email=(String)serverData.get("email");
                            String photo=(String)serverData.get("photo");
                            String userName=(String)serverData.get("displayName");

                            userDetails.add(token);
                            userDetails.add(email);
                            userDetails.add(photo);
                            userDetails.add(userName);
                            return userDetails;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return userDetails;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings)
                    {
                        String token=strings.get(0);
                        String email=strings.get(1);
                        String photo=strings.get(2);
                        String userName=strings.get(3);

                        if(!email.equals("error"))
                        {
                            FirebaseAuth.getInstance().signInWithCustomToken(token)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(!task.isSuccessful())
                                            {
                                                Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                sharedPreferences.edit().putString(Constants.USER_EMAIL,email).apply();
                                                sharedPreferences.edit().putString(Constants.USER_NAME,userName).apply();
                                                sharedPreferences.edit().putString(Constants.USER_PICTURE,photo).apply();

                                                Intent intent=new Intent(activity, InboxActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.startActivity(intent);
                                                activity.finish();
                                            }
                                        }
                                    });
                        }

                    }
                });
    }

    //sending Login information to server

    public Subscription sendLoginInfo(EditText userEmailEt,EditText userPasswordEt,Socket socket,BaseFragmentActivity activity)
    {
        List<String> userDetails=new ArrayList<>();
        //userDetails.add(userNameEt.getText().toString());
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());
        //userPasswordEt.addTextChangedListener();

        Observable<List<String>> userDetailsObservable= Observable.just(userDetails);
        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings)
                    {
                        String userEmail=strings.get(0);
                        String userPassword=strings.get(1);
                        if(userEmail.isEmpty())
                        {
                            return USER_ERROR_EMPTY_EMAIL;
                        }
                        else if (userPassword.isEmpty()) {
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if (userPassword.length() < 6) {
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)) {
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        }else
                        {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                       if(!task.isSuccessful())
                                       {
                                           Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                       }
                                       else
                                       {
                                           JSONObject sendData=new JSONObject();
                                           try {
                                               sendData.put("email",userEmail);
                                               socket.emit("userInfo",sendData);
                                           } catch (JSONException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                        }
                                    });
                            FirebaseAuth.getInstance().signOut();
                            //to make sure that Auth custom token is not used...the one from server generated is used
                            return USER_NO_ERRORS;
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
                        if(integer.equals(USER_ERROR_EMPTY_EMAIL))
                            userEmailEt.setError("Email Address is empty...");
                        else if(integer.equals(USER_ERROR_EMAIL_BAD_FORMAT))
                            userEmailEt.setError("Email Address is in wrong format...");
                        else if(integer.equals(USER_ERROR_EMPTY_PASSWORD))
                            userPasswordEt.setError("password...");
                        else if(integer.equals(USER_ERROR_PASSWORD_SHORT))
                            userPasswordEt.setError("Short Password");


                    }
                });
    }


    //RxJava
    public Subscription sendRegistrationInfo(EditText userNameEt, EditText userEmailEt, EditText userPasswordEt
                                                , Socket socket)
    {
        List<String> userDetails=new ArrayList<>();
        userDetails.add(userNameEt.getText().toString());
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());
        //userPasswordEt.addTextChangedListener();

        Observable<List<String>> userDetailsObservable= Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {

                        String userName = strings.get(0);
                        String userEmail = strings.get(1);
                        String userPassword = strings.get(2);

                        if (userName.isEmpty())
                            return USER_ERROR_EMPTY_USERNAME;
                        else if (userEmail.isEmpty())
                            return USER_ERROR_EMPTY_EMAIL;
                        else if (userPassword.isEmpty()) {
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if (userPassword.length() < 6) {
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)) {
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        } else {
                            JSONObject sendData = new JSONObject();
                            try {
                                sendData.put("email", userEmail);
                                sendData.put("username", userName);
                                sendData.put("password", userPassword);
                                socket.emit("UserData", sendData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e)
                            {
                                Log.i(LiveAccountServices.class.getSimpleName(), e.getMessage());
                                return SERVER_FAILURE;
                                //e.printStackTrace();
                            }
                        }


                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
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
                        if(integer.equals(USER_ERROR_EMPTY_EMAIL))
                        userEmailEt.setError("Email Address is empty...");
                        else if(integer.equals(USER_ERROR_EMAIL_BAD_FORMAT))
                            userEmailEt.setError("Email Address is in wrong format...");
                        else if(integer.equals(USER_ERROR_EMPTY_PASSWORD))
                            userPasswordEt.setError("password...");
                        else if(integer.equals(USER_ERROR_PASSWORD_SHORT))
                            userPasswordEt.setError("Short Password");
                        else if(integer.equals(USER_ERROR_EMPTY_USERNAME))
                            userNameEt.setError("empty name");
                      //  else if(integer.equals(SERVER_FAILURE))


                    }
                });
       // return subscribe;
    }

    public Subscription RegisterResponse(JSONObject data, BaseFragmentActivity activity)
    {
        Observable<JSONObject> jsonObjectObservable=Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<JSONObject, String>() {

                    @Override
                    public String call(JSONObject jsonObject) {
                        String message;
                        try {
                            JSONObject json=jsonObject.getJSONObject("message");
                            message=(String)json.getString("text");
                            return message;
                        } catch (JSONException e) {
                            return e.getMessage();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String stringResponse)
                    {
                        if(stringResponse.equals("Success"))
                        {
                            activity.finish();
                            Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(activity, stringResponse, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private boolean isEmailValid(CharSequence email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
