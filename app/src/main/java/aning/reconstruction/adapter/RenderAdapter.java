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

package aning.reconstruction.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import aning.reconstruction.R;
import zuo.biao.library.model.Entry;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class RenderAdapter extends BaseAdapter {
	private static final String TAG = "RenderAdapter";

	public RenderAdapter(Activity context) {
		this(context, R.layout.render_item);
	}
	private final Activity context;
	private final LayoutInflater inflater;
	public RenderAdapter(Activity context, int layoutRes) {
		this.context = context;
		this.inflater = context.getLayoutInflater();
		setLayoutRes(layoutRes);
	}



	private int layoutRes;//item视图资源
	public void setLayoutRes(int layoutRes) {
		this.layoutRes = layoutRes;
	}
	private boolean hasName = true;//是否显示名字
	public RenderAdapter setHasName(boolean hasName) {
		this.hasName = hasName;
		return this;
	}




	private List<Entry<String, String>> list;
	/**刷新列表
	 * @param list
	 */
	public synchronized void refresh(List<Entry<String, String>> list) {
		if (list != null && list.size() > 0) {
			initList(list);
		}
		notifyDataSetChanged();
	}


	private void initList(List<Entry<String, String>> list) {
		this.list = list;
	}


	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Entry<String, String> getItem(int position) {
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = convertView == null ? null : (ViewHolder) convertView.getTag();
		if (holder == null) {
			convertView = inflater.inflate(layoutRes, parent, false);

			holder = new ViewHolder();
			holder.ivPicture = convertView.findViewById(R.id.picture_iv);
			if (hasName) {
				holder.tvName = convertView.findViewById(R.id.name_tv);
			}

			convertView.setTag(holder);
		}

		final Entry<String, String> kvb = getItem(position);
		final String name = kvb.getValue();

		Glide.with(context).load(kvb.getKey()).into(holder.ivPicture);

		if (hasName) {
			holder.tvName.setVisibility(View.VISIBLE);
			holder.tvName.setText(StringUtil.getTrimedString(name));
		}



		return convertView;
	}

	static class ViewHolder {
		public ImageView ivPicture;
		public TextView tvName;
	}


}
