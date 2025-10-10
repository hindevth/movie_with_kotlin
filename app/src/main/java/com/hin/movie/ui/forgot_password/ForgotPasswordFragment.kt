package com.hin.movie.ui.forgot_password

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.hin.movie.R
import com.hin.movie.databinding.FragmentForgetPasswordBinding
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.extensions.isValidEmail

class ForgotPasswordFragment :
    BaseFragment<FragmentForgetPasswordBinding>(FragmentForgetPasswordBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onChange()
        onClick()
    }

    fun onClick(){
        binding.btnContinue.setOnClickListener {
            val email = binding.editTxtEmail.text.toString().trim()
            navigateTo(
                R.id.action_enterPinFragment, bundleOf(
                    "email" to email
                )
            )
        }

        binding.btnBack.setOnClickListener {
            popNavigate()
        }
    }

    fun onChange() {
        binding.editTxtEmail.apply {
            doAfterTextChanged { editable ->
                binding.btnContinue.isEnabled =
                    editable.toString().trim().lowercase().isValidEmail()
            }
        }
    }
}