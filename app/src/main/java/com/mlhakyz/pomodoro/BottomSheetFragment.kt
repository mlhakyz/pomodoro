import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mlhakyz.pomodoro.MainActivity
import com.mlhakyz.pomodoro.R
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding
import com.mlhakyz.pomodoro.databinding.FragmentBottomSheetBinding

// BottomSheetFragment.kt
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bindingFragment: FragmentBottomSheetBinding
    private var bottomSheetListener: BottomSheetListener? = null

    fun setBottomSheetListener(listener: BottomSheetListener) {
        bottomSheetListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingFragment = FragmentBottomSheetBinding.bind(view)
        val seekBar = bindingFragment.seekBar
        val tvSelectedTime = bindingFragment.tvSelectedTime
        val btnApply = bindingFragment.btnApply
        val btnMinus = bindingFragment.btnMinus
        val btnPlus = bindingFragment.btnPlus

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedTime = (progress + 1) * 5
                tvSelectedTime.text = "$selectedTime Dakika"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        btnMinus.setOnClickListener {
            val currentProgress = seekBar.progress
            if (currentProgress > 0) {
                seekBar.progress = currentProgress - 1
            } else {
               //minimum değere ulaşıyor
            }
        }

        btnPlus.setOnClickListener {
            val currentProgress = seekBar.progress
            if (currentProgress < seekBar.max) {
                seekBar.progress = currentProgress + 1
            } else {
               // maksimum değere ulaşıyor
            }
        }

        // onViewCreated fonksiyonu içinde...
        btnApply.setOnClickListener {
            dismiss()
            // Seçilen zamanı al
            val pomodoroTime = (seekBar.progress + 1) * 5 // Dakika cinsinden

            // MainActivity'e veriyi ilet
            bottomSheetListener?.onSelectedTimeChanged(pomodoroTime)
        }
    }
    interface BottomSheetListener {
        fun onSelectedTimeChanged(pomodoroTime: Int)
    }

}
