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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import aning.reconstruction.R;
import aning.reconstruction.application.FPVApplication;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.log.DJILog;
import dji.sdk.base.BaseComponent;
import dji.sdk.base.BaseProduct;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKInitEvent;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.thirdparty.afinal.core.AsyncTask;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.Log;

public class ConnectionActivity extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "ConnectionActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, ConnectionActivity.class).putExtra(INTENT_USER_ID, userId);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;

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
			Manifest.permission.RECORD_AUDIO
	};
	private List<String> missingPermission = new ArrayList<>();
	private AtomicBoolean isRegistrationInProgress = new AtomicBoolean(false);

	private static final int REQUEST_PERMISSION_CODE = 12345;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_activity);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>
		checkAndRequestPermissions();

		// Register the broadcast receiver for receiving the device connection's changes.
		IntentFilter filter = new IntentFilter();
		filter.addAction(FPVApplication.FLAG_CONNECTION_CHANGE);
		registerReceiver(mReceiver, filter);
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private TextView mTextConnectionStatus;
	private TextView mTextProduct;
	private TextView mVersionTv;
	private Button mBtnOpen;
	@Override
	public void initView() {//必须在onCreate方法内调用
		mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
		mTextProduct = (TextView) findViewById(R.id.text_product_info);

		mVersionTv = (TextView) findViewById(R.id.textView2);
		mVersionTv.setText(getResources().getString(R.string.sdk_version, DJISDKManager.getInstance().getSDKVersion()));

		mBtnOpen = (Button) findViewById(R.id.btn_open);

		mBtnOpen.setEnabled(false);
	}




	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用

	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreate方法内调用
		mBtnOpen.setOnClickListener(this);
	}


	//示例代码<<<<<<<<<<<<<<<<<<<
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btn_open: {
				Intent intent = DroneActivity.createIntent(this, userId);
				startActivity(intent);
				break;
			}
			default:
				break;
		}
	}

	//示例代码>>>>>>>>>>>>>>>>>>>


	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	/**
	 * Checks if there is any missing permissions, and
	 * requests runtime permission if needed.
	 */
	private void checkAndRequestPermissions() {
		// Check for permissions
		for (String eachPermission : REQUIRED_PERMISSION_LIST) {
			if (ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED) {
				missingPermission.add(eachPermission);
			}
		}
		// Request for missing permissions
		if (!missingPermission.isEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
					DJISDKManager.getInstance().registerApp(getApplicationContext(), new DJISDKManager.SDKManagerCallback() {
						@Override
						public void onRegister(DJIError djiError) {
							if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
								DJILog.e("App registration", DJISDKError.REGISTRATION_SUCCESS.getDescription());
								DJISDKManager.getInstance().startConnectionToProduct();
								showShortToast("Register Success");
							} else {
								showShortToast("Register sdk fails, check network is available");
							}
							Log.v(TAG, djiError.getDescription());
						}

						@Override
						public void onProductDisconnect() {
							Log.d(TAG, "onProductDisconnect");
							showShortToast("Product Disconnected");

						}

						@Override
						public void onProductConnect(BaseProduct baseProduct) {
							Log.d(TAG, String.format("onProductConnect newProduct:%s", baseProduct));
							showShortToast("Product Connected");

						}

						@Override
						public void onProductChanged(BaseProduct baseProduct) {
							Log.d(TAG, String.format("onProductChanged newProduct:%s", baseProduct));
							showShortToast("Product Changed");
						}

						@Override
						public void onComponentChange(BaseProduct.ComponentKey componentKey, BaseComponent oldComponent,
													  BaseComponent newComponent) {

							if (newComponent != null) {
								newComponent.setComponentListener(new BaseComponent.ComponentListener() {

									@Override
									public void onConnectivityChange(boolean isConnected) {
										Log.d(TAG, "onComponentConnectivityChanged: " + isConnected);
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

	protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			refreshSDKRelativeUI();
		}
	};

	@Override
	protected void onDestroy() {
		Log.e(TAG, "onDestroy");
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private void refreshSDKRelativeUI() {
		BaseProduct mProduct = FPVApplication.getProductInstance();

		if (null != mProduct && mProduct.isConnected()) {
			Log.v(TAG, "refreshSDK: True");
			mBtnOpen.setEnabled(true);

			String str = mProduct instanceof Aircraft ? "DJIAircraft" : "DJIHandHeld";
			mTextConnectionStatus.setText("Status: " + str + " connected");

			if (null != mProduct.getModel()) {
				mTextProduct.setText("" + mProduct.getModel().getDisplayName());
			} else {
				mTextProduct.setText(R.string.product_information);
			}

		} else {
			Log.v(TAG, "refreshSDK: False");
			mBtnOpen.setEnabled(false);

			mTextProduct.setText(R.string.product_information);
			mTextConnectionStatus.setText(R.string.connection_loose);
		}
	}

	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}