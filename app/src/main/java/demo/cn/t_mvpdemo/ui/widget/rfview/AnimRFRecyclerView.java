package demo.cn.t_mvpdemo.ui.widget.rfview;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.Util.PhoneUtil;
import demo.cn.t_mvpdemo.ui.widget.rfview.listener.OverScrollListener;
import demo.cn.t_mvpdemo.ui.widget.rfview.manager.AnimRFGridLayoutManager;
import demo.cn.t_mvpdemo.ui.widget.rfview.manager.AnimRFLinearLayoutManager;
import demo.cn.t_mvpdemo.ui.widget.rfview.manager.AnimRFStaggeredGridLayoutManager;

/**
 * 下拉放大图片recyclerview
 */
public class AnimRFRecyclerView extends RecyclerView implements Runnable {

    private int CRIIMAGERADIUS=10;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private Context mContext;
    private ArrayList<View> mFootViews = new ArrayList<>();
    private Adapter mAdapter;

    private ImageView headerImage;
    private int headerImageHeight = -1; // 默认高度
    private int headerImageMaxHeight = -1; // 最大高度
    private float scaleRatio = 1.05f; // 最大拉伸比例
    private int phoneHeight;
    private int statuBarHeight = PhoneUtil.getStatusBarHeight(getContext());


    private boolean isTouching = false; // 是否正在手指触摸的标识
    private boolean isLoadingData = false; // 是否正在加载数据

    private LoadDataListener mLoadDataListener;

    private boolean isEnable = true;


    /**
     * 阻尼系数,越小阻力就越大.
     */
    private static final float SCROLL_RATIO = 0.7f;
    boolean isTop = false;
    boolean isEnablePull = true;

    float lastY = -1.0f;
    float currentY = -1.0f;
    float downY = -1.0f;
    int scrollY;

    private OverScrollListener mOverScrollListener = new OverScrollListener() {
        @Override
        public void overScrollBy(int dy) {
            // dy为拉伸过度时每毫秒拉伸的距离，正数表示向上拉伸多度，负数表示向下拉伸过度
            scrollY = dy;
            isTop = dy < 0;

//            if (isEnable && !isLoadingData && isTouching
//                    && ((dy < 0 && headerImage.getLayoutParams().height < headerImageMaxHeight)
//                    || (dy > 0 && headerImage.getLayoutParams().height > headerImageHeight))) {
//                onScrollChanged(0, 0, 0, 0);
//            }
        }
    };

    public AnimRFRecyclerView(Context context) {
        this(context, null);
    }

    public AnimRFRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimRFRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOverScrollMode(OVER_SCROLL_NEVER);
        mScalingRunnable = new ScalingRunnable();
        phoneHeight = PhoneUtil.getDisplayHeight(getContext());
        statuBarHeight = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT ? 0 :
                PhoneUtil.getStatusBarHeight(getContext());
        post(this);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

    }


    //    CircleProgressBar mProgressBar;

    private void createProgressView() {
//        mProgressBar = new CircleProgressBar(getContext());
//        mProgressBar.setBackgroundColor(Color.WHITE);
//        mProgressBar.setColorSchemeColors(Color.GREEN, Color.YELLOW, Color.RED);
    }

    /**
     * 添加头部视图，可以添加多个
     *
     * @param view 头部视图
     */
    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof WrapAdapter)) {
                mAdapter = new WrapAdapter(mHeaderViews, mFootViews, mAdapter);
            }
        }
    }

    /**
     * 添加脚部视图，此视图只能添加一个，添加多个时，默认最后添加的一个。
     *
     * @param view 底部视图
     */
    public void addFootView(final View view) {
        mFootViews.clear();
        mFootViews.add(view);
        if (mAdapter != null) {
            if (!(mAdapter instanceof WrapAdapter)) {
                mAdapter = new WrapAdapter(mHeaderViews, mFootViews, mAdapter);
            }
        }
    }

    /**
     * 设置头部拉伸图片
     *
     * @param headerImage 头部中的背景ImageView
     */
    public void setHeaderImage(ImageView headerImage) {
        this.headerImage = headerImage;
        // 防止第一次拉伸的时候headerImage.getLayoutParams().height = 0
        headerImageHeight = PhoneUtil.getDisplayWidth(getContext());
        headerImageMaxHeight = (int) (PhoneUtil.getDisplayWidth(getContext()) * scaleRatio);
    }

    public void setHeaderImageHeight(int headerImageHeight) {
        this.headerImageHeight = headerImageHeight;
        headerImageMaxHeight = (int) (headerImageHeight * scaleRatio);
    }

    /**
     * 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
     *
     * @param scaleRatio 头部的最大拉伸倍率，必须大于1，小于1则默认为1.5f
     */
    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
    }


    /**
     * 设置刷新和加载更多数据的监听
     *
     * @param listener {@link LoadDataListener}
     */
    public void setLoadDataListener(LoadDataListener listener) {
        mLoadDataListener = listener;
    }

    /**
     * 加载更多数据完成后调用，必须在UI线程中
     */
    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootViews.size() > 0) {
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    public void showFoot() {
        if (mFootViews.size() > 0) {
            mFootViews.get(0).setVisibility(VISIBLE);
        }
    }

    /**
     * 刷新数据完成后调用，必须在UI线程中
     */
    public void refreshComplete() {
        //TODO 隱藏刷新view
        isComplete = true;
        if (isTimeIn) //动画是否执行完成一圈
        {
            isLoadingData = false;
            isEnablePull = true;
            // 内容不能充满一页时，刷新完自动获取下一页
            smoothScrollBy(0, 1);
        }
        if (showLoadingFirst) {
            reset();
            endScaling();
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {

        if (mFootViews.isEmpty()) {
            // 新建脚部
            LinearLayout footerLayout = new LinearLayout(mContext);
            footerLayout.setGravity(Gravity.CENTER);
            footerLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mFootViews.add(footerLayout);

            footerLayout.addView(new ProgressBar(mContext, null, android.R.attr.progressBarStyleSmall));

            TextView text = new TextView(mContext);
            text.setText(mContext.getResources().getString(R.string.loading));
            footerLayout.addView(text);
        }
        // 使用包装了头部和脚部的适配器
        adapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(adapter);
        // 根据是否有头部/脚部视图选择适配器
        mAdapter = adapter;
    }

    @Override
    public void run() {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof AnimRFLinearLayoutManager) {
            // ListView布局
            ((AnimRFLinearLayoutManager) manager).setOverScrollListener(mOverScrollListener);
        } else if (manager instanceof AnimRFGridLayoutManager) {
            layoutGridAttach((AnimRFGridLayoutManager) manager);
        } else if (manager instanceof AnimRFStaggeredGridLayoutManager) {
            layoutStaggeredGridHeadAttach((AnimRFStaggeredGridLayoutManager) manager);
        }
        if (((WrapAdapter) mAdapter).getFootersCount() > 0) {
            // 脚部先隐藏
            mFootViews.get(0).setVisibility(GONE);
        }
    }

    /**
     * 给StaggeredGridLayoutManager附加头部和滑动过度监听
     *
     * @param manager {@link AnimRFStaggeredGridLayoutManager}
     */
    private void layoutStaggeredGridHeadAttach(AnimRFStaggeredGridLayoutManager manager) {
        manager.setOverScrollListener(mOverScrollListener);
        // 从前向后查找Header并设置为充满一行
        View view;
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            if (((WrapAdapter) mAdapter).isHeader(i)) {
                view = getChildAt(i);
                ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams())
                        .setFullSpan(true);
                view.requestLayout();
            } else {
                break;
            }
        }
    }

    /**
     * 给{@link StaggeredGridLayoutManager}附加脚部
     *
     * @param view 底部视图
     */
    private void layoutStaggeredGridFootAttach(View view) {
        // Footer设置为充满一行
        ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams())
                .setFullSpan(true);
        // view.requestLayout();
    }

    /**
     * 给{@link AnimRFGridLayoutManager}附加头部脚部和滑动过度监听
     *
     * @param manager {@link AnimRFGridLayoutManager}
     */
    private void layoutGridAttach(final AnimRFGridLayoutManager manager) {
        // GridView布局
        manager.setOverScrollListener(mOverScrollListener);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return ((WrapAdapter) mAdapter).isHeader(position) ||
                        ((WrapAdapter) mAdapter).isFooter(position) ? manager.getSpanCount() : 1;
            }
        });
        requestLayout();
    }

    private int firstVisibleItem;
    private int[] location = new int[2];
    LinearLayoutManager mManager;

    private LinearLayoutManager getManager() {
        if (mManager == null)
            mManager = (LinearLayoutManager) getLayoutManager();
        return mManager;
    }

    private boolean showLoadingFirst;

    public void setShowLoadingFirst(boolean showLoadingFirst) {
        this.showLoadingFirst = showLoadingFirst;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (getManager() != null) {
            firstVisibleItem = mManager.findFirstVisibleItemPosition();
            if (firstVisibleItem == 0) {
                View view = getChildAt(0);
                view.getLocationOnScreen(location);
                if (onScrollChangeListener != null) {
                    if (imageHeight != 0) {
                        onScrollChangeListener.onScrollChange(true, 0, (statuBarHeight - location[1]) * 225 / imageHeight);
                    } else {
                        onScrollChangeListener.onScrollChange(true, 0, 0);
                    }
                }
            } else {
                if (onScrollChangeListener != null) {
                    onScrollChangeListener.onScrollChange(false, 0, phoneHeight);
                }
            }
        } else {
            if (onScrollChangeListener != null) {
                onScrollChangeListener.onScrollChange(false, l, t);
            }
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    OnScrollChangeListener onScrollChangeListener;

    public interface OnScrollChangeListener {
        void onScrollChange(boolean isFirst, int x, int y);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        // 当前不滚动，且不是正在刷新或加载数据
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadDataListener != null && !isLoadingData) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            // 获取最后一个正在显示的Item的位置
            if (layoutManager instanceof AnimRFGridLayoutManager) {
                lastVisibleItemPosition = ((AnimRFGridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof AnimRFStaggeredGridLayoutManager) {
                int[] into = new int[((AnimRFStaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((AnimRFStaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((AnimRFLinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (isCanLoadMore) {
                if (layoutManager.getChildCount() > 2
                        && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                    if (mFootViews.size() > 0) {
                        mFootViews.get(0).setVisibility(VISIBLE);
                    }
                    // 加载更多
                    isLoadingData = true;
                    mLoadDataListener.onLoadMore();
                }
            }
        }
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    boolean isCanLoadMore = true;


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 设置是否执行刷新
     *
     * @param isRefrsh
     */
    public void setRefresh(boolean isRefrsh) {
        if (isRefrsh) {
            refresh();
        } else {
            refreshComplete();
        }
    }

    /**
     * 设置是否可刷新
     *
     * @param isEnable
     */
    public void setRefreshEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    private int loadingStyle = 0;

    public void setLoadingStyle(int loadingStyle) {
        this.loadingStyle = loadingStyle;
    }

    // 刷新
    private void refresh() {
        isLoadingData = true;
        isEnablePull = false;
        isTimeIn = false;
        isComplete = false;
        mLoadDataListener.onRefresh();
        //TODO 修改刷新view的樣式

        postDelayed(new Runnable() {
            @Override
            public void run() {
                isTimeIn = true;
                if (isComplete) {
                    refreshComplete();
                }
            }
        }, 1900);
    }

    private boolean isTimeIn = false;//动画是否执行完成
    private boolean isComplete = false;//数据是否加载完成

    private int getLoadingMarginBottom() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        if (loadingStyle == 0) {
            return PhoneUtil.dip2px(mContext, 20);
        } else {
            return PhoneUtil.dip2px(mContext, 10);
        }
//        } else {
//            if (loadingStyle == 0) {
//                return PhoneUtil.dip2px(mContext, 60);
//            } else {
//                return PhoneUtil.dip2px(mContext, 45);
//            }
//        }
    }

    /**
     * 自定义带有头部/脚部的适配器
     */
    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter mAdapter;
        private ArrayList<View> mHeaderViews;
        private ArrayList<View> mFootViews;
        final ArrayList<View> EMPTY_INFO_LIST = new ArrayList<>();
        private int headerPosition = 0;

        public WrapAdapter(ArrayList<View> mHeaderViews, ArrayList<View> mFootViews, Adapter mAdapter) {
            this.mAdapter = mAdapter;
            if (mHeaderViews == null) {
                this.mHeaderViews = EMPTY_INFO_LIST;
            } else {
                this.mHeaderViews = mHeaderViews;
            }
            if (mFootViews == null) {
                this.mFootViews = EMPTY_INFO_LIST;
            } else {
                this.mFootViews = mFootViews;
            }
        }

        /**
         * @param position 位置
         * @return 当前布局是否为Header
         */
        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        /**
         * @param position 位置
         * @return 当前布局是否为Footer
         */
        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - mFootViews.size();
        }

        /**
         * @return Header的数量
         */
        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        /**
         * @return Footer的数量
         */
        public int getFootersCount() {
            return mFootViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == RecyclerView.INVALID_TYPE) {
                return new HeaderViewHolder(mHeaderViews.get(headerPosition++));
            } else if (viewType == RecyclerView.INVALID_TYPE - 1) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(
                        StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);
                params.setFullSpan(true);
                mFootViews.get(0).setLayoutParams(params);
                return new HeaderViewHolder(mFootViews.get(0));
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return;
            }
            int adjPosition = position - numHeaders;
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mAdapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mAdapter != null) {
                return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            int numHeaders = getHeadersCount();
            if (position < numHeaders) {
                return RecyclerView.INVALID_TYPE;
            }
            int adjPosition = position - numHeaders;
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemViewType(adjPosition);
                }
            }
            return RecyclerView.INVALID_TYPE - 1;
        }

        @Override
        public long getItemId(int position) {
            int numHeaders = getHeadersCount();
            if (mAdapter != null && position >= numHeaders) {
                int adjPosition = position - numHeaders;
                int adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        private class HeaderViewHolder extends ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 刷新和加载更多数据的监听接口
     */
    public interface LoadDataListener {

        /**
         * 执行刷新
         */
        void onRefresh();

        /**
         * 执行加载更多
         */
        void onLoadMore();

    }


    public enum State {
        /**
         * 顶部
         */
        UP,
        /**
         * 底部
         */
        DOWN,
        /**
         * 正常
         */
        NORMAL
    }

    /**
     * 状态.
     */
    private State mState = State.NORMAL;

    ScalingRunnable mScalingRunnable;

    private final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    private void endScaling() {
        mScalingRunnable.startAnimation(200L);

    }

    int mActivePointerId = -1;
    private boolean isChange = false;

    private boolean isCan = true;

    public void setCan(boolean isCan) {
        this.isCan = isCan;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isCan) {
            return super.dispatchTouchEvent(ev);
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN://按下
                isMove = false;
                isTouching = true;
                downY = ev.getY();
                if (isEnablePull) {

                    mActivePointerId = ev.getPointerId(0);
                    if (headerImage != null) {
                        if (imageWidth == 0) {
                            imageWidth = headerImage.getWidth();
                            imageHeight = headerImage.getHeight();
                        }
                    }
                    if (!mScalingRunnable.isFinished()) {
                        mScalingRunnable.abortAnimation();
                    }
                }
            case MotionEvent.ACTION_MOVE://移动
                currentY = ev.getY();
                if (ev.findPointerIndex(mActivePointerId) != -1 && isEnablePull) {
                    if (scrollY < 0) {
                        if (lastY == -1.0f) {
                            lastY = ev.getY();
                            isTop = true;
                        }
                    }


                    float deltaY = currentY - lastY;
                    float changeY = currentY - downY;
                    if (changeY < -20 && mState == State.NORMAL && downY != -1.0) {
                        mState = State.UP;
                        isChange = false;
                    } else if (changeY > 20 && downY != -1.0 && (mState == State.NORMAL || mState == State.DOWN)) {
                        mState = State.DOWN;
                        if (isTop) {
                            isChange = true;
                        }
                    }
                    if (isChange && headerImage != null && scrollY < 0 && deltaY > 0) {
                        float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
                        float f = headerMoveHeight / imageHeight + 1;
                        ViewGroup.LayoutParams localParams = headerImage.getLayoutParams();
                        localParams.height = (int) (imageHeight * f);
                        localParams.width = imageWidth;
                        headerImage.setLayoutParams(localParams);
                        return true;
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (isEnablePull && headerImage != null) {
                    if (!showLoadingFirst) {
                        reset();
                        endScaling();
                    }
                }
                isTouching = false;
                if (headerImage.getLayoutParams().height > headerImageHeight &&
                        headerImage.getLayoutParams().height >= headerImageMaxHeight
                        && mLoadDataListener != null && !isLoadingData) {
                    //动画执行完之后再出现loading
                    if (showLoadingFirst) {
                        refresh();
                    } else {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        }, 200);
                    }
                } else {
                    reset();
                    endScaling();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    boolean isMove = false;

    private void reset() {
        this.lastY = -1.0F;
        downY = -1.0F;
        currentY = -1.0F;
        isChange = false;
        mActivePointerId = -1;
        mState = State.NORMAL;
    }

    float onInerceptDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            isMove = false;
            onInerceptDownY = e.getY();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            if (!isMove)
                isMove = Math.abs(e.getY() - onInerceptDownY) > 10;
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_OUTSIDE) {

            if (!isMove) {
                isMove = Math.abs(e.getY() - onInerceptDownY) > 10;
            }
            return isMove;
        }
        return super.onInterceptTouchEvent(e);
    }

    int imageWidth;
    int imageHeight;

    class ScalingRunnable implements Runnable {
        long mDuration;
        boolean mIsFinished = true;
        float mScale;
        long mStartTime;

        ScalingRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            float f2;
            ViewGroup.LayoutParams headLayoutParams;
            if ((!mIsFinished) && (mScale > 1.0D)) {
                float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
                f2 = mScale - (mScale - 1.0F) * sInterpolator.getInterpolation(f1);
                headLayoutParams = headerImage.getLayoutParams();
                if (f2 > 1.0F) {
                    headLayoutParams.width = imageWidth;
                    headLayoutParams.height = ((int) (f2 * imageHeight));
                    headerImage.setLayoutParams(headLayoutParams);
                    post(this);
                    return;
                }
                mState = State.NORMAL;
                mIsFinished = true;
            }
        }

        public void startAnimation(long paramLong) {
            mStartTime = SystemClock.currentThreadTimeMillis();
            mDuration = paramLong;
            mScale = ((float) (headerImage.getBottom()) / imageHeight);
            mIsFinished = false;
            post(this);
        }
    }

    public void setEnablePull(boolean isEnablePull) {
        this.isEnablePull = isEnablePull;
    }
}
