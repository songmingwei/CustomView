package com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by terrysong on 2017/2/16.
 *
 * 自定义View
 * 可点击的文本，类似验证码
 *
 * 自定义View可使用的数据类型
 * string,color,demension,integer,enum,reference,float,boolean,fraction,flag;
 *
 * 参考鸿洋大神的csdn：
 * http://blog.csdn.net/lmj623565791/article/details/24252901/
 * 噪点实现参考这位同学的博客：
 * http://blog.csdn.net/qq_24304811/article/details/51308663
 */

public class CustomTitleView extends View {
    /**
     * 文本
     */
    private String titleText;
    /**
     * 文本颜色
     */
    private int titleTextColor;
    /**
     * 文本大小
     */
    private int titleTextSize;

    /**
     * 绘制时控制文本的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public CustomTitleView(Context context) {
        this(context,null);

    }

    /**
     * 获取随机文本
     * @return
     */
    private String randomText() {
        Set<Integer> set = new HashSet<>();

        while (set.size() < 4){
            Random random = new Random();
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }

        StringBuilder sb = new StringBuilder();
        for (Integer i : set) {
            sb.append("" + i);
        }

        return sb.toString();
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    /**
     * 获取我自定义的属性
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText = randomText();
                postInvalidate();
            }
        });

        //以下两种方式获取属性是一样的;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomTitleView,defStyleAttr,0);
//        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomTitleView,defStyleAttr,0);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.CustomTitleView_titleText:
                    titleText = typedArray.getString(attr);
                    break;
                case R.styleable.CustomTitleView_titleTextColor:
                    //默认颜色设置为黑色
                    titleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    //默认设置为16sp，TypeValue也可以将sp转为px
                    titleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        typedArray.recycle();

        /**
         * 获取文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(titleTextSize);
//        mPaint.setColor(titleTextColor);

        /**
         * 计算文字所在的矩形来计算文字的宽和高
         *
         * 计算文字的宽和高有三种方式：可参考下面博客
         * http://blog.csdn.net/chuekup/article/details/7518239
         */
        mBound = new Rect();
        mPaint.getTextBounds(titleText,0,titleText.length(),mBound);

        //3. 精确计算文字宽度
//        int textWidth = getTextWidth(mPaint, titleText);
//        Log.d("--Main--", "textWidth=" + textWidth);
    }

    /**
     * 获取文字的宽度
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 如果不自己测量
     * 1.设置固定宽和高，系统为我们测量的结果就是我们设置的宽和高
     * 2.设置为wrap_content或match_parent，则系统测量的结果都是，match_parent。
     * 所以，要重写onMeasure方法，自己去测量。
     *
     * MeasureSpec的specMode有三种类型：
     *        1.EXACTLY：一般是设置了明确的值或者是MATCH_PARENT;
     *        2.AT_MOST：表示子布局限制在一个最大值内，一般为WRAP_CONTENT;
     *        3.UNSPECIFIED：表示子布局想要多大就多大，很少使用。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            mPaint.setTextSize(titleTextSize);
            mPaint.getTextBounds(titleText,0,titleText.length(),mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }


        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            mPaint.setTextSize(titleTextSize);
            mPaint.getTextBounds(titleText,0,titleText.length(),mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop()+textHeight+getPaddingBottom());
            height = desired;
        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //将自己测量的结果直接设置进去
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(titleTextColor);
        canvas.drawText(titleText,getWidth()/2 - mBound.width()/2,getHeight()/2 + mBound.height()/2,mPaint);


        /**
         * 添加噪点
         */
        drawCircle(canvas);
        long time = System.currentTimeMillis();
        drawLine(canvas);
        time-=System.currentTimeMillis();
        Log.e("--Main--", "drawLine for: "+(-time));
    }

    /**
     * 画线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int[] line = {0,0,0,0};
        for (int i = 0; i < 100; i++) {
            line = getLine(width,height);
            /**
             * drawLine(float startX, float startY, float stopX, float stopY,Paint paint)
             * float startX:起始点x坐标。
             * float startY:起始点y坐标。
             * float stopX:结束点y坐标。
             * float stopY:结束点y坐标。
             * Paint paint:绘制时使用的画笔。
             */
            canvas.drawLine(line[0],line[1],line[2],line[3],mPaint);
        }
    }

    private int[] getLine(int width, int height) {
        int [] tempCheckNum = {0,0,0,0};
        /**
         * 此处写个for不知道起到什么作用，两个效率不差什么，可能是随机的点更好一些？
         * 有知道的可以告诉我哈，哈哈哈！
         */
        for (int i = 0; i < 4; i+=2) {
            tempCheckNum[i] = (int) (Math.random()*width);
            tempCheckNum[i+1] = (int) (Math.random()*height);
        }
        /*tempCheckNum[0] = (int) (Math.random()*width);
        tempCheckNum[1] = (int) (Math.random()*height);
        tempCheckNum[2] = (int) (Math.random()*width);
        tempCheckNum[3] = (int) (Math.random()*height);*/
        return tempCheckNum;
    }

    /**
     * 画圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int[] point;
        for (int i = 0; i < 100; i++) {
            point = getPoint(width,height);
            /**
             * drawCircle(float cx,float cy,float radius,Paint paint)
             * float cx:圆心的x坐标。
             * float cy:圆心的y坐标。
             * float radius:圆的半径。
             * Paint paint:绘制时使用的画笔。
             */
            canvas.drawCircle(point[0],point[1],2,mPaint);
        }
    }

    public int[] getPoint(int width,int height) {
        int[] tempCheckNum = {0,0,0,0};
        tempCheckNum[0] = (int) (Math.random()*width);
        tempCheckNum[1] = (int) (Math.random()*height);
        return tempCheckNum;
    }
}
