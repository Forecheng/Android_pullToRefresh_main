package com.example.lpc.android_pulltorefresh_main.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.lpc.android_pulltorefresh_main.R;

/**
 * ListView上滑加载数据
 *
 * Created by lpc on 2016/5/11.
 */
@TargetApi(Build.VERSION_CODES.M)
public class LoadListView  extends ListView implements AbsListView.OnScrollListener{

    private View footer;
    private int mTotalItemCount;    //总数量
    private int mLastItem;          // 最后可见的item
    private boolean isLoading;      //是否正在加载
    private ILoadMoreDataListener loadMoreDataListener;

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottonLayout(context);
    }

    /**
     * 添加底部布局
     * */
    public void initBottonLayout(Context context)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        footer = layoutInflater.inflate(R.layout.loading_footer,null);
        footer.findViewById(R.id.loading).setVisibility(View.GONE);   //该开始将该布局隐藏
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if((mTotalItemCount == mLastItem) && scrollState == SCROLL_STATE_IDLE)
        {
            if (!isLoading)
            {
                isLoading = true;
                //使加载布局可见
                footer.findViewById(R.id.loading).setVisibility(View.VISIBLE);
                //加载更多数据
                loadMoreDataListener.onLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mLastItem = firstVisibleItem + visibleItemCount;
        this.mTotalItemCount = totalItemCount;
    }


    /**
     * 加载完毕
     * */
    public void loadComplete()
    {
        isLoading = false;
        footer.findViewById(R.id.loading).setVisibility(View.GONE);
    }

    public void setInterface(ILoadMoreDataListener loadMoreDataListener)
    {
        this.loadMoreDataListener = loadMoreDataListener;
    }
    /**
     * 加载更多数据的回调接口
     * 1、获取更多的数据
     * 2、更新ListView
     * MainActivity 实现该接口
     * */
    public interface ILoadMoreDataListener{
        public void onLoad();
    }
}

