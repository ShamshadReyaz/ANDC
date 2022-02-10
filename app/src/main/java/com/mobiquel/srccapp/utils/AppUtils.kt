package com.mobiquel.srccapp.utils

import android.content.res.Resources
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Navjot Singh
 * on 2/3/19.
 */
object AppUtils {
    fun dp2px(resources: Resources, dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(resources: Resources, sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }

    fun formatDateTime(
        dateTime: String?,
        inputFormat: String?,
        outputFormat: String?
    ): String? {
        val inputSDF =
            SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputSDF =
            SimpleDateFormat(outputFormat, Locale.getDefault())
        val date: Date
        try {
            date = inputSDF.parse(dateTime)
            return outputSDF.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}