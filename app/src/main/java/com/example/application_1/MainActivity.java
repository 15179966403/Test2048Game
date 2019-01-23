package com.example.application_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int score=0;
    private TextView tvScore;
    private static MainActivity mainActivity=null;

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

    public void clearScore(){
        score=0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void addScore(int s){
        score+=s;
        showScore();
    }
}
