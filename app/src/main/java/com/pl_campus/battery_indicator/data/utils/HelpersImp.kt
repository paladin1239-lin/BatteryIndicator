package com.pl_campus.battery_indicator.data.utils

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

import com.pl_campus.battery_indicator.domain.utils.Helpers
import androidx.compose.ui.graphics.Color
import javax.inject.Inject

class HelpersImp @Inject constructor(): Helpers {
    override fun getBatteryPercentage(mContex: Context): Int {
        if (Build.VERSION.SDK_INT >= 21) {
            val bm = mContex.getSystemService(BATTERY_SERVICE) as BatteryManager
            return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = mContex.registerReceiver(null, iFilter)

            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

            val batteryPct = level / scale.toDouble()

            return (batteryPct * 100).toInt()
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