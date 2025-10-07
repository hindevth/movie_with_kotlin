package com.hin.flixcomix.ui.custom.exo_player.bottom_sheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hin.flixcomix.R
import com.hin.flixcomix.databinding.LayoutBottomSheetSpeedBinding
import com.hin.flixcomix.ui.base.BaseBottomSheetDialogFragment
import com.hin.flixcomix.ui.base.GridSpacingItemDecoration
import com.hin.flixcomix.ui.custom.exo_player.adapter.AdapterSpeed
import com.hin.flixcomix.ui.custom.exo_player.adapter.OnListenerItemSpeed
import com.hin.flixcomix.ui.custom.exo_player.data.Speed
import com.hin.flixcomix.utils.extensions.dpToPx


class BottomSheetSpeed(
    private val onSpeedSelected: (Float) -> Unit
) :
    BaseBottomSheetDialogFragment<LayoutBottomSheetSpeedBinding>(LayoutBottomSheetSpeedBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdapterSpeed()
        val spacingItemGrid = resources.getDimensionPixelSize(R.dimen.item_grid_spacing)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                5,
                spacingItemGrid,
                true,
                0
            )
        )
        binding.recyclerView.adapter = adapter
        adapter.submitList(
            listOf(
                Speed("0.25x", 0.25F),
                Speed("0.50x", 0.5F),
                Speed("1.00x", 1F),
                Speed("1.50x", 1.5F),
                Speed("2.00x", 2.00F),
            )
        )
        adapter.setListener(object : OnListenerItemSpeed {
            override fun onClick(item: Speed) {
                onSpeedSelected(item.value)
                binding.seekBarSpeed.progress = ((item.value * 100) / 5).toInt()
                binding.txtSpeed.text = item.name
            }
        })
        onListener()
    }

    fun onListener() {
        binding.btnPlus.setOnClickListener {
            if (binding.seekBarSpeed.progress == binding.seekBarSpeed.max) return@setOnClickListener
            val progress = binding.seekBarSpeed.progress + 1
            val speed = progress.toFloat() * 5  / 100F
            binding.seekBarSpeed.progress = progress
            binding.txtSpeed.text = "${speed}x"
            onSpeedSelected(speed)
        }

        binding.btnMinus.setOnClickListener {
            val progress = binding.seekBarSpeed.progress - 1
            val speed = progress.toFloat() * 5 / 100F
            if (progress == 0) return@setOnClickListener
            binding.seekBarSpeed.progress = progress
            binding.txtSpeed.text = "${speed}x"
            onSpeedSelected(speed)
        }

        binding.seekBarSpeed.max = 200 / 5
        binding.seekBarSpeed.progress = binding.seekBarSpeed.max / 2
        binding.seekBarSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val speed = progress.toFloat() * 5 / 100F
                binding.txtSpeed.text = "${speed}x"
                onSpeedSelected(speed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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
            dialog.behavior.peekHeight = 300.dpToPx(requireContext())
        }
        return dialog
    }

}