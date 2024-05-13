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
        val btnApply = bindingFragment.btnApply

        val seekBarPomodoro = bindingFragment.seekBarPomodoro
        val tvSelectedPomodoroTime = bindingFragment.tvSelectedPomodoroTime
        val btnPomodoroMinus = bindingFragment.btnPomodoroMinus
        val btnPomodoroPlus = bindingFragment.btnPomodoroPlus

        val seekBarShortPause = bindingFragment.seekBarShortPause
        val tvSelectedShortPauseTime = bindingFragment.tvSelectedShortPauseTime
        val btnShortPauseMinus = bindingFragment.btnShortPauseMinus
        val btnShortPausePlus = bindingFragment.btnShortPausePlus

        //POMODORO START
        seekBarPomodoro.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedTime = (progress + 1) * 5
                tvSelectedPomodoroTime.text = "$selectedTime Dakika"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        btnPomodoroMinus.setOnClickListener {
            val currentProgress = seekBarPomodoro.progress
            if (currentProgress > 0) {
                seekBarPomodoro.progress = currentProgress - 1
            } else {
               //minimum değere ulaşıyor
            }
        }

        btnPomodoroPlus.setOnClickListener {
            val currentProgress = seekBarPomodoro.progress
            if (currentProgress < seekBarPomodoro.max) {
                seekBarPomodoro.progress = currentProgress + 1
            } else {
               // maksimum değere ulaşıyor
            }
        }
        //POMODORO FİNİSH



        //SHORTPAUSE START

        seekBarShortPause.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedTime = (progress + 1) * 5
                tvSelectedShortPauseTime.text = "$selectedTime Dakika"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        btnShortPauseMinus.setOnClickListener {
            val currentProgress = seekBarShortPause.progress
            if (currentProgress > 0) {
                seekBarShortPause.progress = currentProgress - 1
            } else {
                //minimum değere ulaşıyor
            }
        }

        btnShortPausePlus.setOnClickListener {
            val currentProgress = seekBarShortPause.progress
            if (currentProgress < seekBarShortPause.max) {
                seekBarShortPause.progress = currentProgress + 1
            } else {
                // maksimum değere ulaşıyor
            }
        }
        //SHORTPAUSE FİNİSH

        // onViewCreated fonksiyonu içinde...
        btnApply.setOnClickListener {
            dismiss()
            // Seçilen zamanı al
            val pomodoroTime = (seekBarPomodoro.progress + 1) * 5 // Dakika cinsinden
            val shortPauseTime = (seekBarShortPause.progress + 1) * 5 // Dakika cinsinden

            // MainActivity'e veriyi ilet
            bottomSheetListener?.onSelectedTimeChanged(pomodoroTime,shortPauseTime)
        }

    }
    interface BottomSheetListener {
        fun onSelectedTimeChanged(pomodoroTime: Int,shortPauseTime: Int)
    }

}
