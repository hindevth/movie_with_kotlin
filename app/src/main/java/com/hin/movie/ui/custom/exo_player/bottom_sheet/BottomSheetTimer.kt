package com.hin.movie.ui.custom.exo_player.bottom_sheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hin.movie.R
import com.hin.movie.databinding.LayoutBottomSheetTimerBinding;
import com.hin.movie.ui.base.BaseBottomSheetDialogFragment
import com.hin.movie.ui.custom.exo_player.adapter.AdapterTimer
import com.hin.movie.ui.custom.exo_player.adapter.OnListenerItemTimer
import com.hin.movie.ui.custom.exo_player.data.Timer

class BottomSheetTimer(private val player: Player?,private val timer: Timer?, private val onTimerSelected: (Timer) -> Unit) :
    BaseBottomSheetDialogFragment<LayoutBottomSheetTimerBinding>(LayoutBottomSheetTimerBinding::inflate) {

    private lateinit var adapterTimer: AdapterTimer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterTimer = AdapterTimer()
        binding.recyclerView.adapter = adapterTimer

        val list = listOf(
            Timer(ContextCompat.getString(requireContext(), R.string.off), 0),
            Timer(ContextCompat.getString(requireContext(), R.string._10_minutes), 600000),
            Timer(ContextCompat.getString(requireContext(), R.string._15_minutes), 900000),
            Timer(ContextCompat.getString(requireContext(), R.string._20_minutes), 1200000),
            Timer(ContextCompat.getString(requireContext(), R.string._25_minutes), 1500000),
            Timer(ContextCompat.getString(requireContext(), R.string._30_minutes), 1800000),
            Timer(ContextCompat.getString(requireContext(), R.string._35_minutes), 2100000),
            Timer(ContextCompat.getString(requireContext(), R.string._40_minutes), 2400000),
            Timer(ContextCompat.getString(requireContext(), R.string._45_minutes), 2700000),
            Timer(ContextCompat.getString(requireContext(), R.string._50_minutes), 3000000),
            Timer(ContextCompat.getString(requireContext(), R.string._1_hour), 3600000),
            Timer(
                ContextCompat.getString(requireContext(), R.string.when_end),
                player?.duration ?: 0
            ),
        )

        adapterTimer.submitList(list)
        adapterTimer.setSelectedItem(timer ?: list.firstOrNull())
        adapterTimer.setOnListener(object : OnListenerItemTimer {
            override fun onClick(item: Timer) {
                onTimerSelected(item)
                dismiss()
            }
        })

    }

    fun setTimerSelect(item: Timer) {
        adapterTimer.setSelectedItem(item)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.setBackgroundColor(Color.TRANSPARENT)
            }
//            dialog.behavior.peekHeight = 300.dpToPx(requireContext())
        }
        return dialog
    }
}
