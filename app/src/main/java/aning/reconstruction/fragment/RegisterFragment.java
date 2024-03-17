/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package aning.reconstruction.fragment;

import static zuo.biao.library.util.JSON.parseObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import aning.reconstruction.DEMO.DemoAdapter;
import aning.reconstruction.R;
import aning.reconstruction.activity.MainTabActivity;
import aning.reconstruction.activity.UserActivity;
import aning.reconstruction.interfaces.OnDataPassListener;
import aning.reconstruction.model.Response;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.Entry;


/**
 * 注册页面Fragment
 */
public class RegisterFragment extends BaseFragment {
    private static final String TAG = "RegisterFragment";

    OnDataPassListener.OnFragmentMessageListener mCallback;
    //与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
    public static final String ARGUMENT_USER_NAME = "ARGUMENT_USER_NAME";

    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @return
     */
    public static RegisterFragment createInstance(long userId) {
        return createInstance(userId, null);
    }

    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @param userName
     * @return
     */
    public static RegisterFragment createInstance(long userId, String userName) {
        RegisterFragment fragment = new RegisterFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(ARGUMENT_USER_ID, userId);
        bundle.putString(ARGUMENT_USER_NAME, userName);

        fragment.setArguments(bundle);
        return fragment;
    }

    //与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private long userId = 0;
    private String userName = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //TODO demo_fragment改为你所需要的layout文件
        setContentView(R.layout.register_fragment);

        argument = getArguments();
        if (argument != null) {
            userId = argument.getLong(ARGUMENT_USER_ID, userId);
            userName = argument.getString(ARGUMENT_USER_NAME, userName);
        }

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        return view;//返回值必须为view
    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private Button btRegister;
    private EditText account;
    private EditText password;
    private EditText passwordAgain;

    @Override
    public void initView() {//必须在onCreateView方法内调用
        btRegister = findView(R.id.register);
        account = findView(R.id.account);
        password = findView(R.id.password);
        passwordAgain = findView(R.id.passwordAgain);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnDataPassListener.OnFragmentMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentMessageListener");
        }
    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initData() {//必须在onCreateView方法内调用


    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private final int requestCodeRegister = 2;

    @Override
    public void initEvent() {//必须在onCreateView方法内调用
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = RegisterFragment.this.account.getText().toString().trim();
                String password = RegisterFragment.this.password.getText().toString().trim();
                String passwordAgain = RegisterFragment.this.passwordAgain.getText().toString().trim();
                if (account.length() <= 0 || password.length() <= 0 || passwordAgain.length() <= 0) {
                    showShortToast(R.string.not_empty);
                    return;
                } else {
                    if (password.equals(passwordAgain)) {
                        showProgressDialog(R.string.on_loadding);
                        HttpRequest.register(account, password, requestCodeRegister, new OnHttpResponseListener() {
                            @Override
                            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                                dismissProgressDialog();
                                if (e != null) {
                                    showShortToast(R.string.register_faild);
                                } else {
                                    if (requestCode == requestCodeRegister) {
                                        try {
                                            Response response = parseObject(resultJson, Response.class);
                                            if (response == null) {
                                                throw new Exception("Response is null");
                                            }
                                            if (response.getCode() == 0) {
                                                if (mCallback != null) {
                                                    mCallback.onFragmentMessageListener(0);
                                                }
                                            } else {
                                                showShortToast(response.getMsg());
                                            }
                                        } catch (Exception error) {
                                            showShortToast(R.string.sys_error);
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        showShortToast(R.string.password_not_same);
                    }
                }


            }
        });
    }


    //生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}