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
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import aning.reconstruction.R;
import aning.reconstruction.fragment.LoginFragment;
import aning.reconstruction.fragment.OutputFragment;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class MainActivity extends BaseActivity implements OutputFragment.OnVisibilityChangeListener {
	private static final String TAG = "MainActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, MainActivity.class).putExtra(INTENT_USER_ID, userId);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private OutputFragment outputFragment;
	private TextView tvCancel;
	private TextView tvDelete;
	@Override
	public void initView() {//必须在onCreate方法内调用
		this.setStatusBarColor(R.color.colorMain);
		tvCancel = findView(R.id.cancel_tv);
		tvDelete = findView(R.id.delete_tv);
		outputFragment = OutputFragment.createInstance(getIntent().getLongExtra(INTENT_USER_ID, 0));
		fragmentManager
				.beginTransaction()
				.add(R.id.flMainFragmentContainer, outputFragment)
				.addToBackStack("loginFragment")
				.show(outputFragment)
				.commit();
	}




	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {


	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreate方法内调用
		tvCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//取消
				if (outputFragment != null){
					outputFragment.cancel();
				}
			}
		});

		tvDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//删除
				if (outputFragment != null){
					outputFragment.delete();
				}
			}
		});
	}



	//示例代码>>>>>>>>>>>>>>>>>>>


	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	//双击手机返回键退出<<<<<<<<<<<<<<<<<<<<<
	private long firstTime = 0;//第一次返回按钮计时
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode){
			case KeyEvent.KEYCODE_BACK:
				long secondTime = System.currentTimeMillis();
				if(secondTime - firstTime > 2000){
					showShortToast("再按一次退出");
					firstTime = secondTime;
				} else {//完全退出
					moveTaskToBack(false);//应用退到后台
					System.exit(0);
				}
				return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	// 实现outputfragment控制编辑按钮的接口
	@Override
	public void onVisibilityChange(int visibility) {
		if (visibility == 0) {
			tvCancel.setVisibility(View.VISIBLE);
			tvDelete.setVisibility(View.VISIBLE);
		} else {
			tvCancel.setVisibility(View.GONE);
			tvDelete.setVisibility(View.GONE);
		}
	}




	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}