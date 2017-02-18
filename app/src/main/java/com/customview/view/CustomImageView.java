package com.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.customview.R;

/**
 * Created by terrysong on 2017/2/17.
 */

public class CustomImageView extends View{

    private final Rect rect;
    private final Paint mPaint;
    private final Rect mTextBounds;



    private String TAG = "--Main--";
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
     * 图片
     */
    private Bitmap image;
    /**
     * 文字与图片的间距
     */
    private int imageTestSpace;
    /**
     * 缩放类型
     */
    private int mImageScale;


    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView,defStyleAttr,0);

        int count = a.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomImageView_image:
                    image = BitmapFactory.decodeResource(getResources(),a.getResourceId(attr,0));
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = a.getInt(attr,0);
                    break;
                case R.styleable.CustomImageView_titleText:
                    titleText = a.getString(attr);
                    break;
                case R.styleable.CustomImageView_titleTextColor:
                    titleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleTextSize:
                    titleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP,16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomImageView_imageTestSpace:
                    imageTestSpace = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,16, getResources().getDisplayMetrics()));
                    break;

            }
        }

        a.recycle();
        rect = new Rect();
        mTextBounds = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(titleTextSize);
        /**
         * 计算描绘文本所需要的大小
         */
        mPaint.getTextBounds(titleText,0,titleText.length(),mTextBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 设置宽
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        if(widthMode == MeasureSpec.EXACTLY){
            Log.e("--Main--", "EXACTLY " );
            width = widthSize;
        }else {
            /**
             * 使用图片的宽度
             */
//            float textWidth = mPaint.measureText(titleText);
            float imageWidth = image.getWidth();
//            float maxWidth = Math.max(textWidth,imageWidth);
            float desire = getPaddingLeft() + imageWidth + getPaddingRight();
            width = (int) desire;
            if(widthMode == MeasureSpec.AT_MOST){
                width = (int) Math.min(widthSize,desire);
                Log.e("--Main--", "AT_MOST " );
            }
        }

        /**
         * 设置高
         */
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else {
            float textHeight = mTextBounds.height();
            float imageHeight = image.getHeight();
            float desire = getPaddingTop() + textHeight + imageTestSpace +imageHeight + getPaddingBottom();
            height = (int) desire;
            if(heightMode == MeasureSpec.AT_MOST){
                height = (int) Math.min(heightSize,desire);
            }
        }
        Log.e(TAG, "width: "+ width);
        Log.e(TAG, "height: "+ height);
        setMeasuredDimension(width,height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        rect.left = getPaddingLeft();
        rect.right = getMeasuredWidth() - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = getMeasuredHeight() - getPaddingBottom();

        Log.e(TAG, "getMeasuredWidth: "+ getMeasuredWidth());
        Log.e(TAG, "getMeasuredHeight: "+ getMeasuredHeight());

        mPaint.setColor(titleTextColor);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setTextSize(titleTextSize);

        /**
         * 精确的测量文本的高度
         */
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));
//        float textHeight = Math.abs((fontMetrics.descent - fontMetrics.ascent));

        if(mTextBounds.width() > getWidth()){
            TextPaint textPaint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(titleText,textPaint,getWidth()-getPaddingLeft()-getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),getMeasuredHeight()-getPaddingBottom()-fontMetrics.bottom,mPaint);
        }else {
           /* canvas.drawText(titleText,width/2 -mTextBounds.width()/2,
                    height-getPaddingBottom()-mPaint.descent(),mPaint);*/
            canvas.drawText(titleText,getMeasuredWidth()/2 -mPaint.measureText(titleText)/2,
                    getMeasuredHeight()-getPaddingBottom()-fontMetrics.bottom,mPaint);
        }


        Log.e(TAG, "getPaddingBottom()+textHeight/2: "+(getPaddingBottom()+textHeight/2) );
        Log.e(TAG, "textHeight: "+(textHeight) );
        //取消使用掉的快
        rect.bottom -= textHeight+imageTestSpace;

        /**
         * 画图片
         */
        if(mImageScale == 0){
            canvas.drawBitmap(image,null,rect,mPaint);
        }else {
            //计算居中的矩形范围
            rect.left = getMeasuredWidth() / 2 - image.getWidth() / 2;
            rect.right = getMeasuredWidth() / 2 + image.getWidth() / 2;
            rect.top = (int) ((getMeasuredHeight() - textHeight) / 2 - image.getHeight() / 2);
            /*rect.bottom = (int) ((getMeasuredHeight() - textHeight) / 2 +
                    image.getHeight() / 2-imageTestSpace-mPaint.descent());*/
            rect.bottom = (int) (getMeasuredHeight() - textHeight -
                                imageTestSpace);
            canvas.drawBitmap(image, null, rect, mPaint);
        }
//        super.onDraw(canvas);
    }
}
