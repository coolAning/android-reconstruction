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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import aning.reconstruction.R;
import aning.reconstruction.application.Application;
import aning.reconstruction.fragment.OutputFragment;
import aning.reconstruction.fragment.BottomSheetFragment;
import zuo.biao.library.base.BaseActivity;

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
		// 加载单独封装的 Navigation Drawer 布局

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
	private BottomAppBar bottomAppBar;
	private FloatingActionButton fab;
	private boolean isDeleteMode = false;
	private BottomSheetFragment bottomSheet;
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;
	private TextView tobBarTitle;
	private TextInputLayout search_bar_layout;
	private TextInputEditText search_bar;
	@Override
	public void initView() {//必须在onCreate方法内调用
		this.setStatusBarColor(R.color.colorMain);
		bottomSheet = BottomSheetFragment.createInstance(userId);
		tvCancel = findView(R.id.cancel_tv);
		tvDelete = findView(R.id.delete_tv);
		bottomAppBar = findView(R.id.bottom_app_bar);
		fab = findView(R.id.fab);
		outputFragment = OutputFragment.createInstance(getIntent().getLongExtra(INTENT_USER_ID, 0));
		fragmentManager
				.beginTransaction()
				.add(R.id.flMainFragmentContainer, outputFragment)
				.addToBackStack("loginFragment")
				.show(outputFragment)
				.commit();

		drawerLayout = findView(R.id.drawer_layout);
		navigationView = findView(R.id.nav_view);
		search_bar_layout = findView(R.id.search_bar_layout);
		search_bar = findView(R.id.search_bar_edit_text);
		tobBarTitle = findView(R.id.topbar_title_tv);
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

		bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawerLayout.open();
			}
		});

		bottomAppBar.setOnMenuItemClickListener(new BottomAppBar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_search:
						if (search_bar_layout.getVisibility() == View.VISIBLE) {
							search_bar_layout.setVisibility(View.GONE);
							tobBarTitle.setVisibility(View.VISIBLE);
						} else {
							search_bar_layout.setVisibility(View.VISIBLE);
							tobBarTitle.setVisibility(View.GONE);
							search_bar.requestFocus();
							// 唤醒输入法
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.showSoftInput(search_bar, InputMethodManager.SHOW_IMPLICIT);
						}
						break;
					case R.id.action_delete:
						isDeleteMode = !isDeleteMode;
						if(isDeleteMode) {
							outputFragment.onDeleteMode();
						}else {
							outputFragment.cancel();
						}
						break;
				}
				return true;
			}
		});
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bottomSheet.show(getSupportFragmentManager(), BottomSheetFragment.TAG);
			}
		});

		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.personal_setting:
						toActivity(SettingActivity.createIntent(context));
						break;
					case R.id.model_setting:
						showShortToast("model_setting clicked");
						break;
					case R.id.exit:
						new MaterialAlertDialogBuilder(context)
								.setTitle("退出登录")
								.setMessage("确定退出登录？")
								.setPositiveButton("确定", (dialog, which) -> {
									//退出登录
									Application.getInstance().logout();
									context.finish();
								})
								.setNegativeButton("取消", (dialog, which) -> {
									//取消
									item.setChecked(false);
								}).show();
						break;
				}

				drawerLayout.close();
				return true;
			}
		});
		search_bar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				outputFragment.search(search_bar.getText().toString());
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
				// 检查是否处于删除模式 搜索模式 抽屉模式
				if (isDeleteMode) {
					isDeleteMode = false;
					outputFragment.cancel();
				}else if (search_bar_layout.getVisibility() == View.VISIBLE) {
					search_bar_layout.setVisibility(View.GONE);
					tobBarTitle.setVisibility(View.VISIBLE);
				}else if (drawerLayout.isOpen()) {
					drawerLayout.close();
				}else {
					// 退出流程
					long secondTime = System.currentTimeMillis();
					if(secondTime - firstTime > 2000){
						showShortToast("再按一次退出");
						firstTime = secondTime;
					} else {//完全退出
						moveTaskToBack(false);//应用退到后台
						System.exit(0);
					}
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