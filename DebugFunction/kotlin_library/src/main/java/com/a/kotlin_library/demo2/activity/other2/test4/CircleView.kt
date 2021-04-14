package com.a.kotlin_library.demo2.activity.other2.test4

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.a.kotlin_library.R

class CircleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
    private var circleRadius: Float = ta.getDimension(R.styleable.CircleView_circleRadius, 10.0F)

    @SuppressLint("ResourceAsColor")
    private var mColor: Int = ta.getColor(R.styleable.CircleView_circleColor, R.color.colorRed)

    init {
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
        var widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (circleRadius * 2 + paddingLeft + paddingRight).toInt()
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
        var heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (circleRadius * 2 + paddingTop + paddingBottom).toInt()
        }

        setMeasuredDimension(widthSize, heightSize)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    @SuppressLint("ResourceAsColor", "DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = mColor
        val width = width - paddingLeft - paddingRight;
        val height = height - paddingBottom - paddingTop;
        canvas?.drawCircle(width / 2 + paddingLeft.toFloat(), height / 2 + paddingTop.toFloat(), circleRadius, mPaint)
    }
//    采用像素值方式
//    TypedValue.applyDimension(
//    TypedValue.COMPLEX_UNIT_DIP,
//    circleRadius,
//    Resources.getSystem().displayMetrics
//    )
}
