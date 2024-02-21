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

package aning.reconstruction.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aning.reconstruction.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;


/**
 * 登录页面Activity

 */
public class LoginActivity extends BaseActivity implements OnBottomDragListener {
	private static final String TAG = "LoginActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, LoginActivity.class).putExtra(INTENT_USER_ID, userId);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO demo_activity改为你所需要的layout文件；传this是为了底部左右滑动手势
		setContentView(R.layout.login_activity, this);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private Button btLogin;
	@Override
	public void initView() {//必须在onCreate方法内调用
		autoSetTitle();//自动设置标题为上个Activity传入的INTENT_TITLE

		btLogin = findViewById(R.id.login);
	}


//	/**绑定数据到UI
//	 * @param userId_
//	 */
//	private void setContent(final long userId_) {
//		this.userId = userId_;
//
//		runUiThread(new Runnable() {
//			@Override
//			public void run() {
//
//				dismissProgressDialog();
//				tvDemo.setText("userId = " + userId);
//			}
//		});
//	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用

		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//		showProgressDialog(R.string.loading);

//		runThread(TAG + "initData", new Runnable() {
//			@Override
//			public void run() {
//
//				new Handler().postDelayed(new Runnable() { //模拟延时
//					@Override
//					public void run() {
//
////						setContent(userId);
//					}
//				}, 500);
//			}
//		});

		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreate方法内调用
		btLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 创建一个意图，从当前Activity跳转到另一个Activity
				Intent intent = MainTabActivity.createIntent(LoginActivity.this, userId);
				// intent.putExtra("key", value);

				// 启动另一个Activity
				startActivity(intent);
				finish(); // 关闭当前的Activity
			}
		});
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			return;
		}
		finish();
	}




	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}