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

    private void initGameView(){
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);

        setOnTouchListener(new OnTouchListener() {

            private float startX,startY,offsetX,offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX=event.getX();
                        startY=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX=event.getX()-startX;
                        offsetY=event.getY()-startY;

                        if (Math.abs(offsetX)>Math.abs(offsetY)){
                            if (offsetX>5){
                                //向右边滑动
                                swipeRight();
                            }else if (offsetX<-5){
                                //向左滑动
                                swipeLeft();
                            }
                        }else{
                            if (offsetY>5){
                                //向下滑动
                                swipeDown();
                            }else if (offsetY<-5){
                                //向上移动
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth=(Math.min(w,h)-10)/4;
        addCards(cardWidth,cardWidth);
        startGame();
    }

    private void addCards(int cardWidth,int cardHeight){
        smallCard c;
        GridLayout.LayoutParams lp;
        GridLayout.Spec rowSpec,columnSpec;
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                c=new smallCard(this.getContext());
                c.setNum(0);
                rowSpec=GridLayout.spec(y,1.0f);
                columnSpec=GridLayout.spec(x,1.0f);
                lp=new GridLayout.LayoutParams(rowSpec,columnSpec);
                addView(c,lp);

                cardMaps[x][y]=c;
            }
        }
    }

    private void startGame(){
        MainActivity.getMainActivity().clearScore();

        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                cardMaps[x][y].setNum(0);
            }
        }

        addRandomNumber();
        addRandomNumber();
    }

    private void addRandomNumber(){

        emptyPoints.clear();

        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                if (cardMaps[x][y].getNum()<=0){
                    emptyPoints.add(new Point(x,y));
                }
            }
        }

        Point p=emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardMaps[p.x][p.y].setNum(Math.random()>0.1?2:4);
    }

    private void swipeLeft(){
        boolean merga=false;

        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){

                for (int x1=x+1;x1<4;x1++){
                    if (cardMaps[x1][y].getNum()>0){
                        if (cardMaps[x][y].getNum()<=0){
                            cardMaps[x][y].setNum(cardMaps[x1][y].getNum());
                            cardMaps[x1][y].setNum(0);

                            x--;
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

    private void checkComplete(){
        boolean complete =true;

        All:
        for (int y=0;y<4;y++){
            for (int x=0;x<4;x++){
                if (cardMaps[x][y].getNum()==0||
                        (x>0&&cardMaps[x][y].equals(cardMaps[x-1][y]))||
                        (x<3&&cardMaps[x][y].equals(cardMaps[x+1][y]))||
                        (y>0&&cardMaps[x][y].equals(cardMaps[x][y-1]))||
                        (y<3&&cardMaps[x][y].equals(cardMaps[x][y+1]))){
                    complete=false;
                    break All;
                }
            }
        }
        if (complete){
            new AlertDialog.Builder(getContext()).setTitle("您好").setMessage("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }

    private smallCard[][] cardMaps=new smallCard[4][4];
    private List<Point> emptyPoints=new ArrayList<Point>();
}
