package com.txs.artandroid.c4

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Author: txs
 * Date: 19-6-28 下午8:29
 * Package:com.txs.artandroid.c4
 */
class HorizontalScrollViewEx : ViewGroup {
    private val TAG = HorizontalScrollViewEx::class.java.simpleName
    private var mChildrenSize = 0
    private var mChildWidth = 0
    private var mChildIndex = 0
    private var mLastX: Float = 0.0f
    private var mLastY: Float = 0.0f
    private var mLastXIntercept: Float = 0.0f
    private var mLastYIntercept: Float = 0.0f
    private var mScroller: Scroller? = null
    private var mVelocityTracker: VelocityTracker? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    private fun initView() {
        if (mScroller == null) {
            mScroller = Scroller(context)
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        var intercepted = false
        val x = event?.x
        val y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
                if (!mScroller!!.isFinished) {
                    mScroller?.abortAnimation()
                    intercepted = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x?.minus(mLastXIntercept)
                val deltaY = y?.minus(mLastYIntercept)
                intercepted = abs(deltaX!!) > abs(deltaY!!)
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        Log.d(TAG, "intercepted = $intercepted")
        if (x != null) {
            mLastX = x
        }
        if (y != null) {
            mLastY = y
        }
        if (x != null) {
            mLastXIntercept = x
        }
        if (y != null) {
            mLastYIntercept = y
        }
        return intercepted
    }


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker?.addMovement(event)
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mScroller!!.isFinished) {
                    mScroller?.abortAnimation()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                scrollBy(-deltaX.toInt(), 0)
            }
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                mVelocityTracker?.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker?.xVelocity
                if (abs(xVelocity!!) >= 50) {
                    mChildIndex = if (xVelocity > 0) mChildIndex - 1 else mChildIndex + 1
                } else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth
                }
                mChildIndex = max(0, min(mChildIndex, mChildrenSize - 1))
                val dx = mChildIndex * mChildWidth -  scrollX
                smoothScrollBy(dx,0)
                mVelocityTracker?.clear()
            }

        }
        mLastX = x
        mLastY = y
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var measureWidth = 0
        var measureHeight = 0
        val childCount = childCount
        measureChildren(widthMeasureSpec,heightMeasureSpec)
        val widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        if (childCount == 0){
            setMeasuredDimension(0,0)
        }else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            val childView = getChildAt(0)
            measureWidth = childView.measuredWidth*childCount
            measureHeight = childView.measuredHeight
            setMeasuredDimension(measureWidth,measureHeight)
        }

    }

    private fun smoothScrollBy(dx: Int, i: Int) {
        mScroller?.startScroll(scrollX,0,dx,0,500)
        invalidate()
    }
}