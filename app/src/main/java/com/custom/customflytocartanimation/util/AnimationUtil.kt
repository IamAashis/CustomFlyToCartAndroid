package com.custom.customflytocartanimation.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import java.lang.ref.WeakReference

/**
 * Created by Aashis on 27,January,2023
 */
class AnimationUtil {
    private var mTarget: View? = null
    private var mDest: View? = null
    private var originX = 0f
    private var originY = 0f
    private var destX = 0f
    private var destY = 0f
    private var mCircleDuration = DEFAULT_DURATION
    private var mMoveDuration = DEFAULT_DURATION
    private val mDisappearDuration = DEFAULT_DURATION_DISAPPEAR
    private var mContextReference: WeakReference<Activity?>? = null
    private var mBorderWidth = 4
    private var mBorderColor = Color.BLACK

    //    private CircleLayout mCircleLayout;
    private var mBitmap: Bitmap? = null
    private var mImageView: ImageView? = null
    private var mAnimationListener: Animator.AnimatorListener? = null
    fun attachActivity(activity: Activity?): AnimationUtil {
        mContextReference = WeakReference(activity)
        return this
    }

    fun setTargetView(view: View?): AnimationUtil {
        mTarget = view
        setOriginRect(mTarget!!.width.toFloat(), mTarget!!.height.toFloat())
        return this
    }

    private fun setOriginRect(x: Float, y: Float): AnimationUtil {
        originX = x
        originY = y
        return this
    }

    private fun setDestRect(x: Float, y: Float): AnimationUtil {
        destX = x
        destY = y
        return this
    }

    fun setDestView(view: View?): AnimationUtil {
        mDest = view
        setDestRect(mDest!!.width.toFloat(), mDest!!.width.toFloat())
        return this
    }

    fun setBorderWidth(width: Int): AnimationUtil {
        mBorderWidth = width
        return this
    }

    fun setBorderColor(color: Int): AnimationUtil {
        mBorderColor = color
        return this
    }

    fun setCircleDuration(duration: Int): AnimationUtil {
        mCircleDuration = duration
        return this
    }

    fun setMoveDuration(duration: Int): AnimationUtil {
        mMoveDuration = duration
        return this
    }

    private fun prepare(): Boolean {
        if (mContextReference!!.get() != null) {
            val decoreView = mContextReference!!.get()!!.window.decorView as ViewGroup
            mBitmap = drawViewToBitmap(mTarget, mTarget!!.width, mTarget!!.height)
            if (mImageView == null) mImageView = ImageView(mContextReference!!.get())
            mImageView!!.setImageBitmap(mBitmap!!)
//            mImageView.setBorderWidth(mBorderWidth)
//            mImageView.setBorderColor(mBorderColor)
            val src = IntArray(2)
            mTarget!!.getLocationOnScreen(src)
            val params = FrameLayout.LayoutParams(mTarget!!.width, mTarget!!.height)
            params.setMargins(src[0], src[1], 0, 0)
            if (mImageView?.parent == null) decoreView.addView(mImageView, params)
        }
        return true
    }

    fun startAnimation() {
        if (prepare()) {
            mTarget!!.visibility = View.INVISIBLE
            avatarRevealAnimator.start()
        }
    }//                        return (float) (Math.sin((0.5f * input) * Math.PI));

    //-(1-x)^2+1
    //        float scaleFactor = Math.max(2f * destY / originY, 2f * destX / originX);
    private val avatarRevealAnimator: AnimatorSet
        @SuppressLint("ObjectAnimatorBinding") private get() {
            val endRadius = Math.max(destX, destY) / 2
            val startRadius = Math.max(originX, originY)
            val mRevealAnimator: Animator = ObjectAnimator.ofFloat(
                mImageView,
                "drawableRadius",
                startRadius,
                endRadius * 1.05f,
                endRadius * 0.9f,
                endRadius
            )
            mRevealAnimator.interpolator = AccelerateInterpolator()

            //        float scaleFactor = Math.max(2f * destY / originY, 2f * destX / originX);
            val scaleFactor = 0.5f
            val scaleAnimatorY: Animator = ObjectAnimator.ofFloat<View>(
                mImageView,
                View.SCALE_Y,
                1f,
                1f,
                scaleFactor,
                scaleFactor
            )
            val scaleAnimatorX: Animator = ObjectAnimator.ofFloat<View>(
                mImageView,
                View.SCALE_X,
                1f,
                1f,
                scaleFactor,
                scaleFactor
            )
            val animatorCircleSet = AnimatorSet()
            animatorCircleSet.duration = mCircleDuration.toLong()
            animatorCircleSet.playTogether(scaleAnimatorX, scaleAnimatorY, mRevealAnimator)
            animatorCircleSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    if (mAnimationListener != null) mAnimationListener!!.onAnimationStart(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    val src = IntArray(2)
                    val dest = IntArray(2)
                    mImageView?.getLocationOnScreen(src)
                    mDest!!.getLocationOnScreen(dest)
                    val y: Float = mImageView!!.y
                    val x: Float = mImageView!!.x
                    val translatorX: Animator = ObjectAnimator.ofFloat<View>(
                        mImageView,
                        View.X,
                        x,
                        x + dest[0] - (src[0] + (originX * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * destX - scaleFactor * endRadius)
                    )
                    translatorX.interpolator =
                        TimeInterpolator { input -> //                        return (float) (Math.sin((0.5f * input) * Math.PI));
                            //-(1-x)^2+1
                            (-Math.pow((input - 1).toDouble(), 2.0) + 1f).toFloat()
                        }
                    val translatorY: Animator = ObjectAnimator.ofFloat<View>(
                        mImageView,
                        View.Y,
                        y,
                        y + dest[1] - (src[1] + (originY * scaleFactor - 2 * endRadius * scaleFactor) / 2) + (0.5f * destY - scaleFactor * endRadius)
                    )
                    translatorY.interpolator = LinearInterpolator()
                    val animatorMoveSet = AnimatorSet()
                    animatorMoveSet.playTogether(translatorX, translatorY)
                    animatorMoveSet.duration = mMoveDuration.toLong()
                    val animatorDisappearSet = AnimatorSet()
                    val disappearAnimatorY: Animator =
                        ObjectAnimator.ofFloat<View>(mImageView, View.SCALE_Y, scaleFactor, 0f)
                    val disappearAnimatorX: Animator =
                        ObjectAnimator.ofFloat<View>(mImageView, View.SCALE_X, scaleFactor, 0f)
                    animatorDisappearSet.duration = mDisappearDuration.toLong()
                    animatorDisappearSet.playTogether(disappearAnimatorX, disappearAnimatorY)
                    val total = AnimatorSet()
                    total.playSequentially(animatorMoveSet, animatorDisappearSet)
                    total.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            if (mAnimationListener != null) mAnimationListener!!.onAnimationEnd(
                                animation
                            )
                            reset()
                        }

                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                    total.start()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            return animatorCircleSet
        }

    private fun drawViewToBitmap(view: View?, width: Int, height: Int): Bitmap {
        val drawable: Drawable = BitmapDrawable()
        //        view.layout(0, 0, width, height);
        val dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(dest)
        drawable.bounds = Rect(0, 0, width, height)
        drawable.draw(c)
        view!!.draw(c)
        //        view.layout(0, 0, width, height);
        return dest
    }

    private fun reset() {
        mBitmap!!.recycle()
        mBitmap = null
        if (mImageView?.getParent() != null) (mImageView?.getParent() as ViewGroup).removeView(
            mImageView
        )
        mImageView = null
        //        mTarget.setVisibility(View.VISIBLE);
    }

    fun setAnimationListener(listener: Animator.AnimatorListener?): AnimationUtil {
        mAnimationListener = listener
        return this
    }

    companion object {
        private const val DEFAULT_DURATION = 1000
        private const val DEFAULT_DURATION_DISAPPEAR = 200
    }
}