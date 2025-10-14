package com.hin.movie.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hin.movie.databinding.FragmentProfileBinding
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.profile.bottom_sheet.BottomSheetConfirmLogout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onListener()
    }

    fun onClick() {
        binding.itemLogout.root.setOnClickListener {
            BottomSheetConfirmLogout().show(parentFragmentManager, "Logout")
        }
    }

    fun onListener(){
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).showLoading()
            }
        }
    }
}