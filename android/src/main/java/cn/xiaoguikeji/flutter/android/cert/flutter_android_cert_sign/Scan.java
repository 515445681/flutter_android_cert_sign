package cn.xiaoguikeji.flutter.android.cert.flutter_android_cert_sign;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.flutter.app.FlutterApplication;

import static android.util.Log.println;

public class Scan {

    private static boolean findHookAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
                return true;
            }
            if (applicationInfo.packageName.equals("com.saurik.substrate")) {
                return true;
            }
        }
        return false;
    }

    private static boolean findHookAppFile() {
        try {
            Set<String> libraries = new HashSet<String>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.endsWith(".so") || line.endsWith(".jar") || line.contains("mt")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            reader.close();
            for (String library : libraries) {
                if (library.contains("com.saurik.substrate")) {
                    return true;
                }
                if (library.contains("XposedBridge.jar")) {
                    return true;
                }
                if (library.contains("hook")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static boolean findHookStack() {
        try {
            throw new Exception("findhook");
        } catch (Exception e) {

            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        return true;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2")
                        && stackTraceElement.getMethodName().equals("invoked")) {
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("main")) {
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isHook(Context context) {
        if(context.getApplicationContext().getClass()!= FlutterApplication.class){
            return true;
        }
        if (findHookAppName(context) || findHookAppFile() || findHookStack()) {
            return true;
        }
        return false;
    }

    //获取当前内存中的so
    public static Set<String> findLibList() {
        try {
            Set<String> libraries = new HashSet<String>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            reader.close();
            return libraries;
        } catch (Exception e) {
        }
        return new HashSet<>();
    }

}
