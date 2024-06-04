package com.nonoxy.foodies_theme.ui.theme.util

import android.content.res.Resources

internal val Int.toPx: Float
    get() = dpToPx(this).toFloat()

internal val Float.toPx: Float
    get() = dpToPx(this).toFloat()

internal val Float.toDp: Float
    get() = this / Resources.getSystem().displayMetrics.density

internal fun dpToPx(dp: Int): Int =
    (dp * Resources.getSystem().displayMetrics.density).toInt()

internal fun dpToPx(dp: Float): Int =
    (dp * Resources.getSystem().displayMetrics.density).toInt()