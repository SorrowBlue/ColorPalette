/*
 * Copyright (C) 2018 SorrowBlue
 * http://sorrowblue.com/android-library
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sorrowblue.android.colorpalette

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.sorrowblue.android.colorpalette.databinding.ViewPaletteBinding

class ColorPalette @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
	FlexboxLayout(context, attrs, defStyleAttr) {

	internal var attrChange: ((color: Int) -> Unit)? = null
	private var onPaletteClick: ((color: Int) -> Unit)? = null
	private val colorList = mutableListOf<Int>()
	var color: Int = Color.BLACK
		set (color) {
			field = color
			setSelectedColor(color)
		}

	init {
		if (isInEditMode.not()) {
			LayoutInflater.from(context).inflate(R.layout.color_palette, this, true)
			flexWrap = FlexWrap.WRAP
			context.resources.getIntArray(R.array.color_palette_color_list).forEach {
				bindPalette(it)
				colorList.add(it)
			}
		}
	}

	private fun setSelectedColor(color: Int) {
		val index = colorList.indexOf(color)
		if (0 <= index) {
			val target = getChildAt(index)
			forEach {
				if (it != target && it.isSelected) {
					it.isSelected = false
				} else if (it == target && it.isSelected.not()) {
					it.isSelected = true
					it.callOnClick()
				}
			}
		}
	}

	private fun bindPalette(color: Int) {
		val binding = ViewPaletteBinding.inflate(LayoutInflater.from(context), this, false)
		binding.root.backgroundTintList = ColorStateList.valueOf(color)
		binding.root.setOnClickListener { v ->
			forEach { if (it != v && it.isSelected) it.isSelected = false }
			if (v.isSelected.not()) {
				val col = v.backgroundTintList?.defaultColor ?: return@setOnClickListener
				v.isSelected = true
				onPaletteClick?.invoke(col)
				attrChange?.invoke(col)
			}
		}
		addView(binding.root)
	}

	fun setOnPaletteClickListener(onPaletteClick: ((color: Int) -> Unit)?) {
		this.onPaletteClick = onPaletteClick
	}

}

@BindingAdapter("palette")
fun ColorPalette.setPalette(color: Int?) {
	color?.let { this.color = it }
}

@InverseBindingAdapter(attribute = "palette")
fun ColorPalette.getPalette() = color

@BindingAdapter("paletteAttrChanged")
fun ColorPalette.setListeners(attrChange: InverseBindingListener) {
	this.attrChange = { attrChange.onChange() }
}
