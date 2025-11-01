package com.dertefter.shapemorphview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.PathInterpolator
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.createBitmap
import androidx.core.graphics.withSave
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.toPath
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

class ShapeMorphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var animationDuration: Int = 500
    private var bgColor = MaterialColors.getColor(this, com.google.android.material.R.attr.colorSurfaceContainerHigh, Color.GRAY)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val path = Path()
    private val scaleMatrix = Matrix()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var activeBitmap: Bitmap? = null
    private var pendingBitmap: Bitmap? = null
    private var morph: Morph? = null
    private var currentShape: Shape = getRandomShape()
    private var nextShape: Shape = getRandomShape()

    private var imageResId: Int = -1
    private var editorBitmapLoaded = false

    private var crossfadeProgress = 0f
        set(value) { field = value; invalidate() }
    private var progress = 0f
        set(value) { field = value.coerceIn(0f, 1f); invalidate() }

    private var crossfadeAnimator: ObjectAnimator? = null
    private var progressAnimator: ObjectAnimator? = null

    private val boundsRect = RectF()
    private val tempRect = RectF()

    init {
        context.withStyledAttributes(attrs, R.styleable.ShapeMorphView, defStyleAttr, 0) {
            animationDuration = getInt(R.styleable.ShapeMorphView_animationDuration, animationDuration)
            bgColor = getColor(R.styleable.ShapeMorphView_bgColor, bgColor)
            val resId = getResourceId(R.styleable.ShapeMorphView_imageResource, -1)
            val selectedShape = getInt(R.styleable.ShapeMorphView_shape, -1)
            currentShape = if (selectedShape != -1) Shape.entries[selectedShape] else getRandomShape()

            if (resId != -1) {
                if (isInEditMode) {
                    imageResId = resId
                } else {
                    setDrawableResId(resId, currentShape, animate = false)
                }
            }
        }
        paint.color = bgColor
    }

    fun setDrawableResId(@DrawableRes resId: Int, newShape: Shape? = null, animate: Boolean = true) {
        loadBitmapFromDrawable(resId) { startTransition(it, newShape, animate) }
    }

    fun setBitmap(bitmap: Bitmap, newShape: Shape? = null, animate: Boolean = true) {
        if (width <= 0 || height <= 0){
            post { setBitmap(bitmap, newShape, animate) }
            return
        }
        scope.launch {
            val processed = withContext(Dispatchers.IO) { createProcessedBitmap(bitmap) }
            startTransition(processed, newShape, animate)
        }
    }

    private fun loadBitmapFromDrawable(@DrawableRes resId: Int, onLoaded: (Bitmap) -> Unit) {
        if (width <= 0 || height <= 0) {
            post { loadBitmapFromDrawable(resId, onLoaded) }
            return
        }
        scope.launch(Dispatchers.IO) {
            AppCompatResources.getDrawable(context, resId)?.let { drawable ->
                val size = min(width, height)
                val bmp = createBitmap(size, size).apply {
                    Canvas(this).drawDrawableScaled(drawable, size)
                }
                withContext(Dispatchers.Main) { onLoaded(bmp) }
            }
        }
    }

    private fun Canvas.drawDrawableScaled(drawable: android.graphics.drawable.Drawable, size: Int) {
        val dw = drawable.intrinsicWidth.takeIf { it > 0 } ?: size
        val dh = drawable.intrinsicHeight.takeIf { it > 0 } ?: size
        val scale = max(size.toFloat() / dw, size.toFloat() / dh)
        val left = (size - dw * scale) / 2f
        val top = (size - dh * scale) / 2f
        withSave {
            translate(left, top)
            scale(scale, scale)
            drawable.setBounds(0, 0, dw, dh)
            drawable.draw(this)
        }
    }

    private fun createProcessedBitmap(src: Bitmap): Bitmap {
        val size = min(width, height)
        return createBitmap(size, size).apply {
            Canvas(this).apply {
                val scale = max(size.toFloat() / src.width, size.toFloat() / src.height)
                val left = (size - src.width * scale) / 2f
                val top = (size - src.height * scale) / 2f
                val matrix = Matrix().apply {
                    postScale(scale, scale)
                    postTranslate(left, top)
                }
                drawBitmap(src, matrix, Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG))
            }
        }
    }

    private fun startTransition(newBmp: Bitmap, newShape: Shape?, animate: Boolean) {
        cancelAnimations()
        val targetShape = newShape ?: getRandomShape()

        if (!animate || activeBitmap == null) {
            activeBitmap?.recycle()
            pendingBitmap?.recycle()
            activeBitmap = newBmp
            currentShape = targetShape
            morph = null
            progress = 0f; crossfadeProgress = 0f
        } else {
            pendingBitmap?.recycle()
            pendingBitmap = newBmp
            nextShape = targetShape
            morph = Morph(currentShape.toMaterialShapeReflective(), nextShape.toMaterialShapeReflective())

            progressAnimator = animateFloat("progress", 0f, 1f, animationDuration.toLong()) {
                completeAnimation()
            }
            crossfadeAnimator = animateFloat("crossfadeProgress", 0f, 1f, (animationDuration * 0.8).toLong())
        }
        invalidate()
    }

    private fun animateFloat(prop: String, from: Float, to: Float, duration: Long, end: (() -> Unit)? = null) =
        ObjectAnimator.ofFloat(this, prop, from, to).apply {
            this.duration = duration
            interpolator = PathInterpolator(0.2f, 0f, 0f, 1f)
            end?.let { addListener(object : AnimatorListenerAdapter() { override fun onAnimationEnd(a: Animator) = it() }) }
            start()
        }

    private fun completeAnimation() {
        activeBitmap?.recycle()
        activeBitmap = pendingBitmap
        pendingBitmap = null
        currentShape = nextShape
        morph = null
        progress = 0f; crossfadeProgress = 0f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (isInEditMode && imageResId != -1 && !editorBitmapLoaded && measuredWidth > 0 && measuredHeight > 0) {
            editorBitmapLoaded = true
            try {
                AppCompatResources.getDrawable(context, imageResId)?.let { drawable ->
                    val size = min(measuredWidth, measuredHeight)
                    val bmp = createBitmap(size, size).apply {
                        Canvas(this).drawDrawableScaled(drawable, size)
                    }

                    activeBitmap?.recycle()
                    pendingBitmap?.recycle()
                    activeBitmap = bmp
                    pendingBitmap = null
                    morph = null
                    progress = 0f
                    crossfadeProgress = 0f
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = min(width, height).toFloat()
        path.reset()
        (morph?.toPath(progress, path) ?: currentShape.toMaterialShapeReflective().toPath(path))

        path.computeBounds(boundsRect, true)
        val scale = boundsRect.takeIf { it.width() > 0 && it.height() > 0 }
            ?.let { min(size / it.width(), size / it.height()) } ?: 1f

        scaleMatrix.setScale(scale, scale)
        path.transform(scaleMatrix)
        path.computeBounds(tempRect, true)
        path.offset((width - tempRect.width()) / 2f - tempRect.left, (height - tempRect.height()) / 2f - tempRect.top)

        canvas.withSave {
            clipPath(path)
            drawPath(path, paint)
            drawCrossfadedBitmaps()
        }
    }

    private fun Canvas.drawCrossfadedBitmaps() {
        activeBitmap?.let { bmp ->
            val left = (width - bmp.width) / 2f
            val top = (height - bmp.height) / 2f
            if (pendingBitmap != null && crossfadeProgress > 0f) {
                paint.alpha = (255 * (1 - crossfadeProgress)).toInt()
                drawBitmap(bmp, left, top, paint)
                pendingBitmap?.let {
                    paint.alpha = (255 * crossfadeProgress).toInt()
                    drawBitmap(it, left, top, paint)
                }
                paint.alpha = 255
            } else drawBitmap(bmp, left, top, null)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimations()
        scope.cancel()
        activeBitmap?.recycle()
        pendingBitmap?.recycle()
        activeBitmap = null
        pendingBitmap = null
    }

    private fun cancelAnimations() {
        crossfadeAnimator?.cancel()
        progressAnimator?.cancel()
    }

    fun getRandomShape(): Shape = Shape.entries.random()
}
