package com.hin.movie.ui.profile.bottom_sheet

import android.os.Bundle
import android.view.View
import com.hin.movie.databinding.LayoutBottomSheetThemeBinding
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BottomSheetDarkMode() :
    BaseBottomSheetDialogFragment<LayoutBottomSheetThemeBinding>(LayoutBottomSheetThemeBinding::inflate) {
    override var height = 0

    private var isDark: String? = null
    private var onThemeMode: ((them: String) -> Unit)? = null

    companion object {
        fun newInstance(
            isDark: String? = null,
            onThemeMode: (mode: String) -> Unit
        ): BottomSheetDarkMode {
            val fragment = BottomSheetDarkMode()
            val args = Bundle()
            args.putString("theme", isDark)
            fragment.arguments = args
            fragment.onThemeMode = onThemeMode
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDark = arguments?.getString("theme")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardRadioDark.setOnClickListener {
            binding.radioDark.isChecked = true
            binding.radioLight.isChecked = false
            binding.radioAuto.isChecked = false
            onThemeMode?.invoke("dark")
            dismissAllowingStateLoss()
        }
        binding.cardRadioLight.setOnClickListener {
            binding.radioDark.isChecked = false
            binding.radioLight.isChecked = true
            binding.radioAuto.isChecked = false
            onThemeMode?.invoke("light")
            dismissAllowingStateLoss()
        }
        binding.cardRadioAuto.setOnClickListener {
            binding.radioDark.isChecked = false
            binding.radioLight.isChecked = false
            binding.radioAuto.isChecked = true
            onThemeMode?.invoke("auto")
            dismissAllowingStateLoss()
        }
    }

    override fun onResume() {
        super.onResume()
        when (isDark) {
            "dark" -> {
                binding.radioDark.isChecked = true
                binding.radioLight.isChecked = false
                binding.radioAuto.isChecked = false
            }

            "auto" -> {
                binding.radioDark.isChecked = false
                binding.radioLight.isChecked = false
                binding.radioAuto.isChecked = true
            }

            "light" -> {
                binding.radioDark.isChecked = false
                binding.radioLight.isChecked = true
                binding.radioAuto.isChecked = false
            }

        }
    }
}