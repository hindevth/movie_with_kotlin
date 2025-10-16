package com.hin.movie.ui.profile

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import com.hin.movie.R
import com.hin.movie.databinding.FragmentProfileBinding
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.auth.AuthActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.ui.profile.bottom_sheet.BottomSheetConfirmLogout
import com.hin.movie.ui.profile.bottom_sheet.BottomSheetDarkMode
import com.hin.movie.ui.profile.bottom_sheet.BottomSheetLanguage
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.pushActivity
import com.hin.movie.utils.helper.LocaleHelper
import com.hin.movie.utils.helper.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        onClick()
        onListener()
    }

    fun onClick() {
        binding.itemLogout.root.setOnClickListener {
            BottomSheetConfirmLogout {
                viewModel.signOut()
            }.show(parentFragmentManager, "Logout")
        }

        binding.itemLanguage.root.setOnClickListener {
            Timber.i(viewModel.uiState.value.setting?.language)
            val bottom = BottomSheetLanguage.newInstance(viewModel.uiState.value.setting?.language ?: "auto") {
                LocaleHelper.changeAppLanguage(it.value)
                viewModel.updateLanguage(it.value)
                recreate(requireActivity())
            }
            bottom.show(parentFragmentManager, "Language")

        }

        binding.itemThemeMode.root.setOnClickListener {
            Timber.i(viewModel.uiState.value.setting?.theme)
            val bottom = BottomSheetDarkMode.newInstance(viewModel.uiState.value.setting?.theme ?: "auto") {
                ThemeHelper.applyTheme(it)
                viewModel.updateTheme(it)
                recreate(requireActivity())
            }
            bottom.show(parentFragmentManager, "DarkMode")
        }

        binding.itemEditProfile.root.setOnClickListener {
            navigateTo(R.id.action_edit_profile)
        }
    }

    fun onListener() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).showLoading()
            }
        }

        collectLifecycleFlow(viewModel.uiState) { state ->
            if (state.isLogoutSuccess) {
                (requireActivity() as MainActivity).pushActivity(
                    AuthActivity::class,
                    EActivityOptionAnim.FADE
                )
                requireActivity().finish()
            }
        }
    }
}