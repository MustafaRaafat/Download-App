package com.udacity.downloadapp

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.udacity.R
import com.udacity.downloadapp.ButtonState
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var startState = 0
    private var loadState = 0
    private var load = 0f
    private var completState = 0
    private val valueAnimator =
        ObjectAnimator.ofArgb(this, "backgroundColor", Color.RED, Color.BLACK).setDuration(2000)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    //    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Clicked) { p, old, new ->
//        var v = 0
//    }
    private var buttonState = ButtonState.Clicked


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            startState = getColor(R.styleable.LoadingButton_start, 0)
            loadState = getColor(R.styleable.LoadingButton_load, 0)
            completState = getColor(R.styleable.LoadingButton_complet, 0)
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = startState
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = when (buttonState) {
            ButtonState.Loading -> loadState
            ButtonState.Clicked -> startState
            ButtonState.Completed -> completState
        }
        paint.color = Color.BLACK
        canvas?.drawRect(0f, 0f, load, heightSize.toFloat(), paint)

        paint.color = Color.WHITE
        canvas?.drawText("download", 500f, 100f, paint)

        paint.color = Color.YELLOW
        canvas?.drawArc(
            widthSize / 2f + 220f,
            heightSize / 2f - 60f,
            widthSize / 2f + 300f,
            heightSize / 2f + 50f,
            0f,
            load,
            true,
            paint
        )

//        valueAnimator.setObjectValues()
//        valueAnimator.apply {
////            setObjectValues(object : ArgbEvaluator(), Color.RED, Color.BLACK)
//            duration = 1000
//            addUpdateListener { it -> this@LoadingButton.setBackgroundColor(it.animatedValue as Int) }//.translationX = it.animatedValue as Float }
//            start()
//        }

    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        buttonState = ButtonState.Completed
        load = widthSize.toFloat() / 2f
        valueAnimator.apply {
            duration = 1000
            start()
        }
        invalidate()
        return true
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