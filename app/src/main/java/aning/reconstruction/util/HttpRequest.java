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

package aning.reconstruction.util;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import aning.reconstruction.application.Application;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.manager.HttpManager;
import zuo.biao.library.model.Parameter;
import zuo.biao.library.util.MD5Util;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;

/**
 * HTTP请求工具类
 *
 * @author Lemon
 * @use 添加请求方法xxxMethod >> HttpRequest.xxxMethod(...)
 * @must 所有请求的url、请求方法(GET, POST等)、请求参数(key-value方式，必要key一定要加，没提供的key不要加，value要符合指定范围)
 * 都要符合后端给的接口文档
 */
public class HttpRequest {
    //	private static final String TAG = "HttpRequest";


    /**
     * 基础URL，这里服务器设置可切换
     */
    public static final String URL_BASE = SettingUtil.getCurrentServerAddress();
    public static final String PAGE_NUM = "pageNum";


    //示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //user<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static final String RANGE = "range";

    public static final String FILE = "file";
    public static final String USER_ID = "user_id";
    public static final String CURRENT_USER_ID = "currentUserId";

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String CAPTCHA = "captcha";
    public static final String TRAIN_STEPS = "n_steps";
    public static final String VIDEO_NAME = "video_name";
    public static final String FILENAME = "filename";
    public static final String VIDEO_NAME_List = "video_name_list";

    //account<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    /**
     * 注册
     *
     * @param account
     * @param password
     * @param listener
     */
    public static void register(final String account, final String password,
                                final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(ACCOUNT, account);
        request.put(PASSWORD, MD5Util.MD5(password));

        HttpManager.getInstance().post(request, URL_BASE + "/user/register",true, requestCode, listener);
    }

    /**
     * 登陆
     *
     * @param account
     * @param password
     * @param listener
     */
    public static void login(final String account, final String password,
                             final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(ACCOUNT, account);
        request.put(PASSWORD, MD5Util.MD5(password));

        HttpManager.getInstance().post(request, URL_BASE + "/user/login",true, requestCode, listener);
    }

    /**
     * 发送验证码
     * @param account
     * @param requestCode
     * @param listener
     */
    public static void sendCode(final String account, final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put("account", account);
        HttpManager.getInstance().post(request, URL_BASE + "/user/captcha",true, requestCode, listener);
    }

    /**
     * 修改密码
     * @param account
     * @param newPassword
     * @param code
     * @param requestCode
     * @param listener
     */
    public static void forgetPassword(final String account, final String newPassword, final String code,
                                      final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(ACCOUNT, account);
        request.put(NEW_PASSWORD, MD5Util.MD5(newPassword));
        request.put(CAPTCHA, code);

        HttpManager.getInstance().post(request, URL_BASE + "/user/forgetPassword", true,requestCode, listener);
    }

    //account>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //camera<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    public static void uploadVideo(File videoFile, final int trainSteps ,final String videoName, final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(USER_ID, Application.getInstance().getCurrentUserId());
        request.put(TRAIN_STEPS, trainSteps);
        request.put(VIDEO_NAME,videoName);
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put(FILE,videoFile);
        HttpManager.getInstance().post(request,fileMap ,URL_BASE + "/camera/file", requestCode, listener);
    }


    /**
     * 获取某用户的渲染模型列表
     * @param requestCode
     * @param listener
     */
    public static void getModelList(final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(USER_ID, Application.getInstance().getCurrentUserId());

        HttpManager.getInstance().post(request, URL_BASE + "/camera/getModelList", true,requestCode, listener);
    }


    /**
     * 批量删除模型
     * @param nameList
     * @param requestCode
     * @param listener
     */
    public static void deleteModel(List<String> nameList, final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(USER_ID, Application.getInstance().getCurrentUserId());
        request.put(VIDEO_NAME_List, nameList);

        HttpManager.getInstance().post(request, URL_BASE + "/camera/delete", true,requestCode, listener);
    }


    public static void render(Boolean origin, String videoName,List<Object> translation, List<Integer> rotationList, final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        String name = Application.getInstance().getCurrentUserId() + "_" + videoName;
        request.put(FILENAME, name);
        request.put("origin", origin);
        request.put("translation", translation);
        request.put("rotation", rotationList);

        HttpManager.getInstance().post(request, URL_BASE + "/render/photo", true,requestCode, listener);
    }

    public static final int USER_LIST_RANGE_ALL = 0;
    public static final int USER_LIST_RANGE_RECOMMEND = 1;

    /**
     * 获取用户列表
     *
     * @param range
     * @param pageNum
     * @param requestCode
     * @param listener
     */
    public static void getUserList(int range, int pageNum, final int requestCode, final OnHttpResponseListener listener) {
        Map<String, Object> request = new HashMap<>();
        request.put(CURRENT_USER_ID, Application.getInstance().getCurrentUserId());
        request.put(RANGE, range);
        request.put(PAGE_NUM, pageNum);

        HttpManager.getInstance().get(request, URL_BASE + "/user/list", requestCode, listener);
    }


    //user>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    /**
     * 添加请求参数，value为空时不添加，最快在 19.0 删除
     *
     * @param list
     * @param key
     * @param value
     */
    @Deprecated
    public static void addExistParameter(List<Parameter> list, String key, Object value) {
        if (list == null) {
            list = new ArrayList<Parameter>();
        }
        if (StringUtil.isNotEmpty(key, true) && StringUtil.isNotEmpty(value, true)) {
            list.add(new Parameter(key, value));
        }
    }




}