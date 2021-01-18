package com.limbo.drag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import java.util.*

/**
 * Created by wangqi on 2018/8/1.
 */
class DragImageView : FrameLayout, OnLongClickListener {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    private val mList: MutableList<Int?> = ArrayList()

    // 长按之后的间距
    private val padding = 10
    private var state = DragState.NORMAL

    //悬浮在最外层的view
    private var tempView: ImageView? = null

    //按下的坐标
    private var downX = 0f
    private var downY = 0f

    //正在执行动画
    private var isAnimatling = false

    // 长按的position
    private var mDragPosition = 0

    internal enum class DragState {
        NORMAL, DRAGING
    }

    private fun addView() {
        //添加imageview
        try {
            removeAllViews()
            for (i in mList.indices) {
                val imageView = ImageView(context)
                imageView.tag = i
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.setImageResource(mList[i]!!)
                // 设置事件监听
                imageView.setOnLongClickListener(this)
                //                imageView.setOnTouchListener(this);
                val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                if (i == 0) {
                    params.width = width / 2
                    params.height = height
                } else {
                    params.width = width / 4
                    params.height = height / 2
                    if (i == 1) {
                        params.leftMargin = width / 2
                    } else if (i == 2) {
                        params.leftMargin = width * 3 / 4
                    } else if (i == 3) {
                        params.leftMargin = width / 2
                        params.topMargin = height / 2
                    } else if (i == 4) {
                        params.leftMargin = width * 3 / 4
                        params.topMargin = height / 2
                    } else {
                        break
                    }
                }
                addView(imageView, params)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLongClick(view: View): Boolean {
        state = DragState.DRAGING
        mDragPosition = view.tag as Int
        //最上层增加一个view
        tempView = ImageView(context)
        tempView!!.tag = -1
        tempView!!.scaleType = ImageView.ScaleType.CENTER_CROP
        tempView!!.setImageResource(mList[mDragPosition]!!)
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val viewParams = view.layoutParams as LayoutParams
        params.width = viewParams.width - padding * 2
        params.height = viewParams.height - padding * 2
        params.leftMargin = viewParams.leftMargin + padding
        params.topMargin = viewParams.topMargin + padding
        addView(tempView, params)
        //隐藏长按的view
        view.visibility = View.GONE
        return true
    }

    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = motionEvent.x
                downY = motionEvent.y
            }
            MotionEvent.ACTION_MOVE -> if (state == DragState.DRAGING) {
                val v = findViewWithTag<View>(-1)
                val moveX = motionEvent.x
                val moveY = motionEvent.y
                v.translationX = x + moveX - downX
                v.translationY = y + moveY - downY
                // 滑动过程中 做位置变换
                val position = getPositionByXY(moveX, moveY)
                if (position != mDragPosition && !isAnimatling) {
                    viewPositionChange(position, mDragPosition)
                    mDragPosition = position
                }
            }
            MotionEvent.ACTION_UP -> {
                state = DragState.NORMAL
                if (tempView != null) {
                    removeView(tempView)
                }
                refreshView()
            }
        }
        return super.onInterceptTouchEvent(motionEvent)
    }

    /**
     * 执行交换位置动画
     *
     * @param position
     * @param mDragPosition
     */
    private fun viewPositionChange(position: Int, mDragPosition: Int) {
        val view = findViewWithTag<ImageView>(position)
        val dragView = findViewWithTag<ImageView>(mDragPosition)
        val params = view.layoutParams as LayoutParams
        val dragParams = dragView.layoutParams as LayoutParams
        val animationSet = AnimationSet(true)

        //平移动画
        val translateAnimation = TranslateAnimation(0.0f,
                (dragParams.leftMargin - params.leftMargin).toFloat(),
                0.0f,
                (dragParams.topMargin - params.topMargin).toFloat())
        translateAnimation.duration = 200
        translateAnimation.fillAfter = false
        animationSet.addAnimation(translateAnimation)
        //缩放动画
        if (position == 0 || mDragPosition == 0) {
            val scale = if (position == 0) (-2).toFloat() else 2.toFloat()
            val scaleAnimation = ScaleAnimation(1.0f, scale, 1.0f, scale,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            scaleAnimation.duration = 200
            scaleAnimation.fillAfter = false
            animationSet.addAnimation(scaleAnimation)
        }
        animationSet.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                isAnimatling = false
                view.visibility = View.GONE
                dragView.visibility = View.VISIBLE
                dragView.setImageResource(mList[position]!!)
                //修改tempView的大小
                if (mDragPosition == 0) {
                    //缩小
                    val layoutParams = tempView!!.layoutParams as LayoutParams
                    layoutParams.width /= 2
                    layoutParams.height /= 2
                    layoutParams.leftMargin += tempView!!.width / 4
                    layoutParams.topMargin += tempView!!.height / 4
                } else if (position == 0) {
                    // 放大
                    val layoutParams = tempView!!.layoutParams as LayoutParams
                    layoutParams.width *= 2
                    layoutParams.height *= 2
                    layoutParams.leftMargin -= tempView!!.width / 2
                    layoutParams.topMargin -= tempView!!.height / 2
                }
                Collections.swap(mList, position, mDragPosition)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animationSet)
        isAnimatling = true
    }

    /**
     * 通过坐标获取 位置position
     *
     * @param x
     * @param y
     * @return
     */
    private fun getPositionByXY(x: Float, y: Float): Int {
        val position: Int
        position = if (x <= width / 2) {
            0
        } else if (x > width / 2 && x <= width * 3 / 4) {
            if (y <= height / 2) {
                1
            } else {
                3
            }
        } else {
            if (y <= height / 2) {
                2
            } else {
                4
            }
        }
        return position
    }

    fun setData(list: List<Int?>?) {
        if (list != null && list.size != 0) {
            mList.clear()
            mList.addAll(list)
            refreshView()
        }
    }

    fun refreshView() {
        post { addView() }
    }
}