package utils;


import com.movie.brworld.BuildConfig;

public class Logger {

    public static void e(String tag, String string) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(tag, string);
        }
    }

    public static void d(String tag, String string) {
        if (BuildConfig.DEBUG) {
            android.util.Log.d(tag, string);
        }
    }

    public static void v(String tag, String string) {
        if (BuildConfig.DEBUG) {
            android.util.Log.v(tag, string);
        }
    }
}
