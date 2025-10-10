package com.hin.movie.ui.sign_up

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.hin.movie.databinding.FragmentSignUpBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.extensions.isValidEmail

class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
    }

    fun onClick() {
        binding.txtToSignIn.setOnClickListener {
            popNavigate()
        }
    }

    fun onChange() {
        var isValidEmail = false
        var isValidPassword = false
        binding.editTxtEmail.apply {
            doAfterTextChanged { editable ->
                isValidEmail = editable.toString().trim().lowercase().isValidEmail()
                binding.btnSignUp.isEnabled = isValidEmail && isValidPassword
            }
        }

        binding.editTxtPassword.apply {
            doAfterTextChanged { editable ->
                isValidPassword = editable.toString().trim().length > 4
                binding.btnSignUp.isEnabled = isValidEmail && isValidPassword
            }
        }
    }
}