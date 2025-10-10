package com.hin.movie.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.viewbinding.ViewBinding
import com.hin.movie.utils.forwardAnim

abstract class BaseFragment<VB : ViewBinding>(private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    fun navigateTo(@IdRes rId: Int) {
        (requireActivity() as BaseActivity).getNavController().navigate(rId)
    }

    fun navigateTo(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions? = forwardAnim()) {
        (requireActivity() as BaseActivity).getNavController().navigate(resId, args, navOptions)
    }

    fun navigateTo(@IdRes resId: Int, navOptions: NavOptions? = forwardAnim()) {
        (requireActivity() as BaseActivity).getNavController().navigate(resId, null, navOptions)
    }

    fun navigateTo(@IdRes resId: Int, args: Bundle?,vararg sharedElements: Pair<View, String>) {
        val extras = FragmentNavigatorExtras(*sharedElements)
        (requireActivity() as BaseActivity).getNavController().navigate(
            resId, args, null,
            extras
        )
    }

    fun popNavigate() {
        (requireActivity() as BaseActivity).onBackPressedDispatcher.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}