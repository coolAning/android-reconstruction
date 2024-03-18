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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Map;

import aning.reconstruction.R;
import aning.reconstruction.activity.MainTabActivity;
import aning.reconstruction.application.Application;
import aning.reconstruction.interfaces.OnDataPassListener;
import aning.reconstruction.model.Response;
import aning.reconstruction.model.User;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.JSON;


/**
 * 登录页面Fragment
 */
public class LoginFragment extends BaseFragment {
	OnDataPassListener.OnFragmentMessageListener mCallback;
	private static final String TAG = "LoginFragment";

	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
	public static final String ARGUMENT_USER_NAME = "ARGUMENT_USER_NAME";

	/**创建一个Fragment实例
	 * @param userId
	 * @return
	 */
	public static LoginFragment createInstance(long userId) {
		return createInstance(userId, null);
	}
	/**创建一个Fragment实例
	 * @param userId
	 * @param userName
	 * @return
	 */
	public static LoginFragment createInstance(long userId, String userName) {
		LoginFragment fragment = new LoginFragment();

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
		setContentView(R.layout.login_fragment);

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

	private Button btLogin;
	private ImageButton ibRegister;
	private ImageButton ibForgetPassword;
	private EditText account;
	private EditText password;
	@Override
	public void initView() {//必须在onCreateView方法内调用

		btLogin = findView(R.id.login);
		ibRegister = findView(R.id.register_btn);
		ibForgetPassword = findView(R.id.forget_password_btn);
		account = findView(R.id.username);
		password = findView(R.id.password);

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
		if (Application.getInstance().isLoggedIn()) {
			Intent intent = MainTabActivity.createIntent(getActivity(), Application.getInstance().getCurrentUserId());
			toActivity(intent);
			getActivity().finish(); // 关闭当前的Activity
		}

	}




	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private RegisterFragment registerFragment;
	private RetrievePasswordFragment retrievePasswordFragment;
	private final int requestCodeLogin = 1;
	@Override
	public void initEvent() {//必须在onCreate方法内调用
		btLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String account = LoginFragment.this.account.getText().toString().trim();
				String password = LoginFragment.this.password.getText().toString().trim();
				if(account.isEmpty()||password.isEmpty()){
					showShortToast(R.string.not_empty);
				}else{
					showProgressDialog(R.string.on_loadding);
					HttpRequest.login(account, password,requestCodeLogin,new OnHttpResponseListener() {
						@Override
						public void onHttpResponse(int requestCode, String resultJson, Exception e) {
							dismissProgressDialog();
							if (e != null) {
								showShortToast(R.string.login_faild);
							}else {
								if (requestCode == requestCodeLogin) {
									try {
										Response response = parseObject(resultJson, Response.class);
										if (response == null) {
											throw new Exception("Response is null");
										}
										if (response.getCode() == 0) {

											Map<String, Object> map = JSON.parseObject(JSON.toJSONString(response.getData()));
											long userId = Long.parseLong(map.get("userId").toString());

											User user = new User(userId);
											Application.getInstance().saveCurrentUser(user);

											Intent intent = MainTabActivity.createIntent(getActivity(), userId);
											toActivity(intent);
											getActivity().finish(); // 关闭当前的Activity
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

				}




			}
		});
		ibRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCallback != null) {
					mCallback.onFragmentMessageListener(1);
				}

			}
		});

		ibForgetPassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCallback != null) {
					mCallback.onFragmentMessageListener(2);
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