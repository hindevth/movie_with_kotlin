package com.hin.flixcomix.ui.sign_up

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentSignUpBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.utils.extensions.isValidEmail

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