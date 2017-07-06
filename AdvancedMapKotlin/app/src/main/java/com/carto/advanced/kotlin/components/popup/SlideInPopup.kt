package com.carto.advanced.kotlin.components.popup

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.carto.advanced.kotlin.MapApplication
import com.carto.advanced.kotlin.sections.base.base.BaseView
import com.carto.advanced.kotlin.sections.base.base.isLandScape
import com.carto.advanced.kotlin.sections.base.base.isLargeTablet

/**
 * Created by aareundo on 04/07/2017.
 */
class SlideInPopup(context: Context) : BaseView(context) {

    val transparentArea = BaseView(context)
    val popup = PopupView(context)

    var hiddenY: Int = -1
    var visibleY: Int = -1

    var content: BaseView? = null

    fun isVisible(): Boolean {
        return transparentArea.alpha > 0.0
    }

    init {
        transparentArea.setBackgroundColor(Color.BLACK)
        transparentArea.alpha = 0.0f
        addView(transparentArea)

        addView(popup)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            elevation = 11.0f
        }

        transparentArea.setOnClickListener {
            hide()
        }
    }

    override fun layoutSubviews() {

        var x: Int = 0
        var y: Int = 0
        var w: Int = frame.width
        var h: Int = frame.height

        transparentArea.setFrame(x, y, w, h)

        hiddenY = h
        visibleY = h - (h / 5 * 3)

        if (isLandScape() || isLargeTablet()) {
            w = (300 * context.resources.displayMetrics.density).toInt()
            visibleY = MapApplication.navigationBarHeight!!
        }

        y += h
        h -= visibleY

        popup.setFrame(x, y, w, h)

        if (isVisible()) {
            show()
        }

        if (content != null) {
            x = 0
            y = popup.header.totalHeight
            w = popup.frame.width
            h = popup.frame.height - popup.header.totalHeight

            content?.setFrame(x, y, w, h)
        }
    }

    fun setPopupContent(content: BaseView) {

        if (this.content != null) {
            popup.removeView(content)
            this.content = null
        }
        this.content = content
        popup.addView(content)

        layoutSubviews()
    }

    fun show() {
        transparentArea.alpha = 0.5f
        popup.setFrame(popup.frame.x, visibleY, popup.frame.width, popup.frame.height)
        visibility = View.VISIBLE

//        animateAlpha(0.5f)
//        animateY(visibleY)

    }

    fun hide() {
        transparentArea.alpha = 0.0f
        popup.setFrame(popup.frame.x, hiddenY, popup.frame.width, popup.frame.height)
        visibility = View.GONE

//        animateAlpha(0.0f)
//        animateY(hiddenY)
    }

    val duration: Long = 200

    fun animateAlpha(to: Float) {
        val animation = AlphaAnimation(alpha, to)
        animation.duration = duration
        animation.start()
    }

    fun animateY(to: Int) {
        val animation = TranslateAnimation(
                0, popup.frame.x.toFloat(), TranslateAnimation.ABSOLUTE, popup.frame.x.toFloat(),
                0, popup.frame.y.toFloat(), TranslateAnimation.ABSOLUTE, to.toFloat()
        )

        animation.duration = duration
        animation.start()

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                if (popup.frame.y.equals(hiddenY)) {
                    visibility = View.GONE
                }
            }

            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

}







