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

package com.sorrowblue.android.colorpalettesample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		button.setOnClickListener {
			startActivity(Intent(this, OssLicensesMenuActivity::class.java))
		}
		val bind = ViewModelProviders.of(this).get(MainViewModel::class.java)
		bind.color.value?.let {
			colorPalette.color = it
		}
			colorPalette.setOnColorChanged {
				bind.color.value = it
			}
	}
}

class MainViewModel : ViewModel() {
	val color = MutableLiveData<Int>()
}