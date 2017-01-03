package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by demon on 17/1/3.
 */

public class TVMoveLayoutManager extends LinearLayoutManager {


    private RecyclerView mRecyclerView;

    private int mOffset = 0;



    public TVMoveLayoutManager(Context context) {
        super(context);
    }

    public TVMoveLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public TVMoveLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTVRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setStartOffset(int startOffset){
        this.mOffset = startOffset;
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

        RecyclerView.LayoutParams childParam = null;
        RecyclerView.ViewHolder focusViewHolder = null;
        int focusItemPos = RecyclerView.NO_POSITION;
        View itemContanierView = null;

        if (child.getLayoutParams() instanceof RecyclerView.LayoutParams) {
            childParam = (RecyclerView.LayoutParams) child.getLayoutParams();
            focusItemPos = childParam.getViewAdapterPosition();
            focusViewHolder = mRecyclerView.findViewHolderForAdapterPosition(focusItemPos);
            itemContanierView = focusViewHolder.itemView;
        } else {
            if (findContainingItemView(child, mRecyclerView) != null) {
                itemContanierView = findContainingItemView(child, mRecyclerView);
                childParam = itemContanierView == null ? null : (RecyclerView.LayoutParams) itemContanierView.getLayoutParams();
                focusItemPos = childParam == null ? RecyclerView.NO_POSITION : childParam.getViewAdapterPosition();
                focusViewHolder = focusItemPos == RecyclerView.NO_POSITION ? null : mRecyclerView.findViewHolderForAdapterPosition(focusItemPos);
                child = itemContanierView == null ? child : itemContanierView;
            }
        }

        if (itemContanierView != null && focusViewHolder != null) {
            rect.set(0, 0, focusViewHolder.itemView.getWidth(), focusViewHolder.itemView.getHeight());
        }

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();
        int childLeft = child.getLeft() + rect.left - child.getScrollX();
        int childTop = child.getTop() + rect.top - child.getScrollY();
        int childRight = childLeft + rect.width();
        int childBottom = childTop + rect.height();

        if (focusViewHolder != null) {
            childLeft = focusViewHolder.itemView.getLeft() + rect.left - focusViewHolder.itemView.getScrollX();
            childTop = focusViewHolder.itemView.getTop() + rect.top - focusViewHolder.itemView.getScrollY();
            childRight = childLeft + rect.width();
            childBottom = childTop + rect.height();
        }

        final int offScreenLeft = Math.min(0,childLeft - parentLeft);
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

        if (focusViewHolder != null && mRecyclerView != null && (dy != 0 || dx != 0)) {

            int total = getItemCount();
            int left = 0;

            int horizontalItemSpace = childParam.width+childParam.leftMargin+childParam.rightMargin;
            int verticalItemSpace =childParam.height+childParam.topMargin+childParam.bottomMargin;

            int vCompleteShowCount = getHeight()/verticalItemSpace;
            int hCompleteShowCount = getWidth()/horizontalItemSpace;

            if (dx != 0) {
                if (dx > 0) {
                    left = total - focusItemPos;
                    if (left >= hCompleteShowCount) {
                        dx = (childParam.width-getWidth()+focusViewHolder.itemView.getLeft())+childParam.rightMargin+(hCompleteShowCount-1) * horizontalItemSpace+mOffset;
                    }else{
                        dx = (childParam.width-getWidth()+focusViewHolder.itemView.getLeft())+childParam.rightMargin+(left-1) * horizontalItemSpace+mOffset;
                    }
                } else {
                    left = focusItemPos+1;
                    if (left >= hCompleteShowCount) {
                        dx = -(((hCompleteShowCount-1) * horizontalItemSpace+ mOffset)+childParam.leftMargin+childParam.width-focusViewHolder.itemView.getRight());
                    }else{
                        dx = -(((left-1) * horizontalItemSpace+ mOffset)+childParam.leftMargin+childParam.width-focusViewHolder.itemView.getRight());
                    }
                }
            }

            if (dy != 0) {
                if (dy > 0) {
                    left = total - focusItemPos;
                    if (left >= vCompleteShowCount) {

                        dy = (childParam.height-getHeight()+focusViewHolder.itemView.getTop())+childParam.bottomMargin+(vCompleteShowCount-1) * verticalItemSpace+mOffset;

                    }else{

                        dy = (childParam.height-getHeight()+focusViewHolder.itemView.getTop())+childParam.bottomMargin+(left-1) * verticalItemSpace+mOffset;

                    }
                } else {
                    left = focusItemPos+1;
                    if (left >= vCompleteShowCount) {
                        dy = -(((vCompleteShowCount-1) * verticalItemSpace+ mOffset)+childParam.topMargin+childParam.height-focusViewHolder.itemView.getBottom());
                    }else{
                        dy = -(((left-1) * verticalItemSpace+ mOffset)+childParam.topMargin+childParam.height-focusViewHolder.itemView.getBottom());
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

    public View findContainingItemView(View view, RecyclerView recyclerView) {
        ViewParent parent;
        for (parent = view.getParent(); parent != null && parent != recyclerView && parent instanceof View; parent = view.getParent()) {
            view = (View) parent;
        }

        return parent == recyclerView ? view : null;
    }
}
