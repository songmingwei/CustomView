package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.customview.R;


/**
 * Created by terrysong on 2017/2/19.
 *
 * 自定义音量控制View
 */

public class CustomVolumeControlBar extends View{

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 第一圈颜色
     */
    private int firstColor;

    /**
     * 第二圈颜色
     */
    private int secondColor;
    /**
     * 环形宽度
     */
    private int circleWidth;
    /**
     * 每个块块间隔距离
     */
    private int splitSize;
    /**
     * 总数
     */
    private int dotCount;

    /**
     * 当前进度
     */
    private int mCurrentCount = 3;
    /**
     * 中间的图片
     */
    private Bitmap bgBitmap;

    private Rect mRect;
    private Rect mBgRect;


    public CustomVolumeControlBar(Context context) {
        this(context,null);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("--Main--", "CustomVolumeControlBar: 构造器执行！" );

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomVolumeControlBar,defStyleAttr,0);

        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomVolumeControlBar_firstColor:
                    firstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomVolumeControlBar_secondColor:
                    secondColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomVolumeControlBar_circleWidth:
                    circleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumeControlBar_splitSize:
                    splitSize = a.getInteger(attr,4);
                    break;
                case R.styleable.CustomVolumeControlBar_dotCount:
                    dotCount = a.getInteger(attr,15);
                    break;
                case R.styleable.CustomVolumeControlBar_background:
                    bgBitmap = BitmapFactory.decodeResource(getResources(),a.getResourceId(attr,0));
                    break;
            }
        }

        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
        mBgRect = new Rect();
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStrokeWidth(circleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("--Main--", "onMeasure: 执行！" );
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        Log.e("--Main--", "onDraw: 执行！" );

        int centre = getWidth()/2;//圆心坐标
        int radius = centre - circleWidth/2;//半径

        drawOval(canvas,centre,radius);

        /**
         * 圆内切正方形的位置
         */
        int relRadius = radius - circleWidth/2;//获得内圆半径

        /**
         * 内切正方形距离上下左右的距离
         *
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √relRadius/√2
         */
        mRect.left = (int) (circleWidth+relRadius-relRadius*1.0f/Math.sqrt(2));
        mRect.right = (int) (circleWidth + relRadius + relRadius*1.0f/Math.sqrt(2));
        mRect.top = mRect.left;
        mRect.bottom =  mRect.right;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        canvas.drawRect(mRect,mPaint);

        /**
         * 如果图片比较小，则根据图片的尺寸放到正中心
         */
        if(bgBitmap.getWidth() < relRadius*Math.sqrt(2)){
            mBgRect.left = circleWidth + relRadius - bgBitmap.getWidth()/2;
            mBgRect.right = circleWidth + relRadius + bgBitmap.getWidth()/2;
            mBgRect.top = circleWidth + relRadius - bgBitmap.getHeight()/2;
            mBgRect.bottom = circleWidth + relRadius + bgBitmap.getHeight()/2;

            canvas.drawBitmap(bgBitmap,null,mBgRect,mPaint);
        }

        canvas.drawBitmap(bgBitmap,null,mRect,mPaint);
    }


    private void drawOval(Canvas canvas, int centre, int radius) {
        /**
         * 根据间隔和总数算出每个ItemSize
         */
        float itemSize = (270*1.0f-splitSize*(dotCount-1))/dotCount;

        RectF oval = new RectF(centre-radius,centre-radius,centre+radius,centre+radius);
        mPaint.setStrokeWidth(circleWidth);
        mPaint.setColor(firstColor);
        for (int i = 0; i < dotCount; i++) {
            canvas.drawArc(oval,i*(itemSize+splitSize) -225,itemSize,false,mPaint);
        }

        mPaint.setColor(secondColor);

        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval,i*(itemSize+splitSize) -225,itemSize,false,mPaint);
        }
    }


    private float startY;
    private float moveY;
    private float downSize = 40;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = event.getY();
                if(moveY - startY > downSize){
                    startY = event.getY();
                    down();
                }

                if(startY - moveY > downSize){
                    startY = event.getY();
                    up();
                }
                break;
        }

        return true;
    }

    /**
     * 增加音量
     */
    private void up() {
        mCurrentCount++;
        if(mCurrentCount > dotCount){
            mCurrentCount = dotCount;
            return;
        }
        postInvalidate();
    }

    /**
     * 减少音量
     */
    private void down() {
        mCurrentCount--;
        if(mCurrentCount < 0){
            mCurrentCount=0;
            return;
        }
        postInvalidate();
    }
}
