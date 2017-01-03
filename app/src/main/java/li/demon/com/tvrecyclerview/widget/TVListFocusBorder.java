package li.demon.com.tvrecyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewParent;

import li.demon.com.tvrecyclerview.R;

/**
 * 只负责绘制焦点框
 * Created by demon on 16/12/28.
 */

public class TVListFocusBorder extends RecyclerView.ItemDecoration {


    private Drawable mBorderDrawable;
    private Rect mBorderPadding;


    private boolean bFrontBorderDrawable = true;

    private Context context;
    private TVRecyclerView mTVRecyclerView;

    private int mLeftDrawableOffset = 0;
    private int mTopDrawableOffset = 0;
    private int mRightDrawableOffset = 0;
    private int mBottomDrawableOffset = 0;

    private int offset;


    private TVRecyclerView.FocusViewItemInfo mFocusViewItemInfo;

    public TVListFocusBorder(Context context,TVRecyclerView recyclerView) {
        this.context = context;
        this.mTVRecyclerView = recyclerView;
        init();
    }

    public void bind(){
        this.mTVRecyclerView.addItemDecoration(this);
    }

    public void unBind(){
        this.mTVRecyclerView.removeItemDecoration(this);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        mFocusViewItemInfo = mTVRecyclerView.getFocusViewItemInfo();
        if(mFocusViewItemInfo!=null&&!bFrontBorderDrawable){
            drawBorder(c,parent);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        mFocusViewItemInfo = mTVRecyclerView.getFocusViewItemInfo();
        if(mFocusViewItemInfo!=null&&bFrontBorderDrawable){
            drawBorder(c,parent);
        }
    }


    private void init() {
        mBorderPadding = new Rect();
        setBorderDrawable(R.drawable.button_bg_press2);

    }


    private void drawBorder(Canvas canvas,RecyclerView parent){
        if(mFocusViewItemInfo.isValidPos()&&mFocusViewItemInfo.mFocusView.isFocused()){
            canvas.save();
            int hScaleSpace = (int) (mFocusViewItemInfo.mFocusView.getWidth()*(mFocusViewItemInfo.scale-1))/2;  //计算缩放之后左右需要偏移的位置
            int vScaleSpace = (int) (mFocusViewItemInfo.mFocusView.getHeight()*(mFocusViewItemInfo.scale-1))/2;

            int left = calLeft(mFocusViewItemInfo,parent);  //mFocusViewItemInfo.mFocusView.getLeft()-mLeftDrawableOffset-hScaleSpace;
            int right = left+mFocusViewItemInfo.mFocusView.getWidth()+mRightDrawableOffset+hScaleSpace;
            left = left - mLeftDrawableOffset-hScaleSpace;

            int top = calTop(mFocusViewItemInfo,parent);//mFocusViewItemInfo.mFocusView.getTop()-mTopDrawableOffset-vScaleSpace;
            int bottom = top+mFocusViewItemInfo.mFocusView.getHeight()+mBottomDrawableOffset+vScaleSpace;
            top = top-mTopDrawableOffset-vScaleSpace;

            mBorderDrawable.setBounds(left,top,right,bottom);
            mBorderDrawable.draw(canvas);
            canvas.restore();
        }
    }


    private int calLeft(TVRecyclerView.FocusViewItemInfo info,RecyclerView parent){

        View focusView = info.mFocusView;
        if(focusView==parent) return 0;

        int left = focusView.getLeft();

        ViewParent viewParent ;
        for(viewParent=focusView.getParent() ;viewParent!=null&&viewParent!=parent&&viewParent instanceof View;viewParent=focusView.getParent()){
            focusView = (View)viewParent;
            left += focusView.getLeft();
        }

        return left;
    }

    private int calTop(TVRecyclerView.FocusViewItemInfo info,RecyclerView parent){
        View focusView = info.mFocusView;
        if(focusView==parent) return 0;

        int top = focusView.getTop();

        ViewParent viewParent ;
        for(viewParent=focusView.getParent() ;viewParent!=null&&viewParent!=parent&&viewParent instanceof View;viewParent=focusView.getParent()){
            focusView = (View)viewParent;
            top += focusView.getTop();
        }

        return top;

    }


    public Rect setBorderDrawable(@DrawableRes int resId) {
        mBorderDrawable = context.getResources().getDrawable(resId);
        mBorderDrawable.getPadding(mBorderPadding);
        setDrawableOffset(mBorderPadding.left,mBorderPadding.top,mBorderPadding.right,mBorderPadding.bottom);
        return mBorderPadding;
    }

    public Rect getBorderPadding(){
        return mBorderPadding;
    }


    public void setFrontBorderDrawable(boolean bFrontBorderDrawable) {
        this.bFrontBorderDrawable = bFrontBorderDrawable;
    }

    public void setDrawableOffset(int leftDrawableOffset,int topDrawableOffset,int rightDrawableOffset,int bottomDrawableOffset){
        this.mLeftDrawableOffset = leftDrawableOffset+offset;
        this.mTopDrawableOffset = topDrawableOffset+offset;
        this.mRightDrawableOffset = rightDrawableOffset+offset;
        this.mBottomDrawableOffset = bottomDrawableOffset+offset;
    }

    /**
     * 绘制边框与View有明显的间隙
     * @param offset
     */
    public void setOffset(int offset){
        this.offset = offset;
    }
}
