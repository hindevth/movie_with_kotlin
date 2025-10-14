package com.hin.movie.ui.profile.bottom_sheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hin.movie.databinding.LayoutBottomSheetLogoutBinding
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import com.hin.movie.ui.profile.ProfileViewModel

class BottomSheetConfirmLogout: BaseBottomSheetDialogFragment<LayoutBottomSheetLogoutBinding>(LayoutBottomSheetLogoutBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    override var height = 270
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnOK.setOnClickListener {
            viewModel.signOut()
        }
    }
}