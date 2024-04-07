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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.secneo.sdk.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import aning.reconstruction.R;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKInitEvent;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.thirdparty.afinal.core.AsyncTask;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.Log;


public class DroneActivity extends BaseActivity implements OnBottomDragListener {
	private static final String TAG = "DroneActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, DroneActivity.class).putExtra(INTENT_USER_ID, userId);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;

	//初始化变量文档示例
	public static final String FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change";
	private static BaseProduct mProduct;
	private Handler mHandler;

	private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
			Manifest.permission.VIBRATE,
			Manifest.permission.INTERNET,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.WAKE_LOCK,
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.CHANGE_WIFI_STATE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.BLUETOOTH,
			Manifest.permission.BLUETOOTH_ADMIN,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE,
	};
	private List<String> missingPermission = new ArrayList<>();
	private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);
	private static final int REQUEST_PERMISSION_CODE = 12345;
	//示例结束




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drone_activity, this);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>
		checkAndRequestPermissions();
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//示例代码<<<<<<<<
	private TextView tvDemo;
	//示例代码>>>>>>>>
	@Override
	public void initView() {//必须在onCreate方法内调用
		autoSetTitle();//自动设置标题为上个Activity传入的INTENT_TITLE

		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		tvDemo = findView(R.id.tvDemo);

		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}




	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用

		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//		showProgressDialog(R.string.loading);

		runThread(TAG + "initData", new Runnable() {
			@Override
			public void run() {

				new Handler().postDelayed(new Runnable() { //模拟延时
					@Override
					public void run() {

//						setContent(userId);
					}
				}, 500);
			}
		});

		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreate方法内调用

	}


	//示例代码<<<<<<<<<<<<<<<<<<<
	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

//			setContent(userId + 1);
			return;
		}

		finish();
	}

	//示例代码>>>>>>>>>>>>>>>>>>>
	private void checkAndRequestPermissions() {
		// Check for permissions
		for (String eachPermission : REQUIRED_PERMISSION_LIST) {
			if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
				missingPermission.add(eachPermission);
			}
		}
		// Request for missing permissions
		if (missingPermission.isEmpty()) {
			startSDKRegistration();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			showShortToast("Need to grant the permissions!");
			ActivityCompat.requestPermissions(this,
					missingPermission.toArray(new String[missingPermission.size()]),
					REQUEST_PERMISSION_CODE);
		}

	}
	/**
	 * Result of runtime permission request
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// Check for granted permission and remove from missing list
		if (requestCode == REQUEST_PERMISSION_CODE) {
			for (int i = grantResults.length - 1; i >= 0; i--) {
				if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					missingPermission.remove(permissions[i]);
				}
			}
		}
		// If there is enough permission, we will start the registration
		if (missingPermission.isEmpty()) {
			startSDKRegistration();
		} else {
			showShortToast("Missing permissions!!!");
		}
	}
	private void startSDKRegistration() {
		if (isRegistrationInProgress.compareAndSet(false, true)) {
			AsyncTask.execute(new Runnable() {
				@Override
				public void run() {
					showShortToast("registering, pls wait...");
					DJISDKManager.getInstance().registerApp(DroneActivity.this.getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
						@Override
						public void onRegister(DJIError djiError) {
							if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
								showShortToast("register success");
								DJISDKManager.getInstance().startConnectionToProduct();
							} else {
								showShortToast("register sdk fails, please check the bundle id and network connection!");
							}
							Log.v(TAG, djiError.getDescription());
						}

						@Override
						public void onProductDisconnect() {
							Log.d(TAG, "onProductDisconnect");
							showShortToast("Product Disconnected");
							notifyStatusChange();

						}
						@Override
						public void onProductConnect(BaseProduct baseProduct) {
							Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));
							showShortToast("Product Connected");
							notifyStatusChange();

						}

						@Override
						public void onProductChanged(BaseProduct baseProduct) {
							Log.d(TAG, String.format("onProductChanged newProduct:%s", baseProduct));
							notifyStatusChange();
						}

						@Override
						public void onComponentChange(BaseProduct.ComponentKey componentKey, BaseComponent oldComponent,
													  BaseComponent newComponent) {

							if (newComponent != null) {
								newComponent.setComponentListener(new BaseComponent.ComponentListener() {

									@Override
									public void onConnectivityChange(boolean isConnected) {
										Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
										notifyStatusChange();
									}
								});
							}
							Log.d(TAG,
									String.format("onComponentChange key:%s, oldComponent:%s, newComponent:%s",
											componentKey,
											oldComponent,
											newComponent));

						}
						@Override
						public void onInitProcess(DJISDKInitEvent djisdkInitEvent, int i) {

						}

						@Override
						public void onDatabaseDownloadProgress(long l, long l1) {

						}
					});
				}
			});
		}
	}
	private void notifyStatusChange() {
		mHandler.removeCallbacks(updateRunnable);
		mHandler.postDelayed(updateRunnable, 500);
	}

	private Runnable updateRunnable = new Runnable() {

		@Override
		public void run() {
			Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
			sendBroadcast(intent);
		}
	};



	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}