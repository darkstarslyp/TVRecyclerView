package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by demon on 16/12/30.
 */

public class TVRelativeLayout extends RelativeLayout {

    public TVRelativeLayout(Context context) {
        super(context);
    }

    public TVRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TVRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TVRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
