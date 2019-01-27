package com.example.application_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序主要界面，用于处理添加卡片
 */
public class GameView extends GridLayout {
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    /**
     * 用于初始化游戏界面
     */
    private void initGameView(){
        setColumnCount(4);          //设置游戏布局为4列
        setBackgroundColor(0xffbbada0);     //设置游戏的背景颜色
        //为该布局设置触摸监听
        setOnTouchListener(new OnTouchListener() {

            private float startX,startY,offsetX,offsetY;    //获取触摸位置的x,y坐标点，以及在x方向和y方向的偏移量

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){             //根据不同的触摸事件处理不同事情
                    case MotionEvent.ACTION_DOWN:       //手指按下，触摸屏幕
                        startX=event.getX();            //获取x坐标
                        startY=event.getY();            //获取y坐标
                        break;
                    case MotionEvent.ACTION_UP:         //手指抬起，离开屏幕
                        offsetX=event.getX()-startX;    //计算x方向的偏移量
                        offsetY=event.getY()-startY;    //计算y方向的偏移量

                        if (Math.abs(offsetX)>Math.abs(offsetY)){   //如果x方向上的绝对偏移量比y方向高，则认定用户在x方向上滑动
                            if (offsetX>5){         //提供5的滑动偏差，若偏移量小于5则认为用户无滑动行为
                                //处理向右边滑动事件
                                swipeRight();
                            }else if (offsetX<-5){
                                //处理向左滑动事件
                                swipeLeft();
                            }
                        }else{                  //y方向的偏移量比x方向高，认定用户在y方向上滑动
                            if (offsetY>5){
                                //处理向下滑动事件
                                swipeDown();
                            }else if (offsetY<-5){
                                //处理向上滑动事件
                                swipeUp();
                            }
                        }

                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    /**
     * 该函数在程序第一次运行时调用，可在该函数中获取手机的宽和高
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth=(Math.min(w,h)-10)/4;         //取宽高中的最小值，留下10像素空袭
        addCards(cardWidth,cardWidth);              //添加游戏小卡片
        startGame();                                //开始游戏
    }

    /**
     * 添加游戏卡片
     * @param cardWidth 卡片的宽度
     * @param cardHeight 卡片的高度
     */
    private void addCards(int cardWidth,int cardHeight){
        smallCard c;            //定义游戏小卡片控件
        GridLayout.LayoutParams lp;     //GridLayou辅助设定边距和父容器关系
        GridLayout.Spec rowSpec,columnSpec;     //GridLayout添加子View时需要指定Spec
        for (int y=0;y<4;y++){          //从第一列开始
            for (int x=0;x<4;x++){      //从第一行开始
                c=new smallCard(this.getContext());     //初始化
                c.setNum(0);            //默认卡片中的数字为0
                rowSpec=GridLayout.spec(y,1.0f);        //设置卡片y方向上的位置，以及相应比重
                columnSpec=GridLayout.spec(x,1.0f);     //设置卡片x方向上的位置，以及相应比重
                lp=new GridLayout.LayoutParams(rowSpec,columnSpec);     //将设置好的信息传入LayoutParams中
                addView(c,lp);          //添加子View，以及相应的格式

                cardMaps[x][y]=c;       //记录小卡片在数组中的位置
            }
        }
    }

    /**
     * 开始游戏
     */
    private void startGame(){
        MainActivity.getMainActivity().clearScore();    //调取MainActivity中的方法，将积分归零

        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                cardMaps[x][y].setNum(0);               //为每一个小卡片设置成默认显示0
            }
        }

        addRandomNumber();          //在随机位置添加卡片
        addRandomNumber();
    }

    /**
     * 在随机位置添加卡片的方法
     */
    private void addRandomNumber(){

        emptyPoints.clear();            //将空卡片的数组储存清空
        //重新获得空卡片的位置
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                if (cardMaps[x][y].getNum()<=0){
                    emptyPoints.add(new Point(x,y));        //将空卡片位置记录
                }
            }
        }
        //在所有空卡片中，随机移除一张卡片，并将其设置成2或者4
        Point p=emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardMaps[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }

    /**
     * 处理向左滑动事件
     */
    private void swipeLeft(){
        boolean merga=false;        //判断是否有卡片位置发生改变，默认没有发生位置改变

        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){

                for (int x1=x+1;x1<4;x1++){     //判断x，y位置右边的卡片能否与自己合并
                    if (cardMaps[x1][y].getNum()>0){        //右边相邻位置不是空卡片，如果是空卡片则继续检查下一张卡片
                        if (cardMaps[x][y].getNum()<=0){    //若x，y位置上的卡片为空，则将右边相邻位置卡片向左边移动
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);

                            x--;            //再次在x，y位置重新检查一次，确保移动之后，右边相邻卡片为空
                            merga=true;     //发生移动
                        }else if (cardMaps[x][y].equals(cardMaps[x1][y])){      //如果两张卡片的值相同，则进行合并
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());       //合并成功，产生积分，通知MainActivity刷新积分
                            merga=true;     //发生移动
                        }
                        break;
                    }
                }
            }
        }
        //产生移动，在空位置添加随机卡片，并判断游戏是否结束
        if (merga){
            addRandomNumber();
            checkComplete();
        }
    }

    private void swipeRight(){

        boolean merga=false;

        for (int y=0;y<4;y++){
            for (int x=3;x>=0;x--){

                for (int x1=x-1;x1>=0;x1--){
                    if (cardMaps[x1][y].getNum()>0){
                        if (cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);

                            x++;
                            merga=true;
                        }else if (cardMaps[x][y].equals(cardMaps[x1][y])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merga=true;
                        }
                        break;
                    }
                }
            }
        }
        if (merga) {
            addRandomNumber();
            checkComplete();
        }
    }

    private void swipeUp(){

        boolean merga=false;

        for (int x=0;x<4;x++){
            for (int y=0;y<4;y++){

                for (int y1=y+1;y1<4;y1++){
                    if (cardMaps[x][y1].getNum()>0){
                        if (cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);

                            y--;
                            merga=true;
                        }else if (cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merga=true;
                        }
                        break;
                    }
                }
            }
        }
        if (merga) {
            addRandomNumber();
            checkComplete();
        }
    }

    private void swipeDown(){
        boolean merga=false;

        for (int x=0;x<4;x++){
            for (int y=3;y>=0;y--){

                for (int y1=y-1;y1>=0;y1--){
                    if (cardMaps[x][y1].getNum()>0){
                        if (cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x][y1].getNum());
                            cardMaps[x][y1].setNum(0);

                            y++;
                            merga=true;
                        }else if (cardMaps[x][y].equals(cardMaps[x][y1])){
                            cardMaps[x][y].setNum(cardMaps[x][y].getNum()*2);
                            cardMaps[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardMaps[x][y].getNum());
                            merga=true;
                        }
                        break;
                    }
                }
            }
        }
        if (merga) {
            addRandomNumber();
            checkComplete();
        }
    }

    /**
     * 判断游戏是否结束。当没有可以进行合并的相邻卡片，以及没有空位置时，则游戏结束。
     */
    private void checkComplete(){
        boolean complete =true;         //默认游戏结束

        All:
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                if (cardMaps[x][y].getNum()==0||
                        (x>0&&cardMaps[x][y].equals(cardMaps[x-1][y]))||
                        (x<3&&cardMaps[x][y].equals(cardMaps[x+1][y]))||
                        (y>0&&cardMaps[x][y].equals(cardMaps[x][y-1]))||
                        (y<3&&cardMaps[x][y].equals(cardMaps[x][y+1]))){
                    complete=false;     //存在相邻位置可以合并的卡片，或者有空位置，则不结束游戏
                    break All;          //跳出整个循环
                }
            }
        }
        //如果游戏结束，弹出对话框，重新开始游戏
        if (complete){
            new AlertDialog.Builder(getContext()).setTitle("您好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }

    private smallCard[][] cardMaps=new smallCard[4][4];         //记录卡片位置
    private List<Point> emptyPoints=new ArrayList<Point>();     //卡片位置坐标
}
