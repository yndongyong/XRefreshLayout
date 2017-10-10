package com.yndongyong.xrefreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.AbsListView;

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

    //刷新时可以滚动内容 默认false
    private boolean mEnableScrollContentWhenRefresh = false;


    //sipper 最初的top的位置
    protected int mSipperInitTop;

    //sipper 当前的位置
    protected int mSipperCurrentOffset = -1;

    //sipper 刷新触发点
    protected int mSipperEndOffset;

    //target 当前的位置
    protected int mTargetCurrentOffset = 0;
    //target 初始位置
    protected int mTargetInitOffset = 0;
    //target headerview 刷新时 target 的位置
    protected int mTargetRefreshOffset = 0;

    //总的下拉距离
    protected float mDragDistances;


    /*第一次按下点的位置*/
    protected float mInitialDownX;
    protected float mInitialDownY;

    protected float mLastMotionY;
    protected float mLastMotionX;

    protected boolean mIsDraging = false;

    //多点触控
    private static final int INVALID_POINTER = -1;

    //拖动阻尼比
    private static final float DRAG_RATE = .5f;
    //系统最小滑动距离
    private int mTouchSlop;
    //系统动画时间
    private int mMediumAnimationDuration;
    //触控点ID
    private int mActivePointerId = INVALID_POINTER;

    //当前的状态
    protected int mStatus = Status.IDLE;

    protected XHeaderView mHeaderView;
    protected View mTargetView;
    protected View mFooterView;

    protected int mHeaderViewHeight;

    private volatile boolean isRefreshing = false; //是否正在刷新
    private boolean isAutoRefresh = false; //是否自动刷新

    //复位动画
    private Animation mRestAnimation;

    private AccelerateInterpolator accelerateInterpolator;

    private RefreshListener refreshListener;

    private SipperTargetOffsetCalculator mSipperTargetOffsetCalculator;

    //滚动刷新位置结束时调用
    private AnimatorListenerAdapter refreshAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // TODO: 2017/10/10  过滤在刷新过程中又再次下拉了 过滤得不完全
            if (!isRefreshing) {
                isRefreshing = true;
                mHeaderView.changeStatus(Status.REFRESH);
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
            }
        }
    };

    //滚动过程中更新 target 的 动画监听
    private ValueAnimator.AnimatorUpdateListener targetOffsetUpdateAnimationListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int targetY = (int) animation.getAnimatedValue();
                    if (targetY != mTargetCurrentOffset) {
                        int offset = targetY - mTargetCurrentOffset;
                        ViewCompat.offsetTopAndBottom(mTargetView, offset);
                        mTargetCurrentOffset = targetY;
                    }

                }
            };
    //滚动过程中更新 sipper 的 动画监听
    private ValueAnimator.AnimatorUpdateListener sipperOffsetUpdateAnimationListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int targetY = (int) animation.getAnimatedValue();
            if (targetY != mSipperCurrentOffset) {
                int offset = targetY - mSipperCurrentOffset;
                ViewCompat.offsetTopAndBottom(mHeaderView.getView(), offset);
                mSipperCurrentOffset = mHeaderView.getView().getTop();
            }

        }
    };

    //变为idle状态时调用
    private AnimatorListenerAdapter resetAnimationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // TODO: 2017/10/9  回到初始位置结束
            reset();
            mHeaderView.changeStatus(Status.IDLE);
        }
    };

    public void setRefreshListener(RefreshListener mListener) {
        this.refreshListener = mListener;
    }

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
        createHeader();
        setWillNotDraw(false);
        accelerateInterpolator = new AccelerateInterpolator(3.0f);

    }

    private void createHeader() {
        mHeaderView = new BasicNormalHeaderView(this);
        ViewGroup.LayoutParams layoutParams = mHeaderView.getView().getLayoutParams();
        addView(mHeaderView.getView(), layoutParams != null ? layoutParams : new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureTarget();
    }

    private void ensureTarget() {
        if (mTargetView == null) {
            int childCount = getChildCount();
            if (childCount != 2) {
                throw new IllegalArgumentException("The xrefreshlayout must be has two child view");
            }
            for (int i = 0, j = getChildCount(); i < j; i++) {
                View view = getChildAt(i);
                if (!mHeaderView.getView().equals(view)) {
                    mTargetView = view;
                    break;
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureTarget();
        if (mHeaderView != null) {
            View headV = mHeaderView.getView();
            measureChild(headV, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) headV.getLayoutParams();
            int height = lp.height;
            int width = lp.width;

            int widthM = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            if (height == WRAP_CONTENT) {
                int heightM = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.AT_MOST);
                headV.measure(widthM, heightM);

            } else {
                int heightM = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                headV.measure(widthM, heightM);
            }
            mSipperInitTop = -headV.getMeasuredHeight();
            mSipperCurrentOffset = mSipperInitTop;
            // TODO: 2017/10/10 sipper 的刷新位置初始位设为 0  需要提供设置方法，且要考虑和mTargetRefreshOffset 的限制
            mSipperEndOffset = 0;
            mHeaderViewHeight = headV.getMeasuredHeight();
        }
        if (mTargetView != null) {
            int targetMeasureWidthSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
            int targetMeasureHeightSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
            mTargetView.measure(targetMeasureWidthSpec, targetMeasureHeightSpec);
            //多次测量的时候 改变这个值 和移动改变这个值，会造成冲突，导致界面会闪
            if (mTargetCurrentOffset == 0) {
                mTargetInitOffset = getPaddingTop();
                mTargetCurrentOffset = mSipperCurrentOffset + mHeaderViewHeight;
            }
            mTargetRefreshOffset = mHeaderViewHeight;

        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }
        if (mHeaderView != null) {
            View headV = mHeaderView.getView();
            mHeaderView.getView().layout(0, mSipperCurrentOffset, headV.getMeasuredWidth(), mSipperCurrentOffset + headV.getMeasuredHeight());
        }
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        mTargetView.layout(childLeft, (childTop + mTargetCurrentOffset),
                childLeft + childWidth, (childTop + childHeight + mTargetCurrentOffset));
    }


    private boolean canScrollUp(View view) {
        if (view == null) {
            return false;
        }
        // TODO: 2017/10/9 可以设置一个回调交由子view控制是否可以刷新
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(view, -1) || view.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(view, -1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = MotionEventCompat.getActionMasked(ev);

        if (canScrollUp(mTargetView) || !mEnableRefresh) {//没有拦截事件
            return false;
        }

        if (isRefreshing && mEnableScrollContentWhenRefresh) {
            return true;
        }
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = ev.getY();
                mInitialDownX = ev.getX();
                mLastMotionY = mInitialDownY;
                mLastMotionX = mInitialDownY;
                mIsDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float y = ev.getY();
                float x = ev.getX();
                startDragging(x, y);
                break;
            default:
                return super.onInterceptTouchEvent(ev);
        }
        return mIsDraging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        Log.d(TAG, "onTouchEvent  mSipperCurrentOffset： " + mSipperCurrentOffset);

        if (canScrollUp(mTargetView) || (isRefreshing && mEnableScrollContentWhenRefresh)) {
            return false;
        }
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mIsDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                final float x = event.getX();
                startDragging(x, y);

                if (mIsDraging) {
                    mDragDistances = (int) (y - mLastMotionY);
                    final int dy = (int) ((mDragDistances) * DRAG_RATE);
                    Log.d(TAG, "onTouchEvent  dy： " + dy);

                    if (dy > 0) {
                        moveChildView(dy);
                    } else {
                        //TODO 还需要处理 sipper被完全隐藏之后，target向上滚动
                        int offset = moveChildView(dy);
                        float delta = Math.abs(dy) - Math.abs(offset);
                        if (delta > 0) {
                            // 重新dispatch一次down事件，使得列表可以继续滚动
                            event.setAction(MotionEvent.ACTION_DOWN);
                            // 立刻dispatch一个大于mSystemTouchSlop的move事件，防止触发TargetView
                            float offsetLoc = mTouchSlop + 1;
                            if (delta > offsetLoc) {
                                offsetLoc = delta;
                            }
                            event.offsetLocation(0, offsetLoc);
                            super.dispatchTouchEvent(event);
                            event.setAction(actionMasked);
                            // 再dispatch一次move事件，消耗掉所有dy
                            event.offsetLocation(0, -offsetLoc);
                            super.dispatchTouchEvent(event);
                        } else {
                            moveToInitPosition();
                        }
                    }
                    mLastMotionY = y;
                    mLastMotionX = x;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDraging) {
                    mIsDraging = false;
                    final float y1 = event.getY();
                    final float x1 = event.getX();
                    finishSipper(x1, y1);
                }
                return false;
//                break;

        }

        return true;
    }

    //<editor-fold desc="重置 reset ">
    private void reset() {
        isRefreshing = false;
        isAutoRefresh = false;
    }

    //</editor-fold>

    //<editor-fold desc="结束滑动 finishSipper 调用刷新回调">
    private void finishSipper(float x1, float y1) {
        if (mTargetCurrentOffset >= mTargetRefreshOffset ) {
            //刷新点
            final int startValue = mTargetCurrentOffset;
            ValueAnimator targetAnimator = ValueAnimator.ofInt(startValue, mTargetRefreshOffset);
            targetAnimator.addUpdateListener(targetOffsetUpdateAnimationListener);

            ValueAnimator sipperAnimator = ValueAnimator.ofInt(mSipperCurrentOffset, mSipperEndOffset);
            sipperAnimator.addUpdateListener(sipperOffsetUpdateAnimationListener);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(targetAnimator, sipperAnimator);
            animatorSet.setDuration(mMediumAnimationDuration);
            animatorSet.setInterpolator(accelerateInterpolator);
//            if (!isRefreshing)
                animatorSet.addListener(refreshAnimationListener);
            animatorSet.start();
        } else {
            //没有到刷新点
            if (!isRefreshing) {
                moveToInitPosition();
            }

        }
    }
    //</editor-fold>

    //<editor-fold desc="滑动到初始位置 moveToInitPosition">

    /**
     * 滚动sipper 和target 到初始位置
     */
    private void moveToInitPosition() {
        Log.e(TAG, "moveToInitPosition  mTargetCurrentOffset： " + mTargetCurrentOffset);

        mHeaderView.changeStatus(Status.SCROLL_TO_INIT);

        int startValue = mTargetCurrentOffset;
        ValueAnimator targetAnimator = ValueAnimator.ofInt(startValue, mTargetInitOffset);
        targetAnimator.addUpdateListener(targetOffsetUpdateAnimationListener);


        ValueAnimator sipperAnimator = ValueAnimator.ofInt(mSipperCurrentOffset, mSipperInitTop);
        sipperAnimator.addUpdateListener(sipperOffsetUpdateAnimationListener);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(targetAnimator, sipperAnimator);
        animatorSet.setDuration(mMediumAnimationDuration);
        animatorSet.setInterpolator(accelerateInterpolator);
        animatorSet.addListener(resetAnimationListener);
        animatorSet.start();
    }
    //</editor-fold>

    //<editor-fold desc="移动view moveChildView">
    private int moveChildView(int dy) {
        int targetY = (dy + mTargetCurrentOffset);
//        targetY = Math.min(targetY, mTargetRefreshOffset);
        if (targetY != mTargetCurrentOffset) {
            int offset = targetY - mTargetCurrentOffset;
            ViewCompat.offsetTopAndBottom(mTargetView, offset);
            mTargetCurrentOffset = targetY;
        }
        if (mSipperTargetOffsetCalculator == null) {
            mSipperTargetOffsetCalculator = new DefaultTargetOffsetCalculator();
        }
        int target = mSipperTargetOffsetCalculator.calculateOffset(dy,
                mSipperCurrentOffset, mSipperInitTop, mSipperEndOffset, mHeaderViewHeight,
                mTargetCurrentOffset, mTargetInitOffset, mTargetRefreshOffset);
        int offset = 0;
        if (target != mSipperCurrentOffset) {
            offset = target - mSipperCurrentOffset;
            ViewCompat.offsetTopAndBottom(mHeaderView.getView(), offset);
            mSipperCurrentOffset = target;

            mHeaderView.onPull(offset, mSipperCurrentOffset, mSipperInitTop, mSipperEndOffset);
            if (mTargetCurrentOffset >= mTargetRefreshOffset && !isRefreshing) {
                mHeaderView.changeStatus(Status.OVER_REFRESH_OFFSET);
                // TODO: 2017/10/10 可能下面这个分支  的第二个条件有问题
            } else if (mTargetCurrentOffset <= mTargetInitOffset && !isRefreshing) {
                mHeaderView.changeStatus(Status.IDLE);
            }
        }

        return offset;
    }
    //</editor-fold >

    //<editor-fold desc="结束刷新 refreshComplete">
    public void refreshComplete() {
//        isRefreshing = false; 动画都没有执行完就设置这个值，在重复下拉刷新会有影响
        moveToInitPosition();
    }
    //</editor-fold >

    //<editor-fold desc="自动刷新 autoRefresh">
    public void autoRefresh() {
        if (mStatus != Status.IDLE && mEnableAutoRefresh) {
            return;
        }
        isAutoRefresh = true;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (moveChildView(mHeaderViewHeight + 10) >= mHeaderViewHeight) {
                    finishSipper(0, 0);

                }
            }
        }, 200);


    }
    //</editor-fold >

    /**
     * 计算sipper 的新位置
     *
     * @param overscrollTop
     * @return
     */
    //sipper 固定在sipperInitOffset 和 targetRefreshInitOffset之间
    private int caculateSipperNewoffset1(int overscrollTop) {
        int targetY = 0;
        if (mTargetCurrentOffset >= mTargetRefreshOffset) {
            //超过刷新线
            targetY = mTargetInitOffset;
        } else if (mTargetCurrentOffset <= mTargetInitOffset) {
            targetY = mSipperInitTop;
            //达到初始线
        } else {
            //中间过程
            targetY = overscrollTop + mSipperCurrentOffset;
        }
        return targetY;
    }

    private void startDragging(float x, float y) {
        int dy = (int) (y - mLastMotionY);
        int dx = (int) (x - mLastMotionX);
        //    下滑
        //上滑(dy < -(mTouchSlop + 1) && mTargetCurrentOffset > mTargetInitOffset))
        if ((dy > mTouchSlop || (dy < -mTouchSlop && mTargetCurrentOffset > mTargetInitOffset)) && !mIsDraging) {
            mIsDraging = true;
            mLastMotionY = mInitialDownY;
        }
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

    public interface RefreshListener {
        void onRefresh();
    }

    //<editor-fold desc="getter and setter">
    public void setEnableAutoRefresh(boolean enableAutoRefresh) {
        this.mEnableAutoRefresh = enableAutoRefresh;
    }


    public void setSipperTargetOffsetCalculator(SipperTargetOffsetCalculator sipperTargetOffsetCalculator) {
        this.mSipperTargetOffsetCalculator = sipperTargetOffsetCalculator;
    }

    //</editor-fold>
}
