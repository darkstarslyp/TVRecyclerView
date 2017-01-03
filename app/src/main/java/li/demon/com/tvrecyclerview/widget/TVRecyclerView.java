package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 *
 *
 * Created by demon on 16/12/12.
 */

public class TVRecyclerView extends RecyclerView implements OnItemViewFocusListener {


    public FocusFindInterface focusFindInterface;

    private long _lastPress;

    private boolean bCanFastMove = false;


    public FocusViewItemInfo mFocusViewItemInfo;

    public TVRecyclerView(Context context) {
        super(context);
        init();
    }

    public TVRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TVRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mFocusViewItemInfo = new FocusViewItemInfo();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (bCanFastMove ) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {

        if (mFocusViewItemInfo.isValidPos()) {
            RecyclerView.ViewHolder viewHolder = findViewHolderForAdapterPosition(mFocusViewItemInfo.pos);
            if (viewHolder != null) {
                int focusLayoutPos = indexOfChild(viewHolder.itemView);
                if (i == focusLayoutPos) {
                    return childCount - 1;
                } else if (i == childCount - 1) {
                    return focusLayoutPos;
                }
            }
        }
        return i;
    }


    @Override
    public void onListItemFocused(View view, int position, float scale) {
        if (view == null) return;
        mFocusViewItemInfo.setFocusViewItemInfo(view, position, scale);
        if (mFocusViewItemInfo.isValidPos()) {
            RecyclerView.ViewHolder viewHolder = findViewHolderForAdapterPosition(mFocusViewItemInfo.pos);
            if (viewHolder != null) {
                bringChildToFront(viewHolder.itemView);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {  //兼容5.0以下版本
                    invalidate();
                    requestLayout();
                }
            }
        }
    }

    @Override
    public void onListItemUnFocused(View view, int position, float scale) {
        mFocusViewItemInfo.reset();
        invalidate();
        requestLayout();
    }


    public FocusViewItemInfo getFocusViewItemInfo() {
        return mFocusViewItemInfo;
    }




    public void setFocusFindInterface(FocusFindInterface focusFindInterface) {
        this.focusFindInterface = focusFindInterface;
    }

    @Override
    public View focusSearch(int direction) {
        if(focusFindInterface!=null){
            View focusView = focusFindInterface.searchFocusView(this,super.focusSearch(direction),direction);
            return focusView;
        }
        return super.focusSearch(direction);
    }

    public class FocusViewItemInfo {

        public int pos = RecyclerView.NO_POSITION;
        public View mFocusView;
        public float scale = 1.0f;
        public ViewHolder mViewHolder;

        public void setFocusViewItemInfo(View mFocusView, int pos, float scale) {
            this.pos = pos;
            this.mFocusView = mFocusView;
            this.scale = scale;
            mViewHolder = findViewHolderForAdapterPosition(pos);
        }


        public void reset() {
            pos = RecyclerView.NO_POSITION;
            this.mFocusView = null;
            this.scale = 1.0f;
        }


        public boolean isValidPos() {
            return pos != RecyclerView.NO_POSITION;
        }

    }
}
