package com.hin.movie.ui.sign_in_password

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.hin.movie.R
import com.hin.movie.databinding.FragmentSignInPasswordBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.extensions.isValidEmail

class SignInPasswordFragment : BaseFragment<FragmentSignInPasswordBinding>(
    FragmentSignInPasswordBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
    }

    fun onClick(){
        binding.txtToSignUp.setOnClickListener {
            navigateTo(R.id.action_sign_up_password)
        }

        binding.txtForgotPassword.setOnClickListener {
            navigateTo(R.id.action_forgotFragment)
        }
    }

    fun onChange(){
        var isValidEmail = false
        var isValidPassword = false
        binding.editTxtEmail.apply {
            doAfterTextChanged { editable ->
                isValidEmail = editable.toString().trim().lowercase().isValidEmail()
                binding.btnSignIn.isEnabled = isValidEmail && isValidPassword
            }
        }

        binding.editTxtPassword.apply {
            doAfterTextChanged { editable ->
                isValidPassword = editable.toString().trim().length > 4
                binding.btnSignIn.isEnabled = isValidEmail && isValidPassword
            }
        }
    }
}