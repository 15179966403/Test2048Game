package com.example.application_1;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 游戏卡片类
 */
public class smallCard extends FrameLayout {
    private int num=0;      //设置卡片中显示的数字
    private TextView label;     //卡片中的文字控件

    public smallCard(Context context) {
        super(context);
        label=new TextView(getContext());       //实例化
        label.setTextSize(32);                  //设置字体大小
        label.setGravity(Gravity.CENTER);       //设置对齐方式为居中对齐
        label.setBackgroundColor(0x33ffffff);   //设置背景颜色

        LayoutParams lp=new LayoutParams(-1,-1);      //设置控件在父容器中的设定
        lp.setMargins(10,10,0,0);           //设置间距
        addView(label,lp);              //添加视图

        setNum(0);                      //设置数字
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num<=0){        //当数字为0时，显示空内容
            label.setText("");
        }else{
            label.setText(num+"");
        }
    }

    /**
     * 判断两张卡片内容是否相同
     * @param obj 需要比较的卡片对象
     * @return 如果为true，则表示两张卡片数字相同，如果为false，则两张卡片数字不相同
     */
    public boolean equals(smallCard obj){
        return getNum()==obj.getNum();
    }
}
