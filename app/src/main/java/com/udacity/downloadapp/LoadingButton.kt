package com.udacity.downloadapp

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.udacity.R
import com.udacity.downloadapp.ButtonState
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0

    private var newBgColor: Int = Color.BLACK
    private var newTextColor: Int = Color.BLACK

    @Volatile
    private var progress: Double = 0.0
    private val valueAnimator: ValueAnimator


    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
    }
    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()
        invalidate()
        requestLayout()
    }

    fun hasDownloadDone() {
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        invalidate()
        requestLayout()
    }

    init {
        isClickable = true
        valueAnimator =
            AnimatorInflater.loadAnimator(context, R.animator.button_animator) as ValueAnimator
        valueAnimator.addUpdateListener(updateListener)

        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            newBgColor = getColor(
                R.styleable.LoadingButton_bgColor,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            newTextColor = getColor(
                R.styleable.LoadingButton_textColor,
                ContextCompat.getColor(context, R.color.colorAccent)
            )
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        anim()
        return true
    }

    private fun anim() {
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        paint.color = startState
        paint.strokeWidth = 0f
        paint.color = newBgColor
        canvas?.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )
        if (buttonState == ButtonState.Loading) {
            paint.color = Color.GREEN
            canvas?.drawRect(0f, 0f, (width * (progress / 100)).toFloat(), height.toFloat(), paint)
        }
        val butext = if (buttonState == ButtonState.Loading) "loading..." else "download"
        paint.color = newTextColor
        canvas?.drawText(butext, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }
}