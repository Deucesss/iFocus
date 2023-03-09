package com.rencaihu.common.ext

import android.content.res.Resources
import android.util.TypedValue

val Number.dp get() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Number.sp get() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Number.px get() =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )