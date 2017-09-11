package com.yndongyong.xrefreshlayout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


/**
 * Created by ad15 on 2017/9/4.
 */

public class XRefreshLayout extends ViewGroup {

    private static final String TAG = XRefreshLayout.class.getSimpleName();

    /* 控制属性*/
    protected boolean mEnableRefresh = true;

    protected boolean mEnableLoadMore = true;

    protected boolean mEnableAutoRefresh = false;

    protected boolean mEnableAutoLoadMore = false;


    //sipper 最初的top的位置
    protected float mSipperOriginalTop;

    //距离顶部的滑动距离
    protected int mOffsetTop = -1;

    //总的下拉距离
    protected float mDragDistances;

    //触发刷新的临界值
    protected float mCritical;

    /*第一次按下点的位置*/
    protected float mInitialDownX;
    protected float mInitialDownY;

    protected boolean mIsDraging = false;

    //多点触控
    private static final int INVALID_POINTER = -1;

    //拖动阻尼比
    private static final float DRAG_RATE = .5f;
    private int mScreenHeightPixels;
    //系统最小滑动距离
    private int mTouchSlop;
    //系统动画时间
    private int mMediumAnimationDuration;
    //触控点ID
    private int mActivePointerId = INVALID_POINTER;

    //当前的状态
    protected float mStatus = RefreshLayout.STATUS_IDLE;

    protected XHeaderView mHeaderView;
    protected RecyclerView mTarget;
    protected View mFooterView;

    protected int mHeaderViewHeight;

    //复位动画
    private Animation mRestAnimation;


    public XRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        mScreenHeightPixels = getResources().getDisplayMetrics().heightPixels;
        createHeader();
        setWillNotDraw(false);
    }

    private void createHeader() {
        mHeaderView = new BasicNormalHeaderView(this);
        addView(mHeaderView.getView(), MATCH_PARENT, WRAP_CONTENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
    }

    private void ensureTarget() {
        if (mTarget == null) {

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            View headV = mHeaderView.getView();
            LayoutParams lp = (LayoutParams) headV.getLayoutParams();
            int height = lp.height;
            int width = lp.width;

            int widthM = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            if (height == WRAP_CONTENT) {
                int heightM = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
                headV.measure(widthM, heightM);
                mHeaderViewHeight = headV.getMeasuredHeight();
                mSipperOriginalTop = -headV.getMeasuredHeight();
                mOffsetTop = (int) mSipperOriginalTop;
                // TODO: 2017/9/8
//                mHeaderView.moveSpinner(,mSipperOriginalTop);
//               ;
//                ViewCompat.offsetTopAndBottom(mHeaderView.getView(), (int) (mSipperOriginalTop - mHeaderView.getView().getTop()));

            }
        }


    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mHeaderView != null) {
            View headV = mHeaderView.getView();
            mHeaderView.getView().layout(0, mOffsetTop, headV.getMeasuredWidth(), headV.getMeasuredHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = MotionEventCompat.getActionMasked(ev);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = ev.getY();
                mIsDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mDragDistances = ev.getY() - mInitialDownY;
                if (mDragDistances > mTouchSlop) {
                    return true;
                }
                break;
            default:
                return super.onInterceptTouchEvent(ev);
        }
        return mIsDraging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        Log.d(TAG, "onTouchEvent  mOffsetTop： " + mOffsetTop);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                mDragDistances = event.getY() - mInitialDownY;
                final float overscrollTop = (mDragDistances) * DRAG_RATE;
                Log.d(TAG, "onTouchEvent  overscrollTop： " + overscrollTop);
                mHeaderView.moveSpinner((int) overscrollTop, mHeaderViewHeight);

                float originalDragPercent = overscrollTop / mHeaderViewHeight;

                float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                float extraOS = Math.abs(overscrollTop) - mHeaderViewHeight;
                float slingshotDist = mHeaderViewHeight/2;
                float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2)
                        / slingshotDist);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float extraMove = (slingshotDist) * tensionPercent * 2;

                int targetY = (int) (mSipperOriginalTop + (int) ((slingshotDist * dragPercent) + extraMove));
                ViewCompat.offsetTopAndBottom(mHeaderView.getView(),  targetY  - mOffsetTop);
                mOffsetTop = mHeaderView.getView().getTop();

                break;
            case MotionEvent.ACTION_UP:
                ViewCompat.offsetTopAndBottom(mHeaderView.getView(), (int) mSipperOriginalTop - mOffsetTop);
                mOffsetTop = (int) mSipperOriginalTop;
                return false;
//                break;

        }

        return true;
//        return super.onTouchEvent(event);
    }


    private void moveSpinner(int overscrollTop) {

    }
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(MATCH_PARENT, MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }
}
