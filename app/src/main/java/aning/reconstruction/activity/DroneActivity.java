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

import static zuo.biao.library.util.JSON.parseObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import aning.reconstruction.R;
import aning.reconstruction.application.FPVApplication;
import aning.reconstruction.model.HTTPResponse;
import aning.reconstruction.ui.SettingDialog;
import aning.reconstruction.util.HttpRequest;
import dji.common.error.DJIError;
import dji.common.flightcontroller.virtualstick.RollPitchControlMode;
import dji.common.flightcontroller.virtualstick.YawControlMode;
import dji.common.product.Model;
import dji.common.useraccount.UserAccountState;
import dji.common.util.CommonCallbacks;
import dji.sdk.base.BaseProduct;
import dji.sdk.camera.Camera;
import dji.sdk.camera.VideoFeeder;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.products.Aircraft;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.sdk.useraccount.UserAccountManager;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.SettingUtil;


public class DroneActivity extends BaseActivity implements TextureView.SurfaceTextureListener, View.OnClickListener {
	private static final String TAG = "DroneActivity";



	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	//后端rtmp地址url
	private String baseLiveShowUrl = "";

	private String liveShowUrl = "";
	private String name = "";
	private int trainSteps = 0;

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

	private Handler handler;
	protected VideoFeeder.VideoDataListener mReceivedVideoDataListener = null;
	protected VideoFeeder.VideoDataListener transcoded_data_listener = null;

	// Codec for video live view
	protected DJICodecManager mCodecManager = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drone_activity);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);
		String ip = SettingUtil.getCurrentServerAddress().replaceAll("http://|https://", "").replaceAll(":[0-9]{1,5}", "");
		Log.i(TAG, "ip: " + ip);
		baseLiveShowUrl = "rtmp://" + ip + ":6666/live/";

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	protected TextureView mVideoSurface = null;
	private Button settingBtn;
	private ToggleButton recordBtn;
	private TextView recordingTime;
	private Button takeOffBtn;
	private Button landBtn;
	@Override
	public void initView() {//必须在onCreate方法内调用
		// init mVideoSurface
		mVideoSurface = (TextureView)findViewById(R.id.video_previewer_surface);

		recordingTime = (TextView) findViewById(R.id.timer);
		recordBtn = (ToggleButton) findViewById(R.id.start_upload_btn);
		settingBtn = (Button) findViewById(R.id.parameter_setting_btn);
		recordingTime.setVisibility(View.INVISIBLE);

		recordBtn.setClickable(false);

		takeOffBtn = (Button) findViewById(R.id.takeoff);
		landBtn = (Button) findViewById(R.id.land);

	}

	/**
	 * 初始化视频预览器
	 */
	private void initPreviewer() {

		BaseProduct product = FPVApplication.getProductInstance();

		if (product == null || !product.isConnected()) {
			showShortToast(getString(R.string.disconnected));
		} else {
			if (null != mVideoSurface) {
				mVideoSurface.setSurfaceTextureListener(this);
			}
			if (!product.getModel().equals(Model.UNKNOWN_AIRCRAFT)) {
				VideoFeeder.getInstance().getPrimaryVideoFeed().addVideoDataListener(mReceivedVideoDataListener);
			}
		}




	}

	/**
	 * 解除视频预览器初始化监听

	 */
	private void uninitPreviewer() {
		Camera camera = FPVApplication.getCameraInstance();
		if (camera != null){
			// Reset the callback
			VideoFeeder.getInstance().getPrimaryVideoFeed().addVideoDataListener(null);
		}
	}



	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用

		handler = new Handler();
		mReceivedVideoDataListener = new VideoFeeder.VideoDataListener() {

			@Override
			public void onReceive(byte[] videoBuffer, int size) {
				if (mCodecManager != null) {
					mCodecManager.sendDataToDecoder(videoBuffer, size);
				}
			}
		};

	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initEvent() {
		settingBtn.setOnClickListener(this);
		if (null != mVideoSurface) {
			mVideoSurface.setSurfaceTextureListener(this);
		}

		recordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					startUpload();
				} else {
					stopUpload();
				}
			}
		});

		//无人机起飞
		takeOffBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取飞行控制器
				FlightController flightController = ((Aircraft)DJISDKManager.getInstance().getProduct()).getFlightController();

				// 起飞
				flightController.startTakeoff(new CommonCallbacks.CompletionCallback() {
					@Override
					public void onResult(DJIError djiError) {
						if (djiError == null) {
							Log.d(TAG, "Takeoff success");
						} else {
							Log.d(TAG, "Takeoff failure: " + djiError.getDescription());
						}
					}
				});

				// 设置手柄控制模式
				flightController.setRollPitchControlMode(RollPitchControlMode.ANGLE);
				flightController.setYawControlMode(YawControlMode.ANGULAR_VELOCITY);

				takeOffBtn.setVisibility(View.GONE);
				landBtn.setVisibility(View.VISIBLE);
			}
		});


		//无人机降落

		landBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 获取飞行控制器
				FlightController flightController = ((Aircraft)DJISDKManager.getInstance().getProduct()).getFlightController();

				// 降落
				flightController.startLanding(new CommonCallbacks.CompletionCallback() {
					@Override
					public void onResult(DJIError djiError) {
						if (djiError == null) {
							Log.d(TAG, "Landing success");
						} else {
							Log.d(TAG, "Landing failure: " + djiError.getDescription());
						}
					}
				});
				landBtn.setVisibility(View.GONE);
				takeOffBtn.setVisibility(View.VISIBLE);
			}
		});

	}





	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.parameter_setting_btn:
				new SettingDialog(this, 1, new SettingDialog.OnDialogButtonClickListener() {
					@Override
					public void onDialogButtonClick(int requestCode, boolean isPositive, String filename, int trainStep) {
						if (isPositive) {
							name=filename;
							trainSteps = trainStep;
							recordBtn.setClickable(true);
							liveShowUrl = baseLiveShowUrl + String.valueOf(userId)+"_" + name;
							Log.i(TAG, "liveshowurl: " + liveShowUrl);
						}
					}
				}).show();
				break;
			default:
				break;
		}
	}

	// Method for starting recording
	private void startUpload(){
//		showShortToast("Start Live Show");
		if (!isLiveStreamManagerOn()) {
			return;
		}
		if (DJISDKManager.getInstance().getLiveStreamManager().isStreaming()) {
			showShortToast("Live Show already started!");
			return;
		}
		new Thread() {
			@Override
			public void run() {
				showShortToast(liveShowUrl);
				DJISDKManager.getInstance().getLiveStreamManager().setLiveUrl(liveShowUrl);
				int result = DJISDKManager.getInstance().getLiveStreamManager().startStream();
				DJISDKManager.getInstance().getLiveStreamManager().setStartTime();

//				showShortToast("startLive:" + result +
//						"\n isVideoStreamSpeedConfigurable:" + DJISDKManager.getInstance().getLiveStreamManager().isVideoStreamSpeedConfigurable() +
//						"\n isLiveAudioEnabled:" + DJISDKManager.getInstance().getLiveStreamManager().isLiveAudioEnabled());
			}
		}.start();
		// 启动计时器
		recordTime = 0;
		final int requestCodeSetDrone = 2;
		recordHandler.post(recordRunnable);
		// 请求后端取流
		HttpRequest.setDroneVideo(name,trainSteps,requestCodeSetDrone,new OnHttpResponseListener() {
			@Override
			public void onHttpResponse(int requestCode, String resultJson, Exception e) {
				if (e != null) {
					showShortToast(R.string.upload_faild);
				}else {
					if (requestCode == requestCodeSetDrone) {
						try {
							HTTPResponse HTTPResponse = parseObject(resultJson, HTTPResponse.class);
							if (HTTPResponse == null) {
								throw new Exception("Response is null");
							}
							if (HTTPResponse.getCode() == 0) {
								showShortToast(R.string.drone_video_uploading);
							} else {
								showShortToast(HTTPResponse.getMsg());
							}
						} catch (Exception error) {
							showShortToast(R.string.sys_error);
						}
					}
				}
			}
		});
	}

	// Method for stopping recording
	private void stopUpload(){
		if (!isLiveStreamManagerOn()) {
			return;
		}
		DJISDKManager.getInstance().getLiveStreamManager().stopStream();
		showShortToast("停止上传");
		// 停止计时器
		recordHandler.removeCallbacks(recordRunnable);
		recordingTime.setVisibility(View.INVISIBLE);
	}

	private int recordTime = 0; // 记录录制时间的变量
	private Handler recordHandler = new Handler(); // 用于更新 UI 的 Handler
	private Runnable recordRunnable = new Runnable() { // 每秒更新 recordTime 并更新 UI 的 Runnable
		@Override
		public void run() {
			recordTime++;
			int minutes = (recordTime % 3600) / 60;
			int seconds = recordTime % 60;
			final String timeString = String.format("%02d:%02d", minutes, seconds);
			recordingTime.setVisibility(View.VISIBLE);
			recordingTime.setText(timeString);
			recordHandler.postDelayed(this, 1000);
		}
	};
	protected void onProductChange() {
		initPreviewer();
		loginAccount();
	}

	private void loginAccount(){

		UserAccountManager.getInstance().logIntoDJIUserAccount(this,
				new CommonCallbacks.CompletionCallbackWith<UserAccountState>() {
					@Override
					public void onSuccess(final UserAccountState userAccountState) {
						android.util.Log.e(TAG, "Login Success");
					}
					@Override
					public void onFailure(DJIError error) {
						showShortToast("Login Error:"
								+ error.getDescription());
					}
				});
	}



	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		android.util.Log.e(TAG, "onSurfaceTextureAvailable");
		if (mCodecManager == null) {
			mCodecManager = new DJICodecManager(this, surface, width, height);
		}
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		android.util.Log.e(TAG, "onSurfaceTextureSizeChanged");
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		Log.e(TAG,"onSurfaceTextureDestroyed");
		if (mCodecManager != null) {
			mCodecManager.cleanSurface();
			mCodecManager = null;
		}

		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
	}


	@Override
	public void onResume() {
		android.util.Log.e(TAG, "onResume");
		super.onResume();
		initPreviewer();
		onProductChange();

		if(mVideoSurface == null) {
			android.util.Log.e(TAG, "mVideoSurface is null");
		}
	}

	@Override
	public void onPause() {
		android.util.Log.e(TAG, "onPause");
		uninitPreviewer();
		super.onPause();
	}

	@Override
	public void onStop() {
		android.util.Log.e(TAG, "onStop");
		super.onStop();
	}

	public void onReturn(View view){
		android.util.Log.e(TAG, "onReturn");
		this.finish();
	}

	@Override
	protected void onDestroy() {
		android.util.Log.e(TAG, "onDestroy");
		uninitPreviewer();
		super.onDestroy();
	}
	private boolean isLiveStreamManagerOn() {
		if (DJISDKManager.getInstance().getLiveStreamManager() == null) {
			showShortToast("No live stream manager!");
			return false;
		}
		return true;
	}
	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void onReturnClick(View v) {
		finish();
	}






	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}