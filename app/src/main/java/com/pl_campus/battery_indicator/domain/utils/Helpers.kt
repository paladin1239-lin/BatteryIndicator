package com.pl_campus.battery_indicator.domain.utils

import android.content.Context
import androidx.compose.ui.graphics.Color

interface Helpers {
    fun getBatteryPercentage(mContex: Context): Int
    fun lerpColor(startColor: Color, endColor: Color, fraction: Float): Color

}