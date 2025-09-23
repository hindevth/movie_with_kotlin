package com.hin.flixcomix.ui.sigin_in_social

import android.os.Bundle
import android.view.View
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.FragmentSignInSocialBinding
import com.hin.flixcomix.ui.base.BaseFragment

class SignInSocialFragment : BaseFragment<FragmentSignInSocialBinding>(FragmentSignInSocialBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignInWithPassword.setOnClickListener {
            navigateTo(R.id.action_sign_in_password)
        }

        binding.txtToSignUp.setOnClickListener {
            navigateTo(R.id.action_sign_up_password)
        }
    }

}