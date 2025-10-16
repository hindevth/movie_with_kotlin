package com.hin.movie.ui.custom.radio

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.hin.movie.R
import com.hin.movie.databinding.ItemRadioOptionBinding

class RadioCardButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : CardView(context, attrs) {
    private var binding: ItemRadioOptionBinding =
        ItemRadioOptionBinding.inflate(LayoutInflater.from(context), this, true)
    private var onClick: ((Boolean) -> Unit)? = null
    private var isChecked = false

    init {
        binding.root.setOnClickListener {
            setToggle()
            onClick?.invoke(isChecked)
        }
        context.theme.obtainStyledAttributes(attrs, R.styleable.RadioCardButton, 0, 0).apply {
            try {
                binding.radioAuto.text = getString(R.styleable.RadioCardButton_rcb_text)
                binding.radioAuto.isChecked = getBoolean(R.styleable.RadioCardButton_rcb_checked, false)
            } finally {
                recycle()
            }
        }
    }

    fun setToggle() {
        isChecked = !isChecked
        binding.radioAuto.isChecked = isChecked
    }

    fun setOnClick(onClick: (Boolean) -> Unit) {
        this.onClick = onClick
    }

    fun setChecked(checked: Boolean) {
        binding.radioAuto.isChecked = checked
    }

    fun setText(text: String) {
        binding.radioAuto.text = text
    }
}
