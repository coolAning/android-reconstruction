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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

import aning.reconstruction.R;
import aning.reconstruction.model.HTTPResponse;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;


/**activity示例
 * @author Lemon
 * @warn 复制到其它工程内使用时务必修改import R文件路径为所在应用包名
 * @use toActivity(DemoActivity.createIntent(...));
 */
public class UploadActivity extends BaseActivity implements OnBottomDragListener {
	private static final String TAG = "UploadActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId , Uri videoUri) {
		return new Intent(context, UploadActivity.class).putExtra(INTENT_USER_ID, userId).setData(videoUri);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;
	private Uri videoUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_activity, this);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);
		videoUri = intent.getData();

		//存储权限获取
		if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(UploadActivity.this,
					new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
		}


		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private EditText fileNameET;
	private EditText trainStepsET;
	private Button UploadButton;
	private VideoView videoView;
	@Override
	public void initView() {//必须在onCreate方法内调用
		autoSetTitle();//自动设置标题为上个Activity传入的INTENT_TITLE

		fileNameET = findView(R.id.video_name_et);
		trainStepsET = findView(R.id.train_steps_et);
		UploadButton = findView(R.id.upload_btn);
		videoView = findView(R.id.videoView);

	}

	/**
	 * 绑定视频到UI

	 */
	private void setVideo(Uri videoUri) {

		this.videoUri = videoUri;
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(videoUri);
		videoView.start();
		videoView.requestFocus();

	}

	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreate方法内调用
		setVideo(videoUri);
	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private final int requestCodeUpload = 1;
	private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
	@Override
	public void initEvent() {//必须在onCreate方法内调用
		UploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String fileName = fileNameET.getText().toString().trim();
				String trainSteps = trainStepsET.getText().toString().trim();
				if (fileName.isEmpty() || trainSteps.isEmpty()) {
					showShortToast(R.string.filename_or_steps_null);
				}else {
					showProgressDialog(R.string.on_uploadding);
					int trainStepsInt = Integer.parseInt(trainSteps);
					File videoFile = new File(getRealPathFromURI(UploadActivity.this, videoUri));
					System.out.println(videoFile.getAbsolutePath());
					HttpRequest.uploadVideo(videoFile,trainStepsInt ,fileName , requestCodeUpload ,new OnHttpResponseListener() {
						@Override
						public void onHttpResponse(int requestCode, String resultJson, Exception e) {
							dismissProgressDialog();
							if (e != null) {
								showShortToast(R.string.upload_faild);
							}else {
								if (requestCode == requestCodeUpload) {
									try {
										HTTPResponse HTTPResponse = parseObject(resultJson, HTTPResponse.class);
										if (HTTPResponse == null) {
											throw new Exception("Response is null");
										}
										if (HTTPResponse.getCode() == 0) {
											showShortToast(R.string.upload_success);
											getActivity().finish(); // 关闭当前的Activity

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
			}
		});
	}


	//示例代码<<<<<<<<<<<<<<<<<<<
	@Override
	public void onDragBottom(boolean rightToLeft) {
//		if (rightToLeft) {
//
//			setContent(userId + 1);
//			return;
//		}
//
//		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onReturnClick(View v) {
		finish();
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.moveToFirst()) {
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * 存储权限获取回调
	 * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
	 * @param permissions The requested permissions. Never null.
	 * @param grantResults The grant results for the corresponding permissions
	 *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
	 *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
	 *
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					return;
				} else {
					getActivity().finish(); // 关闭当前的Activity
				}
				return;
			}
		}
	}

	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}