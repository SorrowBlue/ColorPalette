package com.sorrowblue.android.colorpalette

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

class ColorSelectionView @JvmOverloads constructor(
	context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

	internal var attrChange: ((Int) -> Unit)? = null
	private val palette = ColorPalette(context)
	private val dp = resources.displayMetrics.density
	private val dialog = AlertDialog.Builder(context).setView(palette).create()
	private var onColorChanged: ((Int) -> Unit)? = null
	private val fillPaint = Paint().apply {
		isAntiAlias = true
		style = Paint.Style.FILL
	}
	private val strokePaint = Paint().apply {
		strokeWidth = dp
		isAntiAlias = true
		style = Paint.Style.STROKE
	}

	var color: Int = Color.BLACK
		set(value) {
			field = value
			invalidate()
		}

	init {
		if (isInEditMode.not()) {
			setOnClickListener {
				palette.color = color
				dialog.show()
			}
			palette.setOnPaletteClickListener {
				dialog.dismiss()
				color = it
				invalidate()
				attrChange?.invoke(it)
				onColorChanged?.invoke(it)
			}
		}
	}


	override fun onDraw(canvas: Canvas?) {
		fillPaint.color = color
		strokePaint.color = colorInvert(color)

		val size = Math.max(width, height) - dp * 2
		canvas?.drawCircle(size / 2 + dp, size / 2 + dp, size / 2, fillPaint)
		canvas?.drawCircle(size / 2 + dp, size / 2 + dp, size / 2, strokePaint)
	}

	fun setOnColorChanged(onColorChanged: ((Int) -> Unit)?) {
		this.onColorChanged = onColorChanged
	}

	private fun colorInvert(color: Int) =
		Color.rgb(255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color))

}

@BindingAdapter("color")
fun ColorSelectionView.setColor(color: Int?) {
	color?.let { this.color = it }
}

@InverseBindingAdapter(attribute = "color")
fun ColorSelectionView.getColor() = color

@BindingAdapter("colorAttrChanged")
fun ColorSelectionView.setListeners(attrChange: InverseBindingListener) {
	this.attrChange = { attrChange.onChange() }
}
