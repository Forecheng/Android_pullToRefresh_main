package com.example.lpc.android_pulltorefresh_main.library.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.lpc.android_pulltorefresh_main.R;

/**
 * Description: 自定义刷新listview组件
 * Created by lpc on 2016/6/2.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View mHeader;    //顶部的布局

    private int mScrollState;   //滚动的状态
    private int topPadding = 0;


    public RefreshListView(Context context) {
        super(context);
        initViews(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //添加顶部加载的布局
    public void initViews(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.refresh_header,null);

        measureView(mHeader);
        topPadding = mHeader.getMeasuredHeight();
        paddingTop(-topPadding);
        this.addHeaderView(mHeader);
        this.setOnScrollListener(this);

    }


    //测量顶部视图的宽度和高度
    public void measureView(View view){
        ViewGroup.LayoutParams params = view.getLayoutParams();  //得到顶部布局的参数
        if (params == null){
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int width = ViewGroup.getChildMeasureSpec(0,0,params.width);
        int tempHeight = params.height;
        int height = 0;
        if (tempHeight > 0){
            height = MeasureSpec.makeMeasureSpec(tempHeight,MeasureSpec.EXACTLY);
        }else{
            height = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
        }

        view.measure(width,height);
    }

    //
    public void paddingTop(int topPadding){
        mHeader.setPadding(mHeader.getPaddingLeft(),topPadding,mHeader.getPaddingRight(),mHeader.getPaddingBottom());
        mHeader.invalidate();
    }

}
