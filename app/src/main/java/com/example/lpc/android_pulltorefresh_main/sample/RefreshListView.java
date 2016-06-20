package com.example.lpc.android_pulltorefresh_main.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lpc.android_pulltorefresh_main.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 自定义下拉刷新ListView组件
 * Created by lpc on 2016/6/2.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    private View mHeader;       //顶部的布局
    private int mTopHeight;     //视图距离顶部的高度
    private int mFirstVisibleItem;   //第一个可见的item的位置
    private int mScrollState;   //滚动的状态
    private boolean isRemark;
    private int mStartY;            //记录手指第一次按下时Y坐标

    //下拉刷新的状态
    private int mState;    //用来保存当前的状态
    private static final int STATE_NORMAL = 0;    //正常状态
    private static final int STATE_PULL = 1;      //下拉状态
    private static final int STATE_RELEASE = 2;   //释放状态
    private static final int STATE_REFRESH = 3;   //刷新状态

    private IRefreshMoreData mRefreshMoreData;

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
        this.mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mFirstVisibleItem = firstVisibleItem;
    }

    //添加顶部加载的布局
    public void initViews(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        mHeader = inflater.inflate(R.layout.refresh_header,null);

        measureView(mHeader);
        mTopHeight = mHeader.getMeasuredHeight();
        paddingTop(-mTopHeight);
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

    //设置下拉刷新布局的上边距
    public void paddingTop(int topPadding){
        mHeader.setPadding(mHeader.getPaddingLeft(),topPadding,mHeader.getPaddingRight(),mHeader.getPaddingBottom());
        mHeader.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //根据触摸的动作进行不同的行为
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:   //手指按下时
                if (mFirstVisibleItem == 0){   //按下第一个可见项时
                    isRemark = true;
                    mStartY = (int) motionEvent.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:    //手指在屏幕上移动时
                onMove(motionEvent);
                break;
            case MotionEvent.ACTION_UP:      //手指抬起时
                if (mState == STATE_RELEASE){
                    mState = STATE_REFRESH ;   //抬起之后将状态改变为刷新
                    //状态改变
                    refreshViewByState();
                    //此时加载刷新数据
                    mRefreshMoreData.onFresh(); //回调显示刷新的数据
                }else if(mState == STATE_PULL){  //表示正在下拉时抬起手指
                    mState = STATE_NORMAL;
                    refreshViewByState();
                    isRemark = false;
                    //状态改变
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void onMove(MotionEvent motionEvent){
        if (!isRemark){
            return;
        }

        int tempY = (int) motionEvent.getY();
        int space = tempY - mStartY;
        int topPadding = space - mTopHeight;   //
        switch (mState){
            case STATE_NORMAL:
                if (space > 0){
                    mState = STATE_PULL;
                    refreshViewByState();
                }
                break;
            case STATE_PULL:
                paddingTop(topPadding);   //拉动过程中，刷新视图跟着手指向下移动
                if (space > mTopHeight + 50 && mScrollState == SCROLL_STATE_TOUCH_SCROLL){
                    mState = STATE_RELEASE;
                    refreshViewByState();
                }
                break;
            case STATE_RELEASE:
                paddingTop(topPadding);
                if (space < mTopHeight + 50){
                    mState = STATE_PULL;
                    refreshViewByState();
                }
                break;
            case STATE_REFRESH:
                break;
        }
    }

    //根据状态的改变变化视图
    public void refreshViewByState(){
        TextView tip = (TextView)mHeader.findViewById(R.id.text_tip);
        ImageView arrow = (ImageView)mHeader.findViewById(R.id.pull_image);
        ProgressBar progressBar = (ProgressBar)mHeader.findViewById(R.id.progress);

        //下拉时箭头的改变动画：下拉时向下，释放时向上，刷新时箭头隐藏并显示刷新进度条
        RotateAnimation animation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5F,RotateAnimation.RELATIVE_TO_SELF,0.5F);
        animation.setDuration(500);
        animation.setFillAfter(true);
        RotateAnimation animation1 = new RotateAnimation(180,0,RotateAnimation.RELATIVE_TO_SELF,0.5F,RotateAnimation.RELATIVE_TO_SELF,0.5F);
        animation1.setDuration(500);
        animation1.setFillAfter(true);

        switch (mState){
            case STATE_NORMAL:
                arrow.clearAnimation();
                paddingTop(-mTopHeight);
                break;
            case STATE_PULL:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("下拉进行刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation1);
                break;
            case STATE_REFRESH:
                paddingTop(50);
                arrow.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tip.setText("正在刷新");
                arrow.clearAnimation();
                break;
            case STATE_RELEASE:
                arrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tip.setText("释放进行刷新");
                arrow.clearAnimation();
                arrow.setAnimation(animation);
                break;
        }
    }

    //刷新结束后
    public void refreshFinish(){
        mState = STATE_NORMAL;
        isRemark = false;
        refreshViewByState();
        TextView updateTime = (TextView)mHeader.findViewById(R.id.text_last_update);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        updateTime.setText(format.format(new Date(System.currentTimeMillis())));
    }

    public void setRefreshData(IRefreshMoreData iRefreshMoreData){
        this.mRefreshMoreData = iRefreshMoreData;
    }

    public interface IRefreshMoreData{
        public void onFresh();
    }


}
