package com.example.lpc.android_pulltorefresh_main.interfaceee;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * Description: 加载布局的回调
 * Created by lpc on 2016/5/31.
 */
public interface ILoadingLayout {


    //设置最新更新的文本
    public void setLastUpdatedLabel(CharSequence label);

    //设置drawable在使用加载布局的时候
    public void setLoadingDrawable(Drawable drawable);

    //向下拉动时的label
    public void setPullLabel(CharSequence label);

    //正在刷新时的label
    public void setRefreshingLabel(CharSequence label);

    //释放时的label
    public void setReleaseLabel(CharSequence releaseLabel);

    //设置文本被展示时的字体和风格
    public void setTextTypeface(Typeface textType);
}
