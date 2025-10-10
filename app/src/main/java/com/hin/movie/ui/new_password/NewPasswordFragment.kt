package com.hin.movie.ui.new_password

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hin.movie.R
import com.hin.movie.databinding.FragmentNewPasswordBinding
import com.hin.movie.ui.activity.MainActivity
import com.hin.movie.ui.base.BaseFragment
import com.hin.movie.utils.EActivityOptionAnim
import com.hin.movie.utils.dialog
import com.hin.movie.utils.extensions.pushActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPasswordFragment :
    BaseFragment<FragmentNewPasswordBinding>(FragmentNewPasswordBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
    }

    fun onClick() {
        binding.btnContinue.setOnClickListener {
            val dialog = dialog(requireContext(), R.layout.dialog_create_password_success)
            dialog.setCancelable(false)
            dialog.show()

            viewLifecycleOwner.lifecycleScope.launch {
                delay(5000)
                requireActivity().pushActivity(MainActivity::class, EActivityOptionAnim.FADE)
                requireActivity().finish()
            }
        }
    }

    fun onChange() {
        binding.editTxtPassword.doAfterTextChanged {
            validateForm()
        }

        binding.editTxtPasswordVerify.doAfterTextChanged {
            validateForm()
        }
    }

    private fun validateForm() {
        val password = binding.editTxtPassword.getText().trim()
        val confirm = binding.editTxtPasswordVerify.getText().trim()

        val valid = password.length >= 4 && password == confirm

        binding.btnContinue.isEnabled = valid
    }
}