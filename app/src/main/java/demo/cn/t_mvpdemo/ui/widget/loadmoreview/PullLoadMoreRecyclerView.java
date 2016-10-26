package demo.cn.t_mvpdemo.ui.widget.loadmoreview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.RxManage;

/**
 * Created by WuXiaolong on 2015/7/2.
 * github:https://github.com/WuXiaolong/PullLoadMoreRecyclerView
 * weibo:http://weibo.com/u/2175011601
 * 微信公众号：AndroidProgrammer
 */
@SuppressWarnings("unused")
public class PullLoadMoreRecyclerView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean pullRefreshEnable = true;
    private boolean pushRefreshEnable = true;
    private View mFooterView;
    private LinearLayout mEmptyViewContainer;
    private Context mContext;
    private TextView loadMoreText;
    private AdapterDataObserver mEmptyDataObserver;

    private RxManage rxManage;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
        initAction();
    }

    public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAction();
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pull_loadmore_layout, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll(this));

        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());

        mFooterView = view.findViewById(R.id.footerView);
        mEmptyViewContainer = (LinearLayout) view.findViewById(R.id.emptyView);

        loadMoreText = (TextView) view.findViewById(R.id.listview_footer_hint_textview);

        mFooterView.setVisibility(View.GONE);
        mEmptyViewContainer.setVisibility(View.GONE);

        this.addView(view);

    }

    public void initAction(){

        rxManage=new RxManage();

    }


    /**
     * LinearLayoutManager
     */
    public PullLoadMoreRecyclerView setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    /**
     * GridLayoutManager
     */

    public PullLoadMoreRecyclerView setGridLayout(int spanCount) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        return this;
    }


    /**
     * StaggeredGridLayoutManager
     */

    public PullLoadMoreRecyclerView setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        return this;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }


    public PullLoadMoreRecyclerView setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
            if (mEmptyDataObserver == null) {
                mEmptyDataObserver = new AdapterDataObserver();
            }
            adapter.registerAdapterDataObserver(mEmptyDataObserver);
        }
        return this;
    }

    public PullLoadMoreRecyclerView showEmptyView() {

        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null && mEmptyViewContainer.getChildCount() != 0) {
            if (adapter.getItemCount() == 0) {
                mFooterView.setVisibility(View.GONE);
                mEmptyViewContainer.setVisibility(VISIBLE);
            } else {
                mEmptyViewContainer.setVisibility(GONE);
            }
        }
        return this;

    }

    /**
     * 是否可以下拉
     * @param enable
     */
    public PullLoadMoreRecyclerView setPullRefreshEnable(boolean enable) {
        pullRefreshEnable = enable;
        setSwipeRefreshEnable(enable);
        return this;
    }

    public boolean getPullRefreshEnable() {
        return pullRefreshEnable;
    }

    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public boolean getSwipeRefreshEnable() {
        return mSwipeRefreshLayout.isEnabled();
    }


    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (pullRefreshEnable)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });

    }

    /**
     * When view detached from window , unregister adapter data observer, avoid momery leak.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(mEmptyDataObserver);
        }
    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return isRefresh || isLoadMore;
        }
    }

    /**
     * This Observer receives adapter data change.
     * When adapter's item count greater than 0 and empty view has been set,then show the empty view.
     * when adapter's item count is 0 ,then empty view hide.
     */
    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            showEmptyView();
        }
    }

    public boolean getPushRefreshEnable() {
        return pushRefreshEnable;
    }

    /**
     * 是否可以上拉
     * @param pushRefreshEnable
     */
    public PullLoadMoreRecyclerView setPushRefreshEnable(boolean pushRefreshEnable) {
        this.pushRefreshEnable = pushRefreshEnable;
        return this;
    }

    public PullLoadMoreRecyclerView setFooterViewText(CharSequence text) {
        loadMoreText.setText(text);
        return this;
    }

    public PullLoadMoreRecyclerView setFooterViewText(int resid) {
        loadMoreText.setText(resid);
        return this;
    }

    public PullLoadMoreRecyclerView setEmptyView(View emptyView) {
        mEmptyViewContainer.removeAllViews();
        mEmptyViewContainer.addView(emptyView);
        return this;
    }

    public void refresh() {
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

    public void loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            mFooterView.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFooterView.setVisibility(View.VISIBLE);
                        }
                    })
                    .start();
            invalidate();
            mPullLoadMoreListener.onLoadMore();

        }
    }


    public void setPullLoadMoreCompleted() {
        isRefresh = false;
        setRefreshing(false);

        isLoadMore = false;
        mFooterView.animate()
                .translationY(mFooterView.getHeight())
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }


    public PullLoadMoreRecyclerView setOnPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
        return this;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface PullLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }
}
