package cn.hchstudio.kpermissions

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import java.util.*

/**
 * Created by ChanghuiN on 17-7-30.
 */
class RequestPermission(var activity: Activity) {

    private val TAG = "RequestPermission"
    private val PERMISSIONS_REQUEST_CODE = 42

    private var permissions: MutableMap<String, Permission> = mutableMapOf()
    private var unPermissions: MutableList<String> = mutableListOf()

    lateinit var onRequestResult: Unit

    fun requestPermission(perArr: Array<String>,
                          onRequestResult: (isAllow: Boolean) -> Unit,
                          onRequestPermissions: (permission: Permission) -> Unit = {}) {
        perArr.forEach { permissions.put(it, Permission(it, false, false)) }

        /* check permission */
        checkPermission(onRequestPermissions)

        /* shouldShowRequestPermissionRationale */
        shouldShowRequestPermissionRationale(onRequestPermissions)

        /* requestPermissions */
        if (unPermissions.size > 0)
            ActivityCompat.requestPermissions(activity, unPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        else
            onRequestResult(true)
        this.onRequestResult = onRequestResult()
    }

    /**
     * 回调调用此函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size > 0) {
            grantResults.forEach {
                log("onRequestPermissionsResult---" + Arrays.toString(permissions) + "---" + Arrays.toString(grantResults))
                if (it != PackageManager.PERMISSION_GRANTED) {
                    permissionsResult.onRequestPermissionsResult(false)
                    return
                }
            }
        }
    }

    private fun checkPermission(onRequestPermissions: (permission: Permission) -> Unit = {}) {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(activity, it.key) != PackageManager.PERMISSION_GRANTED) {
                unPermissions.add(it.key)
            } else {
                log("checkPermission---HavePermission---${it.key}")
                it.value.granted = true
                onRequestPermissions(it.value)
            }
        }
    }

    private fun shouldShowRequestPermissionRationale(onRequestPermissions: (permission: Permission) -> Unit = {}) {
        unPermissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                log("shouldShowRequestPermissionRationale---$it")
                permissions.get(it)?.shouldShowRequestPermission = true
                onRequestPermissions(permissions.get(it)!!)
            }
        }
    }

    private fun log(msg: String) {
        Log.i(TAG, msg)
    }

}