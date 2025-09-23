package com.hin.flixcomix.ui.enter_otp

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentEnterPinBinding
import com.hin.flixcomix.ui.base.BaseFragment
import com.hin.flixcomix.utils.extensions.maskEmail

class EnterPinFragment : BaseFragment<FragmentEnterPinBinding>(FragmentEnterPinBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        onChange()
        bind()
    }

    fun onClick() {
        binding.btnBack.setOnClickListener {
            popNavigate()
        }

        binding.btnVerify.setOnClickListener {
            navigateTo(R.id.action_newPassword)
        }
    }

    fun onChange() {
        binding.pinView.apply {
            doAfterTextChanged { editable ->
                binding.btnVerify.isEnabled = editable.toString().length == 4
            }
        }
    }

    fun bind() {
        arguments.let {
            val email = it?.getString("email") ?: ""
            val res = resources.getString(R.string.code_has_been_send_to)
            "$res\n${email.maskEmail()}".also { binding.txtSendToEmail.text = it }
        }
    }
}