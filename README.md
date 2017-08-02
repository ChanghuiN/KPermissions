# KPermissions

This library allows the usage of kotlin with the new Android M permission model.

## Setup

To use this library your `minSdkVersion` must be >= 14.

In your build.gradle :

```gradle
repositories {
    jcenter() // If not already there
}

dependencies {
    compile 'compile 'cn.hchstudio:kpermissions:1.0.6''
}
```

## Usage

Create a `KPermissions` instance :

```kotlin
var requsetPermission: RequestPermission = RequestPermission(this); // where this is an Activity instance
```

Example : request the CAMERA permission (with Retrolambda for brevity, but not required)

```kotlin
requsetPermission.requestPermission(arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE), {
    Log.i(TAG, "isAllow---$it")
}, {
    Log.i(TAG, "permission---$it")
})
```

You can need add callback in 'onRequestPermissionsResult' :
```kotlin
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    requsetPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
```

It can been use in Java
Look at the `sample` app for more.


