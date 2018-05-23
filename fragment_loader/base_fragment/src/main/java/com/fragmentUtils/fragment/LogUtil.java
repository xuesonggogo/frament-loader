package com.fragmentUtils.fragment;

import android.util.Log;


/**
 * 输出日志
 * Created by lixuesong on 18/2/15.
 */
public final class LogUtil {

    private static final String TAG = "LogUtil";
    private static boolean LOG_DEBUG = true;

    private LogUtil() {

    }

    public static void setDebug(boolean tag) {
        LOG_DEBUG = tag;
    }

    public static int v(String msg) {
        return v(null, msg);
    }

    public static int v(String tag, String msg) {
        return println(Log.VERBOSE, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return println(Log.VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int d(String msg) {
        return d(null, msg);
    }

    public static int d(String tag, String msg) {
        return println(Log.DEBUG, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return println(Log.DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int d(String tag, String format, Object... args) {
        return println(Log.DEBUG, tag, String.format(format, args));
    }

    public static int i(String msg) {
        return i(null, msg);
    }

    public static int i(String tag, String msg) {
        return println(Log.INFO, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return println(Log.INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(String msg) {
        return w(null, msg);
    }

    public static int w(String tag, String msg) {
        return println(Log.WARN, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(Log.WARN, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int w(Throwable tr) {
        return w(null, getStackTraceString(tr));
    }

    public static int w(String tag, Throwable tr) {
        return println(Log.WARN, tag, getStackTraceString(tr));
    }

    public static int e(String msg) {
        return e(null, msg);
    }

    public static int e(String tag, String msg) {
        return println(Log.ERROR, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(Log.ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }

    public static int e(String tag, Throwable tr) {
        return println(Log.ERROR, tag, getStackTraceString(tr));
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    private static int println(int priority, String tag, String msg) {
        if (!LOG_DEBUG && priority < Log.ERROR) {    // release环境，只显示error级别的log
            return 0;
        }
        try {
            StackTraceElement e = (new Throwable()).getStackTrace()[2];
            String fileName = e.getFileName();
            String methodName = e.getMethodName();
            int lineNumber = e.getLineNumber();
            if (fileName != null && fileName.contains(".java")) {
                fileName = fileName.replace(".java", "");
            }
            tag = tag == null ? TAG : String.format("%s_%s", TAG, tag);
            msg = String.format("[%s.%s(): %d] %s", fileName, methodName, lineNumber, msg);
            return Log.println(priority, tag, msg);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }
}
