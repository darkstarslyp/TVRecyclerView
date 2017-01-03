package li.demon.com.tvrecyclerview.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by demon on 16/12/12.
 */

public class ScreenUtils {

    public static int mScreenWidth = -1;
    public static int mScreenHeight = -1;

    public static int getScreenWidth(Context context){

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }


}
