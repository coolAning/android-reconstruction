package aning.reconstruction.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import aning.reconstruction.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;

/**模型设置界面Activity
 * @author Lemon
 * @use toActivity(SettingActivity.createIntent(...));
 */
public class ModelSettingActivity extends BaseActivity {
	private static final String TAG = "ModelSettingActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, ModelSettingActivity.class).putExtra(INTENT_USER_ID, userId);
	}


	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	private long userId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.model_setting_activity);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);
		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private Button changeButton;
	private EditText trainStepET;
	private EditText aabbET;
	private EditText pictureQualityET;

	private boolean isChange = false;
	@Override
	public void initView() {//必须调用
		this.setStatusBarColor(R.color.colorMain);
		changeButton = findView(R.id.change);
		trainStepET = findView(R.id.train_steps_et);
		aabbET = findView(R.id.aabb);
		pictureQualityET = findView(R.id.picture_quality_et);
	}







	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	public void initData() {//必须调用


		runThread(TAG + "initData", new Runnable() {

			@Override
			public void run() {
				trainStepET.setText(StringUtil.get(SettingUtil.getInt(SettingUtil.KEY_TRAIN_STEPS, 1000)));
				aabbET.setText(StringUtil.get(SettingUtil.getInt(SettingUtil.KEY_AABB, 32)));
				pictureQualityET.setText(StringUtil.get(SettingUtil.getInt(SettingUtil.KEY_PICTURE_QUALITY, 1)));
			}
		});


	}



	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须调用
		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isChange){
					int[] values = new int[3];
					values[0] = Integer.parseInt((trainStepET.getText().toString()));
					values[1] = Integer.parseInt((aabbET.getText().toString()));
					values[2] = Integer.parseInt((pictureQualityET.getText().toString()));
					SettingUtil.putAllInt(values);
					showShortToast("修改成功");
					finish();
				}
			}
		});


		trainStepET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changeButton.setClickable(true);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtil.isNotEmpty(s.toString(), true)){
					changeButton.setClickable(true);
					isChange = true;
				}else {
					trainStepET.setError("训练步数不能为空");
					changeButton.setClickable(false);
				}
			}
		});

		aabbET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changeButton.setClickable(true);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtil.isNotEmpty(s.toString(), true)){
					changeButton.setClickable(true);
					isChange = true;
				}else {
					aabbET.setError("AABB参数不能为空");
					changeButton.setClickable(false);
				}
			}
		});

		pictureQualityET.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changeButton.setClickable(true);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (StringUtil.isNotEmpty(s.toString(), true)){
					changeButton.setClickable(true);
					isChange = true;
				}else {
					pictureQualityET.setError("图片质量不能为空");
					changeButton.setClickable(false);
				}
			}
		});

	}


	@Override
	public void finish() {
		//重写返回键事件

		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();


	}


	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>







	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
