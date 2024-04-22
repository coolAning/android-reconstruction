package aning.reconstruction.fragment;

import static zuo.biao.library.interfaces.FragmentPresenter.RESULT_OK;
import static zuo.biao.library.util.CommonUtil.showShortToast;
import static zuo.biao.library.util.CommonUtil.toActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import aning.reconstruction.R;
import aning.reconstruction.activity.ConnectionActivity;
import aning.reconstruction.activity.UploadActivity;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = "BottomSheet";
    public static final String INTENT_USER_ID = "INTENT_USER_ID";
    public static final String ARGUMENT_USER_NAME = "ARGUMENT_USER_NAME";

    /**创建一个Fragment实例
     * @param userId
     * @return
     */
    public static BottomSheetFragment createInstance(long userId) {
        return createInstance(userId, null);
    }
    /**创建一个Fragment实例
     * @param userId
     * @param userName
     * @return
     */
    public static BottomSheetFragment createInstance(long userId, String userName) {
        BottomSheetFragment fragment = new BottomSheetFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(INTENT_USER_ID, userId);
        bundle.putString(ARGUMENT_USER_NAME, userName);

        fragment.setArguments(bundle);
        return fragment;
    }


    private BottomSheetBehavior<FrameLayout> bottomSheetBehavior;
    private Button btnFile;
    private Button btnConnectDrone;
    private long userId = 0;
    private String userName = null;
    private Bundle argument = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
        argument = getArguments();
        if (argument != null) {
            userId = argument.getLong(INTENT_USER_ID, userId);
            userName = argument.getString(ARGUMENT_USER_NAME, userName);
        }
        // 绑定组件
        btnFile = view.findViewById(R.id.btn_file);
        btnConnectDrone = view.findViewById(R.id.btn_connectdrone);
		bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_container));
		bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // 绑定事件
        btnFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE);
        });

        btnConnectDrone.setOnClickListener(v -> {
            Intent intent = ConnectionActivity.createIntent(getActivity(), userId);
            startActivity(intent);
        });

        return view;
    }
    private static final int REQUEST_CODE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri == null) {
                // 未选择文件
            } else {
                String mimeType = getActivity().getContentResolver().getType(selectedFileUri);
                String fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

                if (fileExtension != null && (fileExtension.equalsIgnoreCase("mp4") )) {
                    // 文件是视频
                    Intent intent = UploadActivity.createIntent(getActivity(), userId, selectedFileUri);
                    startActivity(intent);
                } else {
                    // 文件不是视频
                }
            }
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        // 处理取消事件
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // 处理关闭事件
    }
}
