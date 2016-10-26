package demo.cn.t_mvpdemo.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.cn.t_mvpdemo.R;
import demo.cn.t_mvpdemo.base.BaseEntity;
import demo.cn.t_mvpdemo.base.BaseViewHolder;
import demo.cn.t_mvpdemo.base.Data;
import demo.cn.t_mvpdemo.base.RxManage;
import demo.cn.t_mvpdemo.base.Util.LogUtil;
import demo.cn.t_mvpdemo.base.Util.TUtil;
import demo.cn.t_mvpdemo.ui.view.viewholder.ListFooterVH;
import rx.functions.Action1;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class TRecyclerView<T extends BaseEntity.ListBean> extends LinearLayout {
    private Context context;
    private SwipeRefreshLayout swipeRefresh;
    private String TAG="TRecyclerView";
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private LinearLayoutManager layoutManager;
    private boolean isRefreshable = true, isEmpty,isHasHeader;
    private int lastVisibleItem;
    private RecyclerAdapter<T> adapter;
    private int index = 0;
    private T model;
    private Map<String, String> params = new HashMap<>();
    public RxManage rxManage = new RxManage();

    public TRecyclerView(Context context) {
        super(context);
        initView(context);
        initAction();
    }

    public TRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAction();
    }

    public TRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAction();
    }

    public void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list_recyclerview, this, true);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swip_refresh);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

    }

    private void initAction() {
        swipeRefresh.setColorSchemeColors(
                new int[]{android.R.color.holo_blue_bright
                        , android.R.color.holo_orange_light});
        swipeRefresh.setEnabled(isRefreshable);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecyclerAdapter<>(context);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter() != null
                        && newState == RecyclerView.SCROLL_STATE_DRAGGING
                        && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()
                        && adapter.isHasMore) {
                    fetch();
                }
            }
        });

        emptyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reFetch();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rxManage.clear();
    }

    public RecyclerAdapter getAdapter() {
        return adapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TRecyclerView setIsRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
        swipeRefresh.setEnabled(isRefreshable);
        return this;
    }

    public TRecyclerView setHeadView(Class<? extends BaseViewHolder> cla,Object headdate) {
        if (cla==null){
            isHasHeader=false;
            adapter.setHeadViewType(0,cla,null);
        }else {
            try {
                int mHeadViewType=cla.getConstructor(View.class)
                        .newInstance(new View(context)).getType();
                adapter.setHeadViewType(mHeadViewType,cla,headdate);
                isHasHeader=true;
            }catch (Exception e){
                LogUtil.e(TAG,e.getMessage());
            }
        }
        return this;
    }

    public TRecyclerView setFooterView() {
        return this;
    }

    public TRecyclerView setView(Class<? extends BaseViewHolder<T>> cla) {
        try {
            BaseViewHolder mVH = cla.getConstructor(View.class).newInstance(new View(context));
            this.model = TUtil.getT(cla, 0);
            adapter.setViewType(mVH, cla);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void setEmpty() {
        if (!isEmpty) {
            isEmpty = true;
            emptyView.setVisibility(VISIBLE);
            swipeRefresh.setVisibility(GONE);
        }
    }


    public void reFetch() {
        index = 1;
        swipeRefresh.setRefreshing(true);
        fetch();
    }

    public void fetch() {
        if (isEmpty) {
            emptyView.setVisibility(GONE);
            swipeRefresh.setVisibility(VISIBLE);
        }
        if (model == null) {
            LogUtil.e(TAG, "model null");
            return;
        }

        model.setParam(params);
        rxManage.add(model.getPageAt(index)
                .subscribe(new Action1<Data<T>>() {
                    @Override
                    public void call(Data<T> data) {
                        swipeRefresh.setRefreshing(false);
                        adapter.setBeans(data.results,index);
                        index++;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        setEmpty();
                    }
                }));
    }

    public TRecyclerView setData(List<T> data) {
        if (isEmpty) {
            emptyView.setVisibility(GONE);
            swipeRefresh.setVisibility(VISIBLE);
        }
        adapter.setBeans(data,1);
        return this;
    }

    public TRecyclerView setParam(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public class UpDateData {
        public int i;
        public T obj;

        public UpDateData(int i, T obj) {
            this.i = i;
            this.obj = obj;
        }
    }

    class RecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

        private List<T> list = new ArrayList<>();
        private int viewType, isHasFooter = 1, isHasHeader = 0, mHeaderViewType;
        public Object mHeadData;
        public Class<? extends BaseViewHolder> mItemViewClass, mHeadViewClass, mFooterViewClass;
        public int mFooterViewType = ListFooterVH.LAYOUT_TYPE;
        private Context context;
        public boolean isHasMore;

        public void setViewType(BaseViewHolder bVH, Class<? extends BaseViewHolder> cla) {
            this.isHasMore = true;
            this.viewType = bVH.getType();
            this.list = new ArrayList<>();
            this.mItemViewClass = cla;
            notifyDataSetChanged();
        }

        public void setHeadViewType(int i, Class<? extends BaseViewHolder> cla, Object data) {
            if (cla == null) {
                this.isHasHeader = 0;
            } else {
                this.isHasHeader = 1;
                this.mHeaderViewType = i;
                this.mHeadViewClass = cla;
                this.mHeadData = data;
            }
        }

        public void setHeadViewData(Object data) {
            this.mHeadData = data;
        }

        public void setFooterViewType(int i, Class<? extends BaseViewHolder> cla) {
            this.mFooterViewType = i;
            this.mFooterViewClass = cla;
            this.list = new ArrayList<>();
            notifyDataSetChanged();
        }

        public void setBeans(List<T> datas, int index) {
            if (datas == null)
                return;
            isHasMore = datas.size() >= 10;
            if (index > 1) {
                list.addAll(datas);
            } else {
                list.clear();
                list.addAll(datas);
            }
            notifyDataSetChanged();
        }

        public RecyclerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            return isHasHeader == 1 ? (position == 0 ? mHeaderViewType
                    : (position + 1 == getItemCount() ? mFooterViewType : viewType))
                    : (position + 1 == getItemCount() ? mFooterViewType : viewType);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            try {
                boolean isFoot = viewType == mFooterViewType;
                return viewType == mHeaderViewType ? mHeadViewClass.getConstructor(View.class)
                        .newInstance(
                                LayoutInflater.from(context).inflate(mHeaderViewType, parent, false)
                        ) : (isFoot ? mFooterViewClass : mItemViewClass)
                        .getConstructor(View.class).newInstance(
                                LayoutInflater.from(context).inflate(isFoot ? mFooterViewType : viewType, parent, false)
                        );
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                return null;
            }

        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.onBindHolder(holder.itemView, position + 1 == getItemCount()
                    ? (isHasMore ? new Object() : null) : isHasHeader == 1 && position == 0
                    ? mHeadData : list.get(position - isHasHeader));
        }

        @Override
        public int getItemCount() {
            return list.size() + isHasHeader + isHasFooter;
        }

        public void removeItem(int position){
            if (position<0||position>=list.size())
                return;
            list.remove(position);
            notifyItemRemoved(position);
        }

        public void updateItem(int position,T item){
            if (position<0||position>=list.size())
                return;
            list.remove(position);
            list.add(position, item);
            notifyItemChanged(position);
        }

    }
}
