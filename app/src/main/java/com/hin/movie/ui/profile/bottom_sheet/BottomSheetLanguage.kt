package com.hin.movie.ui.profile.bottom_sheet

import android.os.Bundle
import android.view.View
import com.hin.movie.R
import com.hin.movie.data.entities.OptionItem
import com.hin.movie.databinding.LayoutBottomSheetLanguageBinding
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import com.hin.movie.ui.profile.adapter.AdapterLanguage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BottomSheetLanguage(
) :
    BaseBottomSheetDialogFragment<LayoutBottomSheetLanguageBinding>(LayoutBottomSheetLanguageBinding::inflate) {
    override var height = 0
    private var langCode: String? = "auto"
    private var onSelectLanguage: ((item: OptionItem) -> Unit)? = null

    companion object {
        fun newInstance(
            langCode: String? = "auto",
            onSelectLanguage: (item: OptionItem) -> Unit
        ): BottomSheetLanguage {
            val fragment = BottomSheetLanguage()
            val args = Bundle()
            args.putString("langCode", langCode)
            fragment.arguments = args
            fragment.onSelectLanguage = onSelectLanguage
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        langCode = arguments?.getString("langCode")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.e(langCode)
        val optionLanguage = listOf(
            OptionItem(
                getString(R.string.auto_follow_system),
                "auto",
                langCode == "auto"
            ),
            OptionItem(
                getString(R.string.lang_vietnamese),
                "vi",
                langCode == "vi"
            ),
            OptionItem(
                getString(R.string.lang_english),
                "en",
                langCode == "en"
            )
        )
        val adapter = AdapterLanguage()
        binding.recyclerView.adapter = adapter
        adapter.submitList(optionLanguage)
        adapter.setOnItemClickListener {
            onSelectLanguage?.invoke(it)
            dismissAllowingStateLoss()
        }
    }
}