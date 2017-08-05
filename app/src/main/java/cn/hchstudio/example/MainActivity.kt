package cn.hchstudio.example

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.hchstudio.kpermissions.KPermission

class MainActivity : AppCompatActivity() {

    private val TAG = "KPermission"
    private var kPermission: KPermission = KPermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kPermission.requestPermission(arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE), {
            Log.i(TAG, "isAllow---$it")
        }, {
            Log.i(TAG, "permission---$it")
        })

        findViewById(R.id.button).setOnClickListener {
            kPermission.startPermissionSetting()
        }
    }

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    kPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
}
