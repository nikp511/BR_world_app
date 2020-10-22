package utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;


/**
 * Created by Safiya Rangwala on Jun, 01 2017 16:34.
 * <p>
 * ws.wolfsoft.propertyplanetapp.utils class for common methods
 */

public class Utils {

    private static SharedPreferences mSharedPreference;

    public static void showSnackBar(View viewLayout, String toastMessage) {
        Snackbar.make(viewLayout, toastMessage, Snackbar.LENGTH_LONG).show();
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                IBinder iBinder = activity.getCurrentFocus().getWindowToken();
                if (iBinder != null && inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(iBinder, 0);
                }
            }
        } catch (Exception exception) {
            Logger.e(activity.getClass().getSimpleName() + " hideKeyBoard", exception.toString());
        }
    }


}
