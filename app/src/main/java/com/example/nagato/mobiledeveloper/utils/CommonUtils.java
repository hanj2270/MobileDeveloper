package com.example.nagato.mobiledeveloper.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.example.nagato.mobiledeveloper.global.MyApplacation;

import java.io.File;

/**
 * Created by nagato on 2016/９/9.
 */
public class CommonUtils {
    public static Context getContext() {
        return MyApplacation.getContext();
    }

    public static Handler getHandler() {
        return MyApplacation.getHandler();
    }

    public static int getMainThreadId() {
        return MyApplacation.getMainThreadId();
    }

    /////////界面交互//////////////////////////////////
    public static void toast(String str) {
        Toast.makeText(MyApplacation.getContext(), str, Toast.LENGTH_LONG).show();
    }

    public static void makeSnackBar(View parentView, String str, int length) {
        final Snackbar snackbar = Snackbar.make(parentView, str, length);
        snackbar.show();
    }

    public static void makeSnackBarWithAction(View parentView, String msg, int length, View.OnClickListener listener, String actionMsg) {
        final Snackbar snackbar = Snackbar.make(parentView, msg, length);
        snackbar.setAction(actionMsg, listener);
        snackbar.show();
    }
////////////组件间通讯////////////////////////////////
    public static void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplacation.getContext().startActivity(intent);
    }
/////////////////加载view/////////////////////////
    public static View getview(int id){
        return View.inflate(MyApplacation.getContext(), id, null);
    }
//////////移除搜索内容中的非法字符///////////////////
    public static String stringFilterStrict(String searchText) {
    return searchText.replaceAll("[^ a-zA-Z0-9\\u4e00-\\u9fa5]", "");
    }
///////////清除缓存/////////////////////////////////////
    private static void deleteFilesByDirectory(File directory) {
    if (directory != null && directory.exists() && directory.isDirectory())
        for (File item : directory.listFiles())
            item.delete();
    }


    public static void clearExternalCache(Context context) {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        deleteFilesByDirectory(context.getExternalCacheDir());
    }
    public static void clearInternalCache(Context context) {
        File directory = context.getCacheDir();
        deleteFilesByDirectory(directory);
    }


    public static void clearAllCache(Context context) {
        clearExternalCache(context);
        clearInternalCache(context);
    }

    ///////////////////是否为Wifi连接/////////////////////////
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplacation.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null)
            return false;
        else{
            return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }


    }

// /////////////////线程判断和UI更新//////////////////////////
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            r.run();
        } else {
            MyApplacation.getHandler().post(r);
        }
    }





    private static boolean isRunOnUIThread() {
        int myTid = android.os.Process.myTid();
        if (myTid == MyApplacation.getMainThreadId()) {
            return true;
        }
        return false;
    }




}
