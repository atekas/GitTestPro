package com.sensu.android.zimaogou.utils;

import android.util.Log;
import com.sensu.android.zimaogou.BaseApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by winter on 2015/9/22.
 *
 * @author winter
 */
public class LogUtils {
    private static final String PREFIX = "fileCahce";
    private static boolean mLogEnable = true;

    public static void setIsLogEnable(boolean isLogEnable) {
        mLogEnable = isLogEnable;
    }

    public static boolean enable() {
        return mLogEnable;
    }

    public static void v(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String message, String message2) {
        v(tag, message);
        writeLog(message2);
    }

    public static void d(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String message, String message2) {
        d(tag, message);
        writeLog(message2);
    }

    public static void i(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String message, String message2) {
        i(tag, message);
        writeLog(message2);
    }

    public static void w(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String message, String message2) {
        w(tag, message);
        writeLog(message2);
    }

    public static void e(String tag, String message) {
        if (mLogEnable) {
            String msg = PREFIX + message;
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String message, String message2) {
        e(tag, message);
        writeLog(message2);
    }

    /**Log文件名称*/
    public static final String REPORT_ABSOLUTE_PATH = BaseApplication.getBaseApplication().getBaseExternalCacheDir()
            + "general_log.txt";

    private static boolean mIsWriteDisk;

    public static void setIsWriteDisk(boolean isWriteDisk) {
        mIsWriteDisk = isWriteDisk;
    }

    public static void writeLog(String message) {
        if (mIsWriteDisk) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(REPORT_ABSOLUTE_PATH, true);
                fileWriter.write("\n===============  start  ================\n");
                fileWriter.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                fileWriter.write(":\n");
                fileWriter.write(message);
                fileWriter.write("\n===============  end  ================\n");

                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
