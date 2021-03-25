package com.a.debugfunction.custom_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class LoadingView extends View {

    //圆形的半径
    int radius;

    //圆形外部矩形rect的起点
    int left = 10, top = 30;


    Paint mPaint = new Paint();

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        radius = 0;
        //        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable
        //        .LoadingView);
        //        radius = typedArray.getInt(R.styleable.LoadingView_radius, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        int width = left + radius * 2;
        int height = top + radius * 2;

        //一定要用resolveSize方法来格式化一下你的view宽高噢，否则遇到某些layout的时候一定会出现奇怪的bug的。
        //因为不用这个 你就完全没有父view的感受了 最后强调一遍
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") RectF oval = new RectF(left, top,
                left + radius * 2, top + radius * 2);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(oval, mPaint);
        //先画圆弧
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        canvas.drawArc(oval, -90, 360, false, mPaint);
    }
}

