package com.hin.movie.ui.sign_in_password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.hin.movie.R
import com.hin.movie.databinding.FragmentSignInPasswordBinding
import com.hin.movie.ui.auth.AuthActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.isValidEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInPasswordFragment : BaseFragment<FragmentSignInPasswordBinding>(
    FragmentSignInPasswordBinding::inflate
) {

    private val viewModel: SignInPasswordViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
    }

    fun onClick() {
        binding.txtToSignUp.setOnClickListener {
            navigateTo(R.id.action_sign_up_password)
        }

        binding.txtForgotPassword.setOnClickListener {
            navigateTo(R.id.action_forgotFragment)
        }

        binding.btnSignIn.setOnClickListener {
            viewModel.signIn()
        }

        binding.btnSignWithGoogle.setOnClickListener {
            viewModel.signInWithGoogle(requireActivity())
        }
    }

    fun onChange() {
        binding.editTxtEmail.apply {
            doAfterTextChanged { editable ->
                viewModel.updateEmail(editable.toString().trim().lowercase())
            }
        }

        binding.editTxtPassword.apply {
            doAfterTextChanged { editable ->
                viewModel.updatePassword(editable.toString().trim())
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { value ->
            binding.frameLayoutLoading.visibility = if (value) View.VISIBLE else View.GONE
        }

        collectLifecycleFlow(viewModel.uiState) { uiState ->
            binding.btnSignIn.isEnabled = uiState.isButtonEnabled

            if (uiState.isLoginSuccess) {
                (requireActivity() as AuthActivity).pushHome()
            }

            if (uiState.messageError != null) {
                showToast(uiState.messageError)
                viewModel.clearMessageError()
            }
        }

    }
}