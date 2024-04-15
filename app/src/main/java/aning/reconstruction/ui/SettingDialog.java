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

package aning.reconstruction.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aning.reconstruction.R;
import zuo.biao.library.util.StringUtil;

/**通用对话框类
 * @author Lemon
 * @use new AlertDialog(...).show();
 */
public class SettingDialog extends Dialog implements View.OnClickListener {
	//	private static final String TAG = "AlertDialog";

	/**
	 * 自定义Dialog监听器
	 */
	public interface OnDialogButtonClickListener {

		/**点击按钮事件的回调方法
		 * @param requestCode 传入的用于区分某种情况下的showDialog
		 * @param isPositive
		 */
		void onDialogButtonClick(int requestCode, boolean isPositive, String name, int trainSteps);
	}


	private Context context;
	private String title = "参数设置";

	private boolean showNegativeButton = true;
	private int requestCode;
	private OnDialogButtonClickListener listener;


	/**
	 * 带监听器参数的构造函数
	 */
	public SettingDialog(Context context ,int requestCode, OnDialogButtonClickListener listener) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.requestCode = requestCode;
		this.listener = listener;
	}

	private TextView tvTitle;
	private Button btnPositive;
	private Button btnNegative;
	private TextView tvName;
	private TextView tvTrainSteps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.setting_dialog);
		setCanceledOnTouchOutside(true);

		tvTitle = findViewById(R.id.tvSettingDialogTitle);
		btnPositive = findViewById(R.id.btnSettingDialogPositive);
		btnNegative = findViewById(R.id.btnSettingDialogNegative);
		tvName = findViewById(R.id.video_name_et);
		tvTrainSteps = findViewById(R.id.train_steps_et);

		tvTitle.setVisibility(StringUtil.isNotEmpty(title, true) ? View.VISIBLE : View.GONE);
		tvTitle.setText("" + StringUtil.getCurrentString());


		btnPositive.setOnClickListener(this);

		if (showNegativeButton) {
			btnNegative.setOnClickListener(this);
		} else {
			btnNegative.setVisibility(View.GONE);
		}







	}

	@Override
	public void onClick(final View v) {
		//输入规范检查
		String name = tvName.getText().toString();
		String trainSteps = tvTrainSteps.getText().toString();
		if (StringUtil.isNotEmpty(name, true) == false) {
			tvName.setError("请输入视频名称");
			return;
		}
		if (StringUtil.isNotEmpty(trainSteps, true) == false) {
			tvTrainSteps.setError("请输入训练步数");
			return;
		}
		int trainStepsInt = Integer.parseInt(trainSteps);

		if (v.getId() == R.id.btnSettingDialogPositive) {
			listener.onDialogButtonClick(requestCode, true,name,trainStepsInt);
		} else if (v.getId() == R.id.btnSettingDialogNegative) {
			listener.onDialogButtonClick(requestCode, false,name,trainStepsInt);
		}

		dismiss();
	}

}

