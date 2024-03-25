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

import static zuo.biao.library.util.JSON.parseObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.fastjson.TypeReference;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aning.reconstruction.R;
import aning.reconstruction.activity.MainTabActivity;
import aning.reconstruction.adapter.RenderAdapter;
import aning.reconstruction.application.Application;
import aning.reconstruction.model.Response;
import aning.reconstruction.model.User;
import aning.reconstruction.util.HttpRequest;
import zuo.biao.library.base.BaseListFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.model.Entry;
import zuo.biao.library.ui.GridAdapter;
import zuo.biao.library.util.JSON;
import zuo.biao.library.util.Log;


public class OutputFragment extends BaseListFragment<Entry<String, String>, GridView, RenderAdapter> implements  OnStopLoadListener, OnRefreshListener, OnLoadmoreListener {
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

//		onRefresh();
		srlBaseHttpRecycler.autoRefresh();
		return view;//返回值必须为view
	}

	protected SmartRefreshLayout srlBaseHttpRecycler;
	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	@Override
	public void initView() {//必须在onCreateView方法内调用
		super.initView();
		srlBaseHttpRecycler = findView(R.id.srlBaseHttpRecycler);
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


	private final int requestCodeGetModelList = 1;
	@Override
	public void getListAsync(int page) {

		List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();

		HttpRequest.getModelList(requestCodeGetModelList,new OnHttpResponseListener() {
			@Override
			public void onHttpResponse(int requestCode, String resultJson, Exception e) {
				if (e != null) {
					showShortToast(R.string.load_faild);
				}else {
					if (requestCode == requestCodeGetModelList) {
						try {
							Response response = parseObject(resultJson, Response.class);
							if (response == null) {
								throw new Exception("Response is null");
							}
							if (response.getCode() == 0) {
								List<Map<String,Object>> mapList = JSON.parseJson(JSON.toJSONString(response.getData()), new TypeReference<List<Map<String,Object>>>() {});
								for (Map<String,Object> map : mapList) {
									list.add(new Entry<String, String>(map.get("url").toString(), map.get("name").toString()));
								}
								onLoadSucceed(page, list);
								onStopRefresh();
							} else {
								showShortToast(response.getMsg());
							}
						} catch (Exception error) {
							showShortToast(R.string.sys_error);
						}
					}
				}
			}
		});

	}


	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initEvent() {//必须在onCreateView方法内调用
		super.initEvent();

		lvBaseList.setOnItemClickListener(this);
		lvBaseList.setOnItemLongClickListener(this);
		srlBaseHttpRecycler.setOnRefreshListener(this);
		srlBaseHttpRecycler.setOnLoadmoreListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (adapter.getCheckStatus()) {
			adapter.setItemChecked(position, ! adapter.getItemChecked(position));
		}else {
			//实现单选
			showShortToast("选择了 " + adapter.getItem(position).getValue());
//		toActivity(UserActivity.createIntent(context, position));//一般用id，这里position仅用于测试 id));//
		}

	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//		adapter.setItemChecked(position, true);
		adapter.setHasCheck(true);
		adapter.setItemChecked(position, true);
		return true;
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		onRefresh();
	}
	@Override
	public void onLoadmore(RefreshLayout refreshlayout) {
//		onLoadMore();
		onStopLoadMore(false);
	}

	@Override
	public void onStopRefresh() {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				srlBaseHttpRecycler.finishRefresh();
				srlBaseHttpRecycler.setLoadmoreFinished(false);
			}
		});
	}

	@Override
	public void onStopLoadMore(boolean isHaveMore) {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				if (isHaveMore) {
					srlBaseHttpRecycler.finishLoadmore();
				} else {
					srlBaseHttpRecycler.finishLoadmoreWithNoMoreData();
				}
				srlBaseHttpRecycler.setLoadmoreFinished(! isHaveMore);
			}
		});
	}

}