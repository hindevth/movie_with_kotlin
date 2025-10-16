package com.hin.movie.ui.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hin.movie.utils.extensions.dpToPx

abstract class BaseBottomSheetDialogFragment<VB : ViewBinding>(private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB): BottomSheetDialogFragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected open var height = 300

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
                parentLayout?.setBackgroundColor(Color.TRANSPARENT)
            }
            if (height > 0){
                dialog.behavior.peekHeight = height.dpToPx(requireContext())
            } else {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }
}