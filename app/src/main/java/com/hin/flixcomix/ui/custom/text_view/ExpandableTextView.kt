package com.hin.flixcomix.ui.custom.text_view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hin.flixcomix.R
import com.hin.flixcomix.utils.extensions.gone
import com.hin.flixcomix.utils.extensions.visible
import timber.log.Timber

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val toggleView: TextView
    private var isExpanded = false

    private var maxLines = 3

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_expandable_text, this)
        textView = findViewById(R.id.tvContent)
        toggleView = findViewById(R.id.tvToggle)

        toggleView.setOnClickListener {
            isExpanded = !isExpanded
            updateView()
        }
    }

    fun setText(text: String?) {
        textView.text = text

        if (text.isNullOrEmpty()){
            toggleView.gone()
            return
        }

        textView.post {
            val lineCount = textView.layout.lineCount

            if (lineCount > maxLines) {
                toggleView.gone()
            } else {
                toggleView.visible()
            }
        }
    }

    fun setMaxLines(maxLines: Int) {
        this.maxLines = maxLines
        updateView()
    }

    private fun updateView() {
        if (isExpanded) {
            textView.maxLines = Int.MAX_VALUE
            textView.ellipsize = null
            toggleView.text = context.getString(R.string.less_more)
        } else {
            textView.maxLines = maxLines
            textView.ellipsize = TextUtils.TruncateAt.END
            toggleView.text = resources.getString(R.string.view_more)
        }
    }
}

@BindingAdapter("expandableText")
fun setExpandableText(view: ExpandableTextView, text: String?) {
    view.setText(text)
}

@BindingAdapter("expandableMaxLines")
fun setExpandableMaxLines(view: ExpandableTextView, maxLines: Int) {
    view.setMaxLines(maxLines)
}

