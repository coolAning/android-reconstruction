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

package aning.reconstruction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import aning.reconstruction.R;
import aning.reconstruction.adapter.RenderAdapter;
import zuo.biao.library.base.BaseListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.model.Entry;
import zuo.biao.library.ui.GridAdapter;


public class OutputFragment extends BaseListFragment<Entry<String, String>, GridView, RenderAdapter> {
	private static final String TAG = "OutputFragment";

	//与Activity通信<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	public static final String ARGUMENT_USER_ID = "ARGUMENT_USER_ID";
	public static final String ARGUMENT_USER_NAME = "ARGUMENT_USER_NAME";
	/**创建一个Fragment实例
	 * @return
	 */
	public static OutputFragment createInstance() {
		return new OutputFragment();
	}

	/**创建一个Fragment实例
	 * @param userId
	 * @return
	 */
	public static OutputFragment createInstance(long userId) {
		return createInstance(userId, null);
	}
	/**创建一个Fragment实例
	 * @param userId
	 * @param userName
	 * @return
	 */
	public static OutputFragment createInstance(long userId, String userName) {
		OutputFragment fragment = new OutputFragment();

		Bundle bundle = new Bundle();
		bundle.putLong(ARGUMENT_USER_ID, userId);
		bundle.putString(ARGUMENT_USER_NAME, userName);

		fragment.setArguments(bundle);
		return fragment;
	}

	//与Activity通信>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.output_fragment);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initEvent();
		//功能归类分区方法，必须调用>>>>>>>>>>

		onRefresh();

		return view;//返回值必须为view
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initView() {//必须在onCreateView方法内调用
		super.initView();

	}

	@Override
	public void setList(final List<Entry<String, String>> list) {
		//示例代码<<<<<<<<<<<<<<<
		setList(new AdapterCallBack<RenderAdapter>() {

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}

			@Override
			public RenderAdapter createAdapter() {
				return new RenderAdapter(context);
			}
		});
		//示例代码>>>>>>>>>>>>>>>
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initData() {//必须在onCreateView方法内调用
		super.initData();

	}


	@Override
	public void getListAsync(int page) {
		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		showProgressDialog(R.string.loading);

		List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
		list.add(new Entry<String, String>("http://118.202.10.20:5001/static/screen_shot/1_shouban/0_0_0_0_0_0.png", "a"));
		list.add(new Entry<String, String>("http://118.202.10.20:5001/static/screen_shot/1_shouban/0_0_0_0_0_0.png", "b"));
		list.add(new Entry<String, String>("http://118.202.10.20:5001/static/screen_shot/1_shouban/0_0_0_0_0_0.png", "c"));
		list.add(new Entry<String, String>("http://118.202.10.20:5001/static/screen_shot/1_shouban/0_0_0_0_0_0.png", "d"));
		list.add(new Entry<String, String>("http://118.202.10.20:5001/static/screen_shot/1_shouban/0_0_0_0_0_0.png", "e"));


		onLoadSucceed(page, list);
		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreateView方法内调用
		super.initEvent();

		lvBaseList.setOnItemClickListener(this);
	}


	//示例代码<<<<<<<<<<<<<<<<<<<
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//实现单选
		showShortToast("选择了 " + adapter.getItem(position).getValue());
//		toActivity(UserActivity.createIntent(context, position));//一般用id，这里position仅用于测试 id));//
	}

	//示例代码>>>>>>>>>>>>>>>>>>>




	//生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}