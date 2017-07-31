package cn.hchstudio.kpermissions

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = "RequestPermission"
    private var requsetPermission: RequestPermission = RequestPermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requsetPermission.requestPermission(arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE), {
            Log.i(TAG, "isAllow---$it")
        }, {
            Log.i(TAG, "permission---$it")
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requsetPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
