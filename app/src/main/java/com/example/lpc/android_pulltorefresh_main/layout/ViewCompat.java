package com.example.lpc.android_pulltorefresh_main.layout;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Description: 视图兼容
 * Created by lpc on 2016/5/31.
 */
public class ViewCompat {
    public static void postOnAnimation(View view, Runnable runnable){
        //public static final int JELLY_BEAN = 16;  sdk版本是16以上的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            SDK16.postOnAnimation(view,runnable);
        }else{
            view.postDelayed(runnable,16);
        }
    }

    public static void setBackground(View view,Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            SDK16.setBackground(view,drawable);
        }else{
            view.setBackgroundDrawable(drawable);
        }
    }

    public static void setLayerType(View view,int layerType){
        //public static final int HONEYCOMB = 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SDK11.setLayerType(view,layerType);
        }
    }


    static class SDK11{

        public static void setLayerType(View view,int layerType){
            view.setLayerType(layerType,null);
        }
    }


    static class SDK16{

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static void postOnAnimation(View view, Runnable runnable){
            view.postOnAnimation(runnable);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static void setBackground(View view, Drawable background){
            view.setBackground(background);
        }

    }
}
