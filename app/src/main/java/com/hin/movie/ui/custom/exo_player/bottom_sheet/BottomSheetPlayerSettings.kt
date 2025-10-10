package com.hin.movie.ui.custom.exo_player.bottom_sheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.media3.common.Player
import com.google.android.material.R
import com.hin.movie.R as MainR
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hin.movie.databinding.LayoutBottomSheetMoreBinding
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import com.hin.movie.ui.custom.exo_player.data.Timer
import com.hin.movie.utils.extensions.dpToPx

class BottomSheetPlayerSettings(
    private val player: Player?,
    private val timer: Timer?,
    private val remainingTimer: Long?,
    private val speed: Float?,
) :
    BaseBottomSheetDialogFragment<LayoutBottomSheetMoreBinding>(LayoutBottomSheetMoreBinding::inflate) {
    private lateinit var listener: PlayerSettingListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtTimer.text = timer?.name?.replace(Regex("\\d+"), remainingTimer.toString())
            ?: getString(MainR.string.off)
        binding.txtSpeed.text =
            if (speed != null && speed != 1f) "${speed}x" else getString(MainR.string.normal)
        onClick()
    }

    fun onClick() {
        binding.btnSpeed.setOnClickListener {
            dismiss()
            BottomSheetSpeed { float ->
                player?.setPlaybackSpeed(float)
                listener.onSpeedChanged(float)
            }.show(parentFragmentManager, "Speed")
        }

        binding.btnTimer.setOnClickListener {
            BottomSheetTimer(player, timer) { timer ->
                binding.txtTimer.text = timer.name
                listener.onTimerSelected(timer)
                dismiss()
            }.show(parentFragmentManager, "Timer")
        }
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
            dialog.behavior.peekHeight = 300.dpToPx(requireContext())
        }
        return dialog
    }

    fun setListener(listener: PlayerSettingListener): BottomSheetPlayerSettings {
        this.listener = listener
        return this
    }
}

interface PlayerSettingListener {
    fun onTimerSelected(timer: Timer)
    fun onSpeedChanged(speed: Float)
}