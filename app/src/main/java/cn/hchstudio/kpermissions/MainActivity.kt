package cn.hchstudio.kpermissions

import android.annotation.TargetApi
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var requsetPermission: RequestPermission = RequestPermission(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requsetPermission.requestPermission(arrayOf("qw"), {}, {})
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun a(){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requsetPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
