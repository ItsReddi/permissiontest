package com.itrsreddi.permissiontest;

import android.Manifest;

import android.content.Intent;

import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //Permission
    private static String TAG = "MainActivityLog";

    //Permission
    private static final int PERMISSION_TEST = 102;

    private final int REQUEST_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG,"Activity Started");
        openFileChooser();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.v(TAG, "Permission is granted");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.v(TAG, "Permission is denied");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.v(TAG, "Request result");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //Is executed, again after permission is granted
    @AfterPermissionGranted(PERMISSION_TEST)
    private void permissions() {
        String[] perms;
        //Be careful if requesting camera permissions here, if user denies permanently, there seems no way to reactivate?
        if (Build.VERSION.SDK_INT >= 16) {
            perms = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
        } else {
            perms = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE };
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
            //Have Permission - do our stuff
            Log.v(TAG, "Permission is granted");
        } else {
            Log.v(TAG, "Permission is not granted, requesting it");
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Get Permissions", PERMISSION_TEST, perms);
        }
    }

    public void openFileChooser() {

        try {
            // Filesystem.
            Intent galleryIntent;
            galleryIntent = new Intent();

            galleryIntent.setType("*/*");

            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Quelle wählen");

            startActivityForResult(chooserIntent, REQUEST_PICTURE);
        }catch(final Exception e){

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult");
        permissions();
    }
}
