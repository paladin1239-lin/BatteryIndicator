package com.pl_campus.battery_indicator.data.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import com.pl_campus.battery_indicator.domain.utils.Helpers
import androidx.compose.ui.graphics.Color
import javax.inject.Inject

class HelpersImp @Inject constructor(): Helpers {
    override fun getBatteryPercentage(mContex: Context): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus: Intent? = mContex.registerReceiver(null, iFilter)
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level == -1 || scale == -1 || scale == 0) {
            0 // Cannot determine percentage
        } else {
            (level.toFloat() / scale.toFloat() * 100.0f).toInt()
        }
    }

    override fun lerpColor(startColor: Color, endColor: Color, fraction: Float): Color {
        val t = fraction.coerceIn(0f, 1f)

        val interpolatedAlpha = startColor.alpha + (endColor.alpha - startColor.alpha) * t
        val interpolatedRed = startColor.red + (endColor.red - startColor.red) * t
        val interpolatedGreen = startColor.green + (endColor.green - startColor.green) * t
        val interpolatedBlue = startColor.blue + (endColor.blue - startColor.blue) * t


        return Color(
            red = interpolatedRed,
            green = interpolatedGreen,
            blue = interpolatedBlue,
            alpha = interpolatedAlpha
        )
    }
}