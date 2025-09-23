package com.hin.flixcomix.ui.new_password

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentNewPasswordBinding
import com.hin.flixcomix.ui.activity.MainActivity
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.utils.EActivityOptionAnim
import com.hin.flixcomix.utils.dialog
import com.hin.flixcomix.utils.extensions.pushActivity
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