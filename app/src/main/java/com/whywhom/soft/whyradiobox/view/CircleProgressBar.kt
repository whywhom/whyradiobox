package com.whywhom.soft.whyradiobox.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ProgressBar
import com.whywhom.soft.whyradiobox.R


/**
 * Created by wuhaoyong on 9/10/20.
 */
class CircleProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ProgressBar(context, attrs, defStyleAttr) {
    private val mDefaultColor: Int
    private val mReachedColor: Int
    private val mDefaultHeight: Float
    private val mReachedHeight: Float
    private val mRadius: Float
    private var mPaint: Paint? = null
    private var mStatus = Status.Waiting

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    private fun setPaint() {
        mPaint = Paint()
        //paint properties
        mPaint?.isAntiAlias = true //抗锯齿
        mPaint?.setDither(true) //防抖动
        mPaint?.setStyle(Paint.Style.STROKE) //设置填充样式
        mPaint?.setStrokeCap(Paint.Cap.ROUND) //设置画笔笔刷类型
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val paintHeight = Math.max(mReachedHeight, mDefaultHeight) //比较两数，取最大值
        if (heightMode != MeasureSpec.EXACTLY) {
            //如果用户没有精确指出宽高时，我们就要测量整个View所需要分配的高度了，测量自定义圆形View设置的上下内边距+圆形view的直径+圆形描边边框的高度
            val exceptHeight = (paddingTop + paddingBottom + mRadius * 2 + paintHeight).toInt()
            //然后再将测量后的值作为精确值传给父类，告诉他我需要这么大的空间，你给我分配吧
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(exceptHeight, MeasureSpec.EXACTLY)
        }
        if (widthMode != MeasureSpec.EXACTLY) {
            //这里在自定义属性中没有设置圆形边框的宽度，所以这里直接用高度代替
            val exceptWidth = (paddingLeft + paddingRight + mRadius * 2 + paintHeight).toInt()
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(exceptWidth, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 这里canvas.save();和canvas.restore();是两个相互匹配出现的，作用是用来保存画布的状态和取出保存的状态的
         * 当我们对画布进行旋转，缩放，平移等操作的时候其实我们是想对特定的元素进行操作,但是当你用canvas的方法来进行这些操作的时候，其实是对整个画布进行了操作，
         * 那么之后在画布上的元素都会受到影响，所以我们在操作之前调用canvas.save()来保存画布当前的状态，当操作之后取出之前保存过的状态，
         * (比如：前面元素设置了平移或旋转的操作后，下一个元素在进行绘制之前执行了canvas.save();和canvas.restore()操作)这样后面的元素就不会受到(平移或旋转的)影响
         */
        canvas.save()
        //为了保证最外层的圆弧全部显示，我们通常会设置自定义view的padding属性，这样就有了内边距，所以画笔应该平移到内边距的位置，这样画笔才会刚好在最外层的圆弧上
        //画笔平移到指定paddingLeft， getPaddingTop()位置
        canvas.translate(paddingStart.toFloat(), paddingTop.toFloat())
        val mDiameter = (mRadius * 2).toInt()
        if (mStatus == Status.Loading) {
            mPaint?.setStyle(Paint.Style.STROKE)
            //画默认圆(边框)的一些设置
            mPaint?.setColor(mDefaultColor)
            mPaint?.setStrokeWidth(mDefaultHeight)
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint!!)

            //画进度条的一些设置
            mPaint?.setColor(mReachedColor)
            mPaint?.setStrokeWidth(mReachedHeight)
            //根据进度绘制圆弧
            val sweepAngle = progress * 1.0f / max * 360
            canvas.drawArc(RectF(0F, 0F, mRadius * 2, mRadius * 2),
                (-90).toFloat(), sweepAngle, false, mPaint!!)
            mPaint?.setStyle(Paint.Style.STROKE)
            mPaint?.setStrokeWidth(dp2px(context, 2f))
            mPaint?.setColor(Color.parseColor("#667380"))
            canvas.drawLine(
                mRadius * 4 / 5,
                mRadius * 3 / 4,
                mRadius * 4 / 5,
                2 * mRadius - mRadius * 3 / 4,
                mPaint!!
            )
            canvas.drawLine(
                2 * mRadius - mRadius * 4 / 5,
                mRadius * 3 / 4,
                2 * mRadius - mRadius * 4 / 5,
                2 * mRadius - mRadius * 3 / 4,
                mPaint!!
            )
        } else {
            val drawableInt: Int
            when (mStatus) {
                Status.Waiting -> drawableInt = R.drawable.ic_progress_waiting
                Status.Pause -> drawableInt = R.drawable.ic_progress_pause
                Status.Finish -> drawableInt = R.drawable.ic_progress_finish
                Status.Error -> drawableInt = R.drawable.ic_progress_error
                else -> drawableInt = R.drawable.ic_progress_waiting
            }
            val drawable = context.resources.getDrawable(drawableInt)
            drawable.setBounds(0, 0, mDiameter, mDiameter)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    fun getStatus(): Status {
        return mStatus
    }

    fun setStatus(status: Status) {
        if (mStatus == status) return
        mStatus = status
        invalidate()
    }

    enum class Status {
        Waiting, Pause, Loading, Error, Finish
    }

    fun dp2px(context: Context, dp: Float): Float {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return dp * scale + 0.5f
    }

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        //默认圆的颜色
        mDefaultColor = typedArray.getColor(
            R.styleable.CircleProgressBar_defaultColor,
            Color.parseColor("#D8D8D8")
        )
        //进度条的颜色
        mReachedColor = typedArray.getColor(
            R.styleable.CircleProgressBar_reachedColor,
            Color.parseColor("#1296DB")
        )
        //默认圆的高度
        mDefaultHeight = typedArray.getDimension(
            R.styleable.CircleProgressBar_defaultHeight,
            dp2px(context, 2.5f)
        )
        //进度条的高度
        mReachedHeight = typedArray.getDimension(
            R.styleable.CircleProgressBar_reachedHeight,
            dp2px(context, 2.5f)
        )
        //圆的半径
        mRadius = typedArray.getDimension(R.styleable.CircleProgressBar_radius, dp2px(context, 17f))
        typedArray.recycle()
        setPaint()
    }
}