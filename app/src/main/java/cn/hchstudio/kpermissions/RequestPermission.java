package cn.hchstudio.kpermissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;

//import cn.hchstudio.base.ExtKt;

/**
 * 1、requestPermission = new RequestPermission(this);
 *
 * 2、requestPermission.requestPermission(new String[]{ Permissions...},
 *      RequestPermission.PermissionsResult { })
 *
 * 3、重写onRequestPermissionsResult方法，并加上
 *   requestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
 *
 * Created by ChanghuiN on 2017/7/17.
 */

public class RequestPermission {

    private static final String TAG = "RequestPermission";

    private String[] permissions;       //权限数组
    private int permissionIndex = 0;    //

    private Activity activity;
    private PermissionsResult permissionsResult;

    public RequestPermission(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param permissions
     * @param permissionsResult 申请权限回调
     */
    public void requestPermission(String[] permissions, PermissionsResult permissionsResult) {
        this.permissions = permissions;
        this.permissionsResult = permissionsResult;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ExtKt.log(this, "Android版本大于22，使用动态权限申请");
            requestPermission(0);
        } else {
            Log.i(TAG, "Android版本小于等于22，直接初始化SDK");
            permissionsResult.onRequestPermissionsResult(true);
        }
    }

    /**
     * 请求单个权限方法
     * @param perIndex
     */
    private void requestPermission(int perIndex) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permissions[perIndex]) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[perIndex])) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.i(TAG, "shouldShowRequestPermissionRationale---" + perIndex + "---" + permissions[perIndex]);
            }
//            else {

                // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(activity, new String[]{permissions[perIndex]}, perIndex);
            Log.i(TAG, "NoPermissionAndroidRequset---" + perIndex + "---" + permissions[perIndex]);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//            }
        } else {
            Log.i(TAG, "HavePermission---" + perIndex + "---" + permissions[perIndex]);
            requestNext();
        }
    }

    /**
     * 请求下一个权限
     */
    private void requestNext() {
        if (++permissionIndex > permissions.length - 1){
            Log.i(TAG, "---权限请求OK");
            permissionsResult.onRequestPermissionsResult(true);
            return;
        }
        requestPermission(permissionIndex);
    }

    /**
     * 回调调用此函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            Log.i(TAG, requestCode + "---" + Arrays.toString(permissions) + "---" + Arrays.toString(grantResults));
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                permissionsResult.onRequestPermissionsResult(false);
                return;
            }
            requestNext();
        }
    }

    public interface PermissionsResult{
        void onRequestPermissionsResult(boolean isAllow);
    }
}