package aning.reconstruction.application;

import aning.reconstruction.manager.DataManager;
import aning.reconstruction.model.User;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.util.StringUtil;

import android.content.Context;
import android.util.Log;

import com.secneo.sdk.Helper;

public class Application extends BaseApplication {
	private static final String TAG = "Application";

	private static Application context;
	public static Application getInstance() {
		return context;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		fpvApplication.onCreate();
	}

	/**
	 * 不知道干啥的方法 大疆文档说要加
	 * @param paramContext
	 */
	private FPVApplication fpvApplication;
	@Override
	protected void attachBaseContext(Context paramContext) {
		super.attachBaseContext(paramContext);
		Helper.install(Application.this);
		if (fpvApplication == null) {
			fpvApplication = new FPVApplication();
			fpvApplication.setContext(this);
		}
	}
	
	/**获取当前用户id
	 * @return
	 */
	public long getCurrentUserId() {
		currentUser = getCurrentUser();
		Log.d(TAG, "getCurrentUserId  currentUserId = " + (currentUser == null ? "null" : currentUser.getId()));
		return currentUser == null ? 0 : currentUser.getId();
	}
	/**获取当前用户phone
	 * @return
	 */
	public String getCurrentUserPhone() {
		currentUser = getCurrentUser();
		return currentUser == null ? null : currentUser.getPhone();
	}


	private static User currentUser = null;
	public User getCurrentUser() {
		if (currentUser == null) {
			currentUser = DataManager.getInstance().getCurrentUser();
		}
		return currentUser;
	}

	public void saveCurrentUser(User user) {
		if (user == null) {
			Log.e(TAG, "saveCurrentUser  currentUser == null >> return;");
			return;
		}
		if (user.getId() <= 0 && StringUtil.isNotEmpty(user.getName(), true) == false) {
			Log.e(TAG, "saveCurrentUser  user.getId() <= 0" +
					" && StringUtil.isNotEmpty(user.getName(), true) == false >> return;");
			return;
		}

		currentUser = user;
		DataManager.getInstance().saveCurrentUser(currentUser);
	}

	public void logout() {
		currentUser = null;
		DataManager.getInstance().saveCurrentUser(currentUser);
	}
	
	/**判断是否为当前用户
	 * @param userId
	 * @return
	 */
	public boolean isCurrentUser(long userId) {
		return DataManager.getInstance().isCurrentUser(userId);
	}

	public boolean isLoggedIn() {
		return getCurrentUserId() > 0;
	}



}
