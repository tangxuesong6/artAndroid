package com.txs.artandroid.c4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.txs.artandroid.R
import kotlin.math.min

/**
 * Author: txs
 * Date: 19-6-27 下午3:00
 * Package:com.txs.artandroid.c4
 */
class CircleView : View {
    private var mColor = Color.RED
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs,0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context.obtainStyledAttributes(attrs,R.styleable.CircleView)
        mColor = a.getColor(R.styleable.CircleView_circle_color,Color.RED)
        a.recycle()
        initView()
    }


    private fun initView() {
        mPaint.color = mColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom

        val width = width - paddingLeft - paddingRight
        val height = height - paddingBottom - paddingTop
        val radius = min(width, height) / 2
        canvas?.drawCircle(
            paddingLeft + width / 2.toFloat(),
            paddingTop + height / 2.toFloat(),
            radius.toFloat(),
            mPaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200,200)
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200,heightSpecSize)
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,200)
        }


    }


}