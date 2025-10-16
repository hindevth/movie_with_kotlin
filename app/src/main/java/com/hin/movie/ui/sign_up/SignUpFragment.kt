package com.hin.movie.ui.sign_up

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.hin.movie.R
import com.hin.movie.databinding.FragmentSignUpBinding
import com.hin.movie.ui.auth.AuthActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.extensions.collectLifecycleFlow
import com.hin.movie.utils.extensions.isValidEmail
import com.hin.movie.utils.extensions.pushActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
        binding.editTxtConfirmPassword.setHint(ContextCompat.getString(requireContext(), R.string.confirm_password))
    }

    fun onClick() {
        binding.txtToSignIn.setOnClickListener {
            popNavigate()
        }

        binding.btnSignUp.setOnClickListener {
            viewModel.signUp()
        }

        binding.btnSignWithGoogle.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.signInWithGoogle(requireActivity())
            }
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
                viewModel.updatePassword(editable.toString().trim().lowercase())
            }
        }

        binding.editTxtConfirmPassword.apply {
            doAfterTextChanged { editable ->
                viewModel.updateConfirmPassword(editable.toString().trim().lowercase())
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { value ->
            binding.frameLayoutLoading.visibility = if (value) View.VISIBLE else View.GONE
        }

        collectLifecycleFlow(viewModel.uiState) { state ->
            binding.btnSignUp.isEnabled = state.isButtonSignUpEnabled

            if (state.error != null) {
                showToast(state.error)
                viewModel.clearMessageError()
            }
            if (state.isRegisterSuccess || state.isLoginSuccess){
               (requireActivity() as AuthActivity).pushHome()
            }
        }
    }
}