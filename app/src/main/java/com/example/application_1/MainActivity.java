package com.example.application_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int score=0;        //设置游戏积分
    private TextView tvScore;       //游戏积分显示控件
    private static MainActivity mainActivity=null;      //为方便其他类可以调用MainActivity初始化

    public MainActivity(){
        mainActivity=this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore=findViewById(R.id.tvScore);
    }

    /**
     * 将积分清零
     */
    public void clearScore(){
        score=0;
        showScore();
    }

    /**
     * 设置积分
     */
    public void showScore(){
        tvScore.setText(score+"");
    }

    /**
     * 在原有的积分上添加积分
     * @param s 获得积分
     */
    public void addScore(int s){
        score+=s;
        showScore();
    }
}
