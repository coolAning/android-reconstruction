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
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aning.reconstruction.R;
import aning.reconstruction.model.Response;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.model.Entry;
import zuo.biao.library.util.JSON;

public class RenderActivity extends BaseActivity implements OnBottomDragListener, OnHttpResponseListener {
	private static final String TAG = "RenderActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";
	public static final String INTENT_VIDEO_NAME = "INTENT_VIDEO_NAME";
	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId, String videoName) {
		return new Intent(context, RenderActivity.class).putExtra(INTENT_USER_ID, userId).putExtra(INTENT_VIDEO_NAME, videoName);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	private long userId = 0;
	private String videoName = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.render_activity, this);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);
		videoName = intent.getStringExtra(INTENT_VIDEO_NAME);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	private ImageView ivRender;
	private ImageButton ibMoveW;
	private ImageButton ibMoveS;
	private ImageButton ibMoveA;
	private ImageButton ibMoveD;
	private ImageButton ibDirectionW;
	private ImageButton ibDirectionS;
	private ImageButton ibDirectionA;
	private ImageButton ibDirectionD;
	@Override
	public void initView() {//必须在onCreate方法内调用
		ivRender = findView(R.id.render_photo_iv);
		//移动键四方位
		ibMoveW = findView(R.id.w_ib);
		ibMoveS = findView(R.id.s_ib);
		ibMoveA = findView(R.id.a_ib);
		ibMoveD = findView(R.id.d_ib);
		//方向键四方位
		ibDirectionW = findView(R.id.direction_w_ib);
		ibDirectionS = findView(R.id.direction_s_ib);
		ibDirectionA = findView(R.id.direction_a_ib);
		ibDirectionD = findView(R.id.direction_d_ib);
	}


	private void setIvRender(String url) {

		runUiThread(new Runnable() {
			@Override
			public void run() {
				Glide.with(context).load(url).into(ivRender);

			}
		});
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private static final int requestCodeRender = 1;
	@Override
	public void initData() {//必须在onCreate方法内调用
		showProgressDialog(R.string.loading);

		runThread(TAG + "initData", new Runnable() {
			@Override
			public void run() {
				HttpRequest.render(true,videoName,null,null,requestCodeRender,RenderActivity.this);
			}
		});
	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {
		ibMoveW.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Object> translation = new ArrayList<>();
				translation.add("w");
				translation.add(1);
				HttpRequest.render(false,videoName,translation,null,requestCodeRender,RenderActivity.this);
			}
		});
		ibMoveS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Object> translation = new ArrayList<>();
				translation.add("s");
				translation.add(1);
				HttpRequest.render(false,videoName,translation,null,requestCodeRender,RenderActivity.this);
			}
		});
		ibMoveA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Object> translation = new ArrayList<>();
				translation.add("a");
				translation.add(1);
				HttpRequest.render(false,videoName,translation,null,requestCodeRender,RenderActivity.this);
			}
		});
		ibMoveD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Object> translation = new ArrayList<>();
				translation.add("d");
				translation.add(1);
				HttpRequest.render(false,videoName,translation,null,requestCodeRender,RenderActivity.this);
			}
		});


		// 方向键四方位
		ibDirectionW.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Integer> rotationList = new ArrayList<>();
				rotationList.add(0);
				rotationList.add(-10);
				rotationList.add(0);
				HttpRequest.render(false,videoName,null,rotationList,requestCodeRender,RenderActivity.this);
			}
		});
		ibDirectionS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Integer> rotationList = new ArrayList<>();
				rotationList.add(0);
				rotationList.add(10);
				rotationList.add(0);
				HttpRequest.render(false,videoName,null,rotationList,requestCodeRender,RenderActivity.this);
			}
		});
		ibDirectionA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Integer> rotationList = new ArrayList<>();
				rotationList.add(0);
				rotationList.add(0);
				rotationList.add(10);
				HttpRequest.render(false,videoName,null,rotationList,requestCodeRender,RenderActivity.this);
			}
		});
		ibDirectionD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgressDialog(R.string.loading);
				List<Integer> rotationList = new ArrayList<>();
				rotationList.add(0);
				rotationList.add(0);
				rotationList.add(-10);
				HttpRequest.render(false,videoName,null,rotationList,requestCodeRender,RenderActivity.this);
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
	public void onHttpResponse(int requestCode, String resultJson, Exception e) {
		dismissProgressDialog();
		if (e != null) {
			showShortToast(R.string.load_faild);
		}else {
			if (requestCode == requestCodeRender) {
				try {
					Response response = parseObject(resultJson, Response.class);
					if (response == null) {
						throw new Exception("Response is null");
					}
					if (response.getCode() == 0) {
						Map<String,String> dataMap = (Map<String, String>) response.getData();
						setIvRender(dataMap.get("url"));

					} else {
						showShortToast(response.getMsg());
					}
				} catch (Exception error) {
					showShortToast(R.string.sys_error);
				}
			}
		}
	}


	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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




	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}