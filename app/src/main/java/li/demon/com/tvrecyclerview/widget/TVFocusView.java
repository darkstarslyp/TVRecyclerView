package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by demon on 17/1/3.
 */

public class TVFocusView extends View{



    static float edgeL = 0;
    static float edgeT;
    static float edgeR;
    static float edgeB;


    // 当前获取焦点的 view.
    WeakReference<View> targetView;

    float scale = -1;
    boolean isFocus = false;

    public float lastX;
    public float lastY;
    public int lastW;
    public int lastH;


    public TVFocusView(Context context) {
        super(context);
        init();
    }

    public TVFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        init();
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
        init();
    }



    void init() {

        /**
         * 基于 图片信息，自动计算
         */
        if (edgeL == 0) {
            Drawable drawable = getBackground();
            if(drawable==null)  return;
            Rect rect = new Rect();
            drawable.getPadding(rect);
            edgeL = rect.left;
            edgeT = rect.top;
            edgeR = rect.right;
            edgeB = rect.bottom;
        }

    }

    // 更新 指定 TVFocusView.
    public void updateView(View view, boolean isFocus, float scale) {
        doUpdateView(view, isFocus, scale);
    }


    public void doUpdateView(View view, boolean isFocus, float scale) {

        if (targetView == null || targetView.get() != view) {
            targetView = new WeakReference<>(view);
        }
        if (isFocus) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.INVISIBLE);
        }

        this.isFocus = isFocus;

        if (isFocus) {

            if (scale == -1) {
                scale = this.scale;
            } else {
                this.scale = scale;
            }

            int[] location = new int[2];
            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标

            int width = view.getWidth();
            int height = view.getHeight();

            float x = location[0] -edgeL-width*(scale-1)/2;
            float y = location[1] -edgeT-height*(scale-1)/2;
            moveTo(x, y, (int) (edgeL+width * scale + edgeR), (int) (edgeT+height * scale + edgeB+2));
        }


    }


    void moveTo(float x, float y, int width, int height) {

        if (lastX != x || lastY != y) {

            setX(x);
            setY(y);

            lastX = x;
            lastY = y;
        }

        if (width != lastW || height != lastH) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = width;
            params.height = height;
            setLayoutParams(params);
            lastW = width;
            lastH = height;

        }
    }
}
