package li.demon.com.tvrecyclerview.utils;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by demon on 16/12/27.
 */

public class ScaleUitl {

    public static void scaleViewWidthAnim(View view,float scale,int duration){
        if(view==null) return;
        ViewCompat.animate(view).scaleX(scale).setDuration(duration).scaleY(scale).start();
    }

    public static void scaleViewWidthAnim(View view,float scale){
        if(view==null) return;
        scaleViewWidthAnim(view,scale,500);
    }

    public static void scaleView(View view,float scale){
        if(view==null) return;
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
