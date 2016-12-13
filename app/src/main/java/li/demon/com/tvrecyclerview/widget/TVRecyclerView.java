package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by demon on 16/12/12.
 */

public class TVRecyclerView extends RecyclerView {

    public TVRecyclerView(Context context) {
        super(context);
    }

    public TVRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TVRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public View focusSearch(View focused, int direction) {
        return super.focusSearch(focused, direction);
    }
}
