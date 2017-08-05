package cn.hchstudio.kpermissions

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log


/**
 * Created by ChanghuiN on 17-7-30.
 *
 * Usage:
 * 1. private var kPermission: KPermission = KPermission(this)
 * 2. kPermission.requestPermission(arrayOf(Manifest.permission.CAMERA,
 *       Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
 *       Manifest.permission.READ_PHONE_STATE), {
 *       Log.i(TAG, "isAllow---$it")
 *       }, {
 *       Log.i(TAG, "permission---$it")
 *       })
 * 3. in onRequestPermissionsResult
 *    kPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
 *
 */
class KPermission(var activity: Activity) {

    private val TAG = "KPermission"
    private val PERMISSIONS_REQUEST_CODE = 42

    private var permissions: MutableMap<String, Permission> = mutableMapOf()
    private var unPermissions: MutableList<String> = mutableListOf()

    private lateinit var onRequestResultCallback: (Boolean)->Unit
    private lateinit var onRequestPermissionsCallback: (Permission) -> Unit

    /**
     * 权限请求
     * @param perArr 权限数组
     * @param onRequestResult 请求结果
     * @param onRequestPermissions 每个权限请求结果，可省略
     */
    fun requestPermission(perArr: Array<String>,
                          onRequestResult: (isAllow: Boolean) -> Unit,
                          onRequestPermissions: (permission: Permission) -> Unit = {}) {
        /* To judge whether it's empty and SDK_VERSION */
//        if (perArr.size <= 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            onRequestResult(true)
//            return
//        }

        onRequestResultCallback = onRequestResult
        onRequestPermissionsCallback = onRequestPermissions

        perArr.forEach { permissions.put(it, Permission(it, false, false)) }

        /* check permission */
        checkPermission()

        /* shouldShowRequestPermissionRationale */
        shouldShowRequestPermissionRationale()

        /* requestPermissions */
        if (unPermissions.size > 0)
            ActivityCompat.requestPermissions(activity, unPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        else
            onRequestResultCallback(true)
    }

    /**
     * 回调调用此函数
     * @param requestCode 请求码
     * @param pers 权限数组
     * @param grantResults 请求权限结果
     */
    fun onRequestPermissionsResult(requestCode: Int, pers: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size > 0) {

            var result = true
            for (index in grantResults.indices) {
                log("onRequestPermissionsResult---" + pers.get(index) + "---" + grantResults.get(index))
                var permission = permissions.get(pers.get(index))

                permission?.let {
                    permission.granted = grantResults.get(index) == PackageManager.PERMISSION_GRANTED
                    onRequestPermissionsCallback(it)
                    if (!permission.granted) result = false
                }
            }
            onRequestResultCallback(result)
        }
    }

    /**
     * 跳转到权限设置界面
     */
    fun startPermissionSetting() {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null))

        try {
            activity.startActivity(intent)
        } catch(exception: Exception) {
            log(exception.toString())
        }
    }

    private fun checkPermission() {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(activity, it.key) != PackageManager.PERMISSION_GRANTED) {
                unPermissions.add(it.key)
            } else {
                log("checkPermission---HavePermission---${it.key}")
                it.value.granted = true
                onRequestPermissionsCallback(it.value)
            }
        }
    }

    private fun shouldShowRequestPermissionRationale() {
        unPermissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                log("shouldShowRequestPermissionRationale---$it")
                permissions.get(it)?.shouldShowRequestPermission = true
                onRequestPermissionsCallback(permissions.get(it)!!)
            }
        }
    }

    var isDebug = BuildConfig.DEBUG

    private fun log(msg: String) {
        if (isDebug)
            Log.i(TAG, msg)
    }

}