package com.sensu.android.zimaogou.utils;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * Created by zhangwentao on 2015/11/30.
 */
public class TextUtils {

    public static void addLineCenter(TextView textView) {
        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        textView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//抗锯齿
    }

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
