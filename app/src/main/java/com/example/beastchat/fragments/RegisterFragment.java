package com.example.beastchat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.beastchat.R;
import com.example.beastchat.activities.BaseFragmentActivity;
import com.example.beastchat.services.LiveAccountServices;
import com.example.beastchat.utils.Constants;

import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RegisterFragment extends  BaseFragment
{

    @BindView(R.id.fragment_register_userName)
    EditText mUserNameEt;

    @BindView(R.id.fragment_register_userEmail)
    EditText mUserEmailEt;

    @BindView(R.id.fragment_register_userPassword)
    EditText mUserPasswordEt;

    @BindView(R.id.fragment_register_registerButtton)
    Button mRegisterButton;

    @BindView(R.id.fragment_register_loginButtton)
    Button mLoginButton;

    private Unbinder mUnbinder;
    private Socket mSocket;
    private BaseFragmentActivity mActivity;
    private LiveAccountServices mLiveAccountServices;


    public static RegisterFragment newInstance()
    {
        return new RegisterFragment();
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
        mSocket.on("message",accountResponse());
           mLiveAccountServices=LiveAccountServices.getInstance();
    }
    @OnClick(R.id.fragment_register_registerButtton)
    public void setmRegistrationButton()
    {
        mCompositeSubscription.add(mLiveAccountServices.sendRegistrationInfo(
                mUserNameEt,mUserEmailEt,mUserPasswordEt,mSocket
        ));
    }

    @OnClick(R.id.fragment_register_loginButtton)
    public void setmLoginButton()
    {
        getActivity().finish();
    }
    private Emitter.Listener accountResponse()
    {
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data=(JSONObject)args[0];
                mCompositeSubscription.add(mLiveAccountServices.RegisterResponse(data,mActivity));
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register,container,false);
        mUnbinder= ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity=(BaseFragmentActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity=null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
