package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

/**
 * Created by terrysong on 2017/2/18.
 *
 * 参考鸿洋大神博客
 * http://blog.csdn.net/lmj623565791/article/details/24500107
 *
 * 比鸿洋大神的多了进度值
 */

public class CustomProgressBar extends View {

    /**
     * 进度条文本大小
     */
    private int progressTextSize;
    /**
     * 第一进度颜色
     */
    private int firstColor;
    /**
     * 第二进度颜色
     */
    private int secondColor;
    /**
     * 环形宽度
     */
    private int circleWidth;

    private Paint mPaint;

    /**
     * 进度文本
     */
    private String progressText = "1%";

    /**
     * 当前进度
     */
    private int mProgress;
    /**
     * 最大进度值
     */
    private int maxProgress;
    /**
     * 是否开始
     */
    private boolean isNext = false;

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    public CustomProgressBar(Context context) {
        this(context,null);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar,defStyleAttr,0);

        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomProgressBar_firstColor:
                    firstColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    secondColor = a.getColor(attr,Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    circleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_progressTextSize:
                    progressTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgressBar_maxProgress:
                    maxProgress = a.getInteger(attr,100);
                    break;
            }
        }

        a.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(progressTextSize);

        /**
         * 绘制线程
         *//*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    mProgress++;
                    if(mProgress == 360){
                        String threadName = Thread.currentThread().getName();
                        Log.e("--Main--", "ThreadName: "+ threadName);
                        postInvalidate();
                        break;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
//        super.onDraw(canvas);

        int cx = getWidth()/2;//圆心x坐标
        int cy = getHeight()/2;//圆心y坐标
        //减去getPaddingLeft()的值
        int cr = getWidth()/2 - circleWidth/2-getPaddingLeft();//圆半径

        RectF rectF = new RectF(cx-cr,cy-cr,cx+cr,cy+cr);
        if(!isNext){
            mPaint.setStrokeWidth(circleWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(firstColor);
            canvas.drawCircle(cx,cy,cr,mPaint);
            mPaint.setColor(secondColor);
            isNext = false;

            mPaint.setStrokeWidth(1);
            canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,mPaint);
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(circleWidth);
        canvas.drawArc(rectF,-90,1.0f*360*mProgress/maxProgress,false,mPaint);

        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        progressText = getProgressText(mProgress);
        float textWidth = mPaint.measureText(progressText);

        /**
         * 精确的测量文本的高度
         */
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        canvas.drawText(progressText,cx-textWidth/2,cx-fm.descent+(fm.descent-fm.ascent)/2,mPaint);

    }

    private String getProgressText(int progress){
        if(progress==0)
            return  "1%";
        else
            return (int)(mProgress/(maxProgress*1.0f)*100) + "%";
    }
}
