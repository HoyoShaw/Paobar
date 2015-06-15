
package com.hoyo.paobar.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author novia
 */
public class Utils {
    private static final boolean DEBUG = true;

    private static final String TAG = "SysOptUtils";

    private static final float NUMBER_SIZE_K = 1024f;

    private static final float NUMBER_SIZE_M = 1048576f;

    private static final float NUMBER_SIZE_G = 1073741824f;




    /**
     * 判断多个SD卡存在的情况
     */
    public static ArrayList<String> getInternalAndExternalSDPath(Context context) {
        ArrayList<String> pathList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                Object storageManager = context.getSystemService("storage");
                Class<?> classStorageManager = Class.forName("android.os.storage.StorageManager");
                Method method_getVolumePaths = classStorageManager.getMethod("getVolumePaths", new Class[0]);
                String[] externalStoragePaths = (String[]) method_getVolumePaths.invoke(storageManager, new Object[0]);
                for (String string : externalStoragePaths) {
                    pathList.add(string);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                String defaultPath = Environment.getExternalStorageDirectory().getPath();
                if (!TextUtils.isEmpty(defaultPath)) {
                    pathList.add(defaultPath);
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/proc/mounts"))));
                String path;
                while ((path = br.readLine()) != null) {
                    if (path.contains("uid=1000") && path.contains("gid=1015") && path.contains("asec") == false) {
                        String[] devideStrings = path.split(" ");
                        if (devideStrings.length >= 4) {
                            String sdpath = devideStrings[1];
                            if (!pathList.contains(sdpath)) {
                                pathList.add(sdpath);
                            }
                        }
                    }
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pathList;
    }

    /**
     * zhong：最好不要用此函数取cpu数量，高仿机会出错
     */
    public static int getCopuCoreNumber() {
        File cpuDir = new File("/sys/devices/system/cpu");
        String[] fileNameList = cpuDir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                if (filename.matches("cpu[0-9]")) {
                    return true;
                }
                return false;
            }
        });

        int cpuCore = 0;
        for (String str : fileNameList) {
            if (DEBUG) {
                Log.d(TAG, "cpu core: " + str);
            }
            cpuCore++;
        }

        return cpuCore > 0 ? cpuCore : 1;
    }

    /** 判断软件是否安装 */
    public static boolean isPkgInstalled(Context c, String pkgName) {
        PackageManager mPm = c.getPackageManager();
        if (mPm == null) {
            return false;
        }
        PackageInfo pkginfo = null;
        try {
            pkginfo = mPm.getPackageInfo(pkgName, 0);
        } catch (NameNotFoundException e) {
        }
        return pkginfo == null ? false : true;
    }

    /**
     * 获取指定路径空间总数 byte
     */
    public static long getPathTotalSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取指定路径剩余空间数 byte
     */
    public static long getPathFreeSize(String path) {
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机默认存储卡空间总数 byte
     */
    public static long getExternalStorageTotalSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory();
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                return totalBlocks * blockSize;
            } catch (Exception e) {
                if (DEBUG) {
                    Log.d(TAG, "", e);
                }
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    /**
     * 获取手机默认存储卡空间剩余数 byte
     */
    public static long getExternalStorageFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    public static long getRootStorageTotalSize() {
        File path = Environment.getRootDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long Blocks = stat.getBlockCount();
        return Blocks * blockSize;
    }

    /**
     * 获取用户可用手机内部空间总数 byte
     */
    public static long getUserStorageTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取用户可用手机内部空间剩余数 byte
     */
    public static long getUserStorageFreeSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /** 整数位最多现实3位，超过3位的升级单位，小数位最多显示1位 */
    public static String formatMemorySize(long bytes) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);
        if (bytes < 1000L) {
            // B
            return bytes + "B";
        } else if (bytes < 1024000) {
            // K
            return numberFormat.format(bytes / 1024F) + "K";
        } else if (bytes < 1048576000) {
            // M
            return numberFormat.format(bytes / 1048576F) + "M";
        } else {
            // G
            return numberFormat.format(bytes / 1073741824F) + "G";
        }
    }

    /**
     * return a string in human readable format
     *
     * @param bytes 单位 B
     */
    public static String getHumanReadableSizeSimple(long bytes) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);

        if (bytes == 0) {
            return "0M";
        } else if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < 1048576) {
            return numberFormat.format(bytes / 1024f) + "K";
        } else if (bytes < 1048576 * 1024) {
            return numberFormat.format(bytes / 1024f / 1024f) + "M";
        } else {
            return numberFormat.format(bytes / 1024f / 1024f / 1024f) + "G";
        }
    }

    /**
     * SD卡是否是虚拟出来的（软件搬家不支持虚拟的SD卡）
     */
    public static boolean isExternalStorageEmulated() {
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                Class<?> classEnvironment = Class.forName("android.os.Environment");
                Method method_isExternalStorageEmulated = classEnvironment.getMethod("isExternalStorageEmulated", new Class[0]);
                Boolean isExternalStorageEmulated = (Boolean) method_isExternalStorageEmulated.invoke(classEnvironment.getClass(), new Object[0]);
                if (isExternalStorageEmulated) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**
     * 字符串转换成int
     *
     * @param str
     * @return
     */
    public static int str2Int(String str, int defValue) {
        int ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Integer.parseInt(str.trim());
            }
        } catch (Exception ex) {
        }
        return ret;
    }

    /**
     * 字符串转换成long
     *
     * @param str
     * @return
     */
    public static long str2Long(String str, long defValue) {
        long ret = defValue;
        try {
            if (!TextUtils.isEmpty(str)) {
                ret = Long.parseLong(str.trim());
            }
        } catch (Exception ex) {
        }
        return ret;
    }



    /** 显示 Android 系统的网络设置 */
    public static void showNetworkSettings(final Context c) {
        try {
            c.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)); // 进入无线网络配置界面
        } catch (Exception e1) {
            try {
                c.startActivity(new Intent(Settings.ACTION_SETTINGS)); // Bug
                                                                       // #74632:
                                                                       // O1不支持上述配置界面
            } catch (Exception e2) {
            }
        }
    }

    public static boolean isDataConnected(Context c) {
        if (isWifiConnected(c) || isPhoneDataConnected(c)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * add for check wifi net connection desc: wifi 开关打开，但是wifi没有连上的话仍然是false
     * 只有wifi正常网络已经建立,正常获取IP后才返回true
     */
    public static boolean isWifiConnected(Context c) {
        // 原有方法在android4.0上返回值总是为true，修改为以下方式
        boolean isWifiConnected = false;

        try {
            ConnectivityManager connecManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connecManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mNetworkInfo != null) {
                isWifiConnected = mNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            isWifiConnected = false;
        }
        return isWifiConnected;
    }

    /**
     * add for check phone net connection,such as APN desc: 移动网络数据通信
     */
    private static boolean isPhoneDataConnected(Context c) {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
            int iDataState = -1;
            iDataState = mTelephonyMgr.getDataState();
            if (iDataState == TelephonyManager.DATA_DISCONNECTED) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] getFormatSizeSource(long size) {
        String[] result = new String[2];
        NumberFormat sizeFormat = NumberFormat.getInstance();
        sizeFormat.setMaximumFractionDigits(0);
        sizeFormat.setMinimumFractionDigits(0);
        if (size >= 1000 * NUMBER_SIZE_M) {
            result[1] = "GB";
            if (size > 999 * NUMBER_SIZE_G) {
                // 当值大于999G时，显示999G(不存在的情况)
                result[0] = "999";
            } else if (size >= 100 * NUMBER_SIZE_G) {
                // 876G
                sizeFormat.setMaximumFractionDigits(0);
                sizeFormat.setMinimumFractionDigits(0);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_G);
            } else if (size >= 10 * NUMBER_SIZE_G) {
                // 46.9G
                sizeFormat.setMaximumFractionDigits(1);
                sizeFormat.setMinimumFractionDigits(1);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_G);
            } else {
                // 8.78G
                sizeFormat.setMaximumFractionDigits(2);
                sizeFormat.setMinimumFractionDigits(2);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_G);
            }
        } else if (size >= 1000 * NUMBER_SIZE_K) {
            result[1] = "MB";
            if (size >= 100 * NUMBER_SIZE_M) {
                // 123M
                sizeFormat.setMaximumFractionDigits(0);
                sizeFormat.setMinimumFractionDigits(0);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_M);
            } else if (size >= 10 * NUMBER_SIZE_M) {
                // 34.8M
                sizeFormat.setMaximumFractionDigits(1);
                sizeFormat.setMinimumFractionDigits(1);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_M);
            } else {
                // 7.98M
                sizeFormat.setMaximumFractionDigits(2);
                sizeFormat.setMinimumFractionDigits(2);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_M);
            }
        } else if (size >= 1000) {
            result[1] = "KB";
            if (size >= 100 * NUMBER_SIZE_K) {
                // 798K
                sizeFormat.setMaximumFractionDigits(0);
                sizeFormat.setMinimumFractionDigits(0);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_K);
            } else if (size >= 10 * NUMBER_SIZE_K) {
                // 49.3K
                sizeFormat.setMaximumFractionDigits(1);
                sizeFormat.setMinimumFractionDigits(1);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_K);
            } else {
                // 9.99k
                sizeFormat.setMaximumFractionDigits(2);
                sizeFormat.setMinimumFractionDigits(2);
                result[0] = sizeFormat.format(size / NUMBER_SIZE_K);
            }
        } else {
            if (size < 0) {
                size = 0;
            }
            result[1] = "B";
            result[0] = String.valueOf(size);
        }
        return result;
    }

    public static String getFormatSize(long size) {
        String[] s = getFormatSizeSource(size);
        return s[0] + s[1];
    }

    /** 安全地显示dialog */
    public static void showDialog(Dialog dialog) {
        try {
            if (dialog == null) {
                return;
            }
            Context context = dialog.getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog.show();
        } catch (Throwable t) {
            if (AppEnv.DEBUG) {
                Log.e(TAG, "dialog show error", t);
            }
        }
    }

    /** 安全地关闭dialog */
    public static void dismissDialog(Dialog dialog) {
        try {
            if (dialog == null) {
                return;
            }
            Context context = dialog.getContext();
            if (context != null && context instanceof Activity) {
                if (((Activity) context).isFinishing()) {
                    return;
                }
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Throwable t) {
            if (AppEnv.DEBUG) {
                Log.e(TAG, "dialog dismiss error", t);
            }
        }
    }

    /**
     * 通过包名启动程序
     */
    public static boolean startAnotherApp(Activity activity, String packageName) {
        boolean ret = false;
        try {
            PackageInfo packageInfo = null;
            PackageManager pm = activity.getPackageManager();

            packageInfo = pm.getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(packageInfo.packageName);

            List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(resolveIntent, 0);
            ResolveInfo resolveInfo = resolveInfoList.iterator().next();

            if (resolveInfo != null) {
                String activityPackageName = resolveInfo.activityInfo.packageName;
                String className = resolveInfo.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                ComponentName componentName = new ComponentName(activityPackageName, className);
                intent.setComponent(componentName);

                startActivity(activity, intent);
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void startActivity(Activity activity, Intent intent) {
        if (activity != null) {
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                if (AppEnv.DEBUG) {
                    throw new SecurityException("Utils.startActivity: got Exception:" + e.getMessage());
                }
            }
        }
    }

    /**
     * 创建文件夹快捷方式图标，文件夹中包含appIcon
     *
     * @param context 用于获取系统图标默认大小
     * @param bgBitmap 文件夹背景
     * @param icos 需要绘制的图标 (最多绘4个图标)
     * @return
     */
    public static Bitmap createShortIcon(Context context, Bitmap bgBitmap, List<Bitmap> icons) {

        int mPaddingLeft = 8;
        int mPaddingTop = 8;
        int mAppMargin = 4;

        if (icons.size() > 4) {
            throw new IllegalArgumentException("the icon size must less than 4!");
        }
        // 默认图标大小
        int iconSize = (int) context.getResources().getDimension(android.R.dimen.app_icon_size);
        // 创建一个空内容的bitmap，用于拷贝背景
        Bitmap blank = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
        // 初始化画布
        Canvas canvas = new Canvas(blank);
        // 拷贝背景
        Paint iconPaint = new Paint();
        iconPaint.setDither(true);// 防抖动
        iconPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
        Rect src = new Rect(0, 0, bgBitmap.getWidth(), bgBitmap.getHeight());
        Rect dst = new Rect(0, 0, iconSize, iconSize);
        canvas.drawBitmap(bgBitmap, src, dst, iconPaint);

        Bitmap icon = null;
        for (int i = 0; i < icons.size(); i++) {
            icon = icons.get(i);
            // 绘制图标
            src = new Rect(0, 0, icon.getWidth(), icon.getWidth());
            int appSize = (iconSize - mAppMargin - mPaddingLeft - mPaddingTop) / 2;
            int drawLeft = mPaddingLeft;
            int drawTop = mPaddingTop;

            switch (i) {
                case 0:
                    drawLeft = mPaddingLeft;
                    drawTop = mPaddingTop;
                    break;
                case 1:
                    drawLeft = mPaddingLeft + mAppMargin + appSize;
                    drawTop = mPaddingTop;
                    break;
                case 2:
                    drawLeft = mPaddingLeft;
                    drawTop = mPaddingTop + mAppMargin + appSize;
                    break;
                case 3:
                    drawLeft = mPaddingLeft + mAppMargin + appSize;
                    drawTop = mPaddingTop + mAppMargin + appSize;
                    break;
            }

            dst = new Rect(drawLeft, drawTop, drawLeft + appSize, drawTop + appSize);
            canvas.drawBitmap(icon, src, dst, iconPaint);
        }

        return blank;

    }


}
