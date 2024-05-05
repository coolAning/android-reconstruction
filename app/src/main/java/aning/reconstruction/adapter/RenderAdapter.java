package aning.reconstruction.adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
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
	private static final String TAG = "GridAdapter";

	public RenderAdapter(Activity context) {
		this(context,R.layout.render_item );
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
	private boolean hasCheck = false;//是否使用标记功能
	public RenderAdapter setHasCheck(boolean hasCheck) {
		this.hasCheck = hasCheck;
		//重置为全false
		if (!hasCheck) {
			for (Integer key : hashMap.keySet()) {
				hashMap.put(key, false);
			}
		}
		notifyDataSetChanged();
		return this;
	}


	private HashMap<Integer, Boolean> hashMap;
	public boolean getItemChecked(int position) {
		if (hasCheck == false) {
			Log.e(TAG, "<<< !!! hasCheck == false  >>>>> ");
			return false;
		}
		return hashMap.get(position);
	}
	public void setItemChecked(int position, boolean isChecked) {
		if (hasCheck == false) {
			Log.e(TAG, "<<< !!! hasCheck == false >>>>> ");
			return;
		}
		hashMap.put(position, isChecked);
		notifyDataSetChanged();
	}
	public HashMap<Integer, Boolean> getMap() {
		return hashMap;
	}

	public boolean getCheckStatus(){
		return hasCheck;
	}



	private List<Entry<String, String>> list;
	/**刷新列表
	 * @param list
	 */
	public synchronized void refresh(List<Entry<String, String>> list) {
		if (list != null && list.size() > 0) {
			initList(list);
		}
		if (hasCheck) {
			selectedCount = 0;
			for (int i = 0; i < this.list.size(); i++) {
				if (getItemChecked(i) == true) {
					selectedCount ++;
				}
			}
		}
		notifyDataSetChanged();
	}

	/**标记List<String>中的值是否已被选中。
	 * 不需要可以删除，但“this.list = list;”这句
	 * 要放到constructor【这个adapter只有ModleAdapter(Context context, List<Object> list)这一个constructor】里去
	 * @param list
	 * @return
	 */
	@SuppressLint("UseSparseArrays")
	private void initList(List<Entry<String, String>> list) {
		this.list = list;
		hashMap = new HashMap<Integer, Boolean>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				hashMap.put(i, false);
			}
		}

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




	public int selectedCount = 0;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = convertView == null ? null : (ViewHolder) convertView.getTag();
		if (holder == null) {
			convertView = inflater.inflate(layoutRes, parent, false);

			holder = new ViewHolder();
			holder.ivGridItemHead = convertView.findViewById(R.id.ivGridItemHead);
			if (hasName) {
				holder.tvGridItemName = convertView.findViewById(R.id.tvGridItemName);
			}

			holder.ivGridItemCheck = convertView.findViewById(R.id.ivGridItemCheck);


			convertView.setTag(holder);
		}

		final Entry<String, String> kvb = getItem(position);
		final String name = kvb.getValue();

		Glide.with(context).load(kvb.getKey()).into(holder.ivGridItemHead);

		if (hasName) {
			holder.tvGridItemName.setVisibility(View.VISIBLE);
			holder.tvGridItemName.setText(StringUtil.getTrimedString(name));
		}

		if (hasCheck) {
			holder.ivGridItemCheck.setVisibility(View.VISIBLE);

			if(getItemChecked(position)){
				Glide.with(context).load(R.drawable.selected).into(holder.ivGridItemCheck);
			}else {
				Glide.with(context).load(R.drawable.not_selected).into(holder.ivGridItemCheck);
			}
			//降低图片亮度

			float brightness = -50; // 负值表示降低亮度，正值表示增加亮度
			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.set(new float[] {
					1, 0, 0, 0, brightness,
					0, 1, 0, 0, brightness,
					0, 0, 1, 0, brightness,
					0, 0, 0, 1, 0
			});


			ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
			holder.ivGridItemHead.setColorFilter(colorFilter);

			holder.ivGridItemCheck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setItemChecked(position, !getItemChecked(position));

					Log.i(TAG, (getItemChecked(position) ? "" : "取消") + "选择第 " + position + " 个item name=" + name);
				}
			});
		}else {
			holder.ivGridItemCheck.setVisibility(View.GONE);
			//移除亮度滤镜
			holder.ivGridItemHead.setColorFilter(null);
		}

		return convertView;
	}

	static class ViewHolder {
		public ImageView ivGridItemHead;
		public TextView tvGridItemName;
		public ImageView ivGridItemCheck;
	}


}
