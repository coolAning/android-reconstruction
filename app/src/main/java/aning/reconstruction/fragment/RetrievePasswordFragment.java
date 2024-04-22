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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import aning.reconstruction.R;
import aning.reconstruction.interfaces.OnDataPassListener;
import aning.reconstruction.model.Response;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;


/**
 * 找回密码页面Fragment
 */
public class RetrievePasswordFragment extends BaseFragment {
    private static final String TAG = "RetrievePasswordFragment";
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
    public static RetrievePasswordFragment createInstance(long userId) {
        return createInstance(userId, null);
    }

    /**
     * 创建一个Fragment实例
     *
     * @param userId
     * @param userName
     * @return
     */
    public static RetrievePasswordFragment createInstance(long userId, String userName) {
        RetrievePasswordFragment fragment = new RetrievePasswordFragment();

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
        setContentView(R.layout.forget_password_fragment);

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

    private EditText accountET;
    private EditText passwordET;
    private EditText passwordAgainET;
    private EditText codeET;
    private TextView sendCodeTV;
    private Button retrieveBT;

    @Override
    public void initView() {//必须在onCreateView方法内调用
        accountET = findView(R.id.account_et);
        passwordET = findView(R.id.new_password_et);
        passwordAgainET = findView(R.id.re_password_et);
        codeET = findView(R.id.code_et);
        sendCodeTV = findView(R.id.send_code_tv);
        retrieveBT = findView(R.id.retrieve_bt);

    }
    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnDataPassListener.OnFragmentMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnFragmentMessageListener");
        }
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    @Override
    public void initData() {//必须在onCreateView方法内调用


    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private final int requestCodeCaptcha = 1;
    private final int requestCodeRetrieve = 2;

    @Override
    public void initEvent() {//必须在onCreateView方法内调用
        sendCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountET.getText().toString().trim().isEmpty()) {
                    showShortToast("请输入邮箱账号");
                } else {
                    // 发送验证码
                    sendCodeTV.setClickable(false);
                    sendCodeTV.setTextColor(getResources().getColor(R.color.gray));
                    // 发送逻辑
                    HttpRequest.sendCode(accountET.getText().toString().trim(), requestCodeCaptcha, new OnHttpResponseListener() {
                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                            if (e != null) {
                                showShortToast(e.getMessage());
                            } else {
                                if(requestCode == requestCodeCaptcha){
                                    try {
                                        Response response = parseObject(resultJson, Response.class);
                                        if (response == null) {
                                            throw new Exception("Response is null");
                                        }
                                        if (response.getCode() == 0) {
                                            showShortToast("验证码已发送");
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

                    // 创建一个60秒的倒计时，每秒更新一次
                    new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // 在这里更新剩余时间
                            sendCodeTV.setText("剩余时间: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            // 倒计时结束，再次启用sendCodeTV
                            sendCodeTV.setClickable(true);
                            sendCodeTV.setTextColor(getResources().getColor(R.color.blue));
                            sendCodeTV.setText("发送验证码");
                        }
                    }.start();
                }
            }

        });

        retrieveBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //找回密码
                String account = accountET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String passwordAgain = passwordAgainET.getText().toString().trim();
                String code = codeET.getText().toString().trim();
                if (account.isEmpty() || password.isEmpty() || passwordAgain.isEmpty() || code.isEmpty()) {
                    showShortToast(R.string.not_empty);
                } else if (!password.equals(passwordAgain)) {
                    showShortToast("两次输入的密码不一致");
                } else {
                    showProgressDialog(R.string.on_loadding);
                    // 找回密码逻辑
                    HttpRequest.forgetPassword(account, password, code, requestCodeRetrieve, new OnHttpResponseListener() {
                        @Override
                        public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                            dismissProgressDialog();
                            if (e != null) {
                                showShortToast(R.string.net_error);
                            } else {
                                if (requestCode == requestCodeRetrieve) {
                                    try {
                                        Response response = parseObject(resultJson, Response.class);
                                        if (response == null) {
                                            throw new Exception("Response is null");
                                        }
                                        if (response.getCode() == 0) {
                                            showShortToast("密码修改成功");
                                            // 找回密码成功后，返回登录页面
                                            if (mCallback != null) {
                                                mCallback.onFragmentMessageListener(0);
                                            }
                                        } else {
                                            showShortToast(response.getMsg());
                                        }
                                    } catch (Exception error) {
                                        showShortToast(error.getMessage());
                                    }

                                }
                            }
                        }
                    });


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