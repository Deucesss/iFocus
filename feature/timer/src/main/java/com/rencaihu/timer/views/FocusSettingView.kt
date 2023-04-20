package com.rencaihu.timer.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.rencaihu.timer.R
import com.rencaihu.timer.databinding.LayoutFocusSettingBinding


class FocusSettingView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): ConstraintLayout(ctx, attrs, defStyle) {

    private val binding: LayoutFocusSettingBinding

    init {
        binding = LayoutFocusSettingBinding.inflate(LayoutInflater.from(ctx), this, false)
        ctx.obtainStyledAttributes(attrs, R.styleable.FocusSettingView).also {
            binding.tvSettingTitle.text = it.getString(R.styleable.FocusSettingView_focus_setting_title)
            binding.tvSettingValue.text = it.getString(R.styleable.FocusSettingView_focus_setting_value)
            it.recycle()
        }
    }

}