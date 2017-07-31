package cn.hchstudio.kpermissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

/**
 * Created by ChanghuiN on 17-7-30.
 *
 */
class RequestPermission(var activity: Activity) {

    private val TAG = "RequestPermission"
    private val PERMISSIONS_REQUEST_CODE = 42

    private var permissions: MutableMap<String, Permission> = mutableMapOf()
    private var unPermissions: MutableList<String> = mutableListOf()

    private lateinit var onRequestResultCallback: (Boolean)->Unit
    private lateinit var onRequestPermissionsCallback: (Permission) -> Unit

    fun requestPermission(perArr: Array<String>,
                          onRequestResult: (isAllow: Boolean) -> Unit,
                          onRequestPermissions: (permission: Permission) -> Unit = {}) {
        /* To judge whether it's empty and SDK_VERSION */
        if (perArr.size <= 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onRequestResult(true)
            return
        }

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
     * @param requestCode
     * @param pers
     * @param grantResults
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

    private fun log(msg: String) {
        Log.i(TAG, msg)
    }

}