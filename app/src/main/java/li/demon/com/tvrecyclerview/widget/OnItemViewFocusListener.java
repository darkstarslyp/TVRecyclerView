package li.demon.com.tvrecyclerview.widget;

import android.view.View;

/**
 * Created by demon on 16/12/27.
 */

public interface OnItemViewFocusListener {
     void onListItemFocused(View view, int position,float scale);
     void onListItemUnFocused(View view, int position,float scale);
}