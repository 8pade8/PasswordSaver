package net.a8pade8.passwordsaver.uilib;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by stanislav on 07.12.16.
 */

public class Messages {

    public static void MiddleToastShort(Context c, String message) {
        Toast t = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, -10, 0);
        t.show();
    }

    public static void MiddleToastLong(Context c, String message) {
        Toast t = Toast.makeText(c, message, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, -10, 0);
        t.show();
    }
}



