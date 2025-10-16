package com.hin.movie.ui.sigin_in_social

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hin.movie.R
import com.hin.movie.databinding.FragmentSignInSocialBinding
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.auth.AuthActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.pushActivity
import com.hin.movie.utils.forwardAnimFade
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInSocialFragment :
    BaseFragment<FragmentSignInSocialBinding>(FragmentSignInSocialBinding::inflate) {
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
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signInWithGoogle(requireActivity())
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { value ->
            binding.frameLayoutLoading.visibility = if (value) View.VISIBLE else View.GONE
        }

        collectLifecycleFlow(viewModel.uiState) {
            if (it.isLoginSuccess){
                (requireActivity() as AuthActivity).pushHome()
            }
            if (it.messageError != null){
                showToast(it.messageError)
                viewModel.clearMessageError()
            }
        }
    }

}