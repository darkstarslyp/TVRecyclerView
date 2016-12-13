package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by demon on 16/12/13.
 */

public class TVGridLayoutManager extends GridLayoutManager {


    private int DEFAULT_MIN_SPACE = 30;
    private float DEFAULT_SHOW_NEXT_ITEM_SCALE = 0.2f;

    private RecyclerView mRecyclerView;
    private boolean bShowFocusViewNextItem;


    private float mShowNextItemScale = DEFAULT_SHOW_NEXT_ITEM_SCALE;

    public TVGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TVGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public TVGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    public void setTVRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }


    public void showFocusViewNextItem(boolean showFocusViewNextItem) {
        bShowFocusViewNextItem = showFocusViewNextItem;
    }

    public void setShowNextItemScale(float scale){
          if(scale>1){
              scale = 1;
          }
        if(scale<0){
            scale = DEFAULT_SHOW_NEXT_ITEM_SCALE;
        }
        this.mShowNextItemScale = scale;
    }





    /**
     * 重新实现焦点移动时 Child view的移动
     *
     * @param parent
     * @param child
     * @param rect
     * @param immediate
     * @return
     */
    @SuppressWarnings("ResourceType")
    @Override
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View child, Rect rect, boolean immediate) {

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();
        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
        final int childTop = child.getTop() + rect.top - child.getScrollY();
        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft);
        final int offScreenTop = Math.min(0, childTop - parentTop);
        final int offScreenRight = Math.max(0, childRight - parentRight);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom);

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        int dx;
        if (getLayoutDirection() == ViewCompat.LAYOUT_DIRECTION_RTL) {
            dx = offScreenRight != 0 ? offScreenRight
                    : Math.max(offScreenLeft, childRight - parentRight);
        } else {
            dx = offScreenLeft != 0 ? offScreenLeft
                    : Math.min(childLeft - parentLeft, offScreenRight);
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        int dy = offScreenTop != 0 ? offScreenTop
                : Math.min(childTop - parentTop, offScreenBottom);

        ////方案一：保证该算法实现获得焦点的View 的margin值生效，同时可以显示出下一个child view 的部分空间,利用系统寻找焦点的方法
        if (mRecyclerView != null && (dy != 0 || dx != 0)) {
            View focusView = mRecyclerView.findFocus();
            if (focusView != null) {
                View itemView = findContainingItemView(focusView);
                if (itemView != null) {

                    int offsetWidthNextItemView = 0;
                    RecyclerView.ViewHolder viewHolder = mRecyclerView.findContainingViewHolder(itemView);

                    if (bShowFocusViewNextItem && viewHolder != null) { //是否将焦点以后的某个child view显示一部分

                        int focusPosition = mRecyclerView.getChildAdapterPosition(itemView);

                        int nextVerticalItemPos = RecyclerView.NO_POSITION;
                        if (RecyclerView.NO_POSITION != focusPosition) {
                            if (focusPosition > 0 && focusPosition < (getItemCount() - 1)) {

                                RecyclerView.LayoutParams itemParams = null;
                                int margin = 0;
                                int itemSpace = 0;

                                if (dy != 0) {
                                    boolean toBottom = true;
                                    View nextVerticalFocusView ;
                                    View nextVerticalItemView = null;

                                    if (dy > 0) {  //to bottom
                                        nextVerticalFocusView = mRecyclerView.focusSearch(focusView,View.FOCUS_DOWN);
                                    } else {   //to up
                                        nextVerticalFocusView = mRecyclerView.focusSearch(focusView,View.FOCUS_UP);
                                        toBottom = false;
                                    }
                                    if(nextVerticalFocusView!=null){
                                        nextVerticalItemView = findContainingItemView(nextVerticalFocusView);
                                    }
                                    if(nextVerticalItemView!=null){
                                        nextVerticalItemPos = mRecyclerView.getChildAdapterPosition(nextVerticalItemView);
                                    }
                                    if(nextVerticalItemPos!=RecyclerView.NO_POSITION){
                                        RecyclerView.ViewHolder nextViewholder = mRecyclerView.findViewHolderForAdapterPosition(nextVerticalItemPos);
                                        if (nextViewholder != null) {
                                            itemParams = (RecyclerView.LayoutParams) nextViewholder.itemView.getLayoutParams();
                                            if(itemParams!=null){
                                                margin = toBottom ? itemParams.bottomMargin : itemParams.topMargin;
                                            }
                                            itemSpace = nextViewholder.itemView.getHeight();
                                        }
                                    }

                                }
                                offsetWidthNextItemView = Math.max(DEFAULT_MIN_SPACE, (int) (itemSpace *mShowNextItemScale + margin));
                            }
                        }
                    }

                    RecyclerView.LayoutParams itemParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                    if (dx != 0) {
                        if (dx > 0) {
                            dx = dx + itemParams.rightMargin + offsetWidthNextItemView;
                        } else {
                            dx = dx - itemParams.leftMargin - offsetWidthNextItemView;
                        }
                    }

                    if (dy != 0) {
                        if (dy > 0) {
                            dy = dy + itemParams.rightMargin + offsetWidthNextItemView;
                        } else {
                            dy = dy - itemParams.leftMargin - offsetWidthNextItemView;
                        }
                    }
                }
            }
        }

        if (dx != 0 || dy != 0) {
            if (immediate) {
                parent.scrollBy(dx, dy);
            } else {
                parent.smoothScrollBy(dx, dy);
            }
            return true;
        }
        return false;
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        return super.onInterceptFocusSearch(focused, direction);
    }
}
