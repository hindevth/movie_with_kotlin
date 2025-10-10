package com.hin.movie.ui.sigin_in_social

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hin.movie.R
import com.hin.movie.databinding.FragmentSignInSocialBinding
import com.hin.movie.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInSocialFragment : BaseFragment<FragmentSignInSocialBinding>(FragmentSignInSocialBinding::inflate) {
    val viewModel: SignInSocialViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignInWithPassword.setOnClickListener {
            navigateTo(R.id.action_sign_in_password)
        }

        binding.txtToSignUp.setOnClickListener {
            navigateTo(R.id.action_sign_up_password)
        }

        binding.btnSignWithGoogle.setOnClickListener {
            viewModel.signInWithGoogle(requireActivity())
        }

    }

}