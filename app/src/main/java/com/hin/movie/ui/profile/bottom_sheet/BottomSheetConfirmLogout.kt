package com.hin.movie.ui.profile.bottom_sheet

import android.os.Bundle
import android.view.View
import com.hin.movie.databinding.LayoutBottomSheetLogoutBinding
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetConfirmLogout(private val onLogout: () -> Unit): BaseBottomSheetDialogFragment<LayoutBottomSheetLogoutBinding>(LayoutBottomSheetLogoutBinding::inflate) {
    override var height = 270
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnOK.setOnClickListener {
            onLogout()
            dismiss()
        }
    }
}