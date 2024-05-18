import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences

    fun setBottomSheetListener(listener: BottomSheetListener) {
        bottomSheetListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        // Saklanacak değerleri Bundle'a ekle
        val savedValues = savedInstanceState ?: Bundle()
        sharedPreferences = requireContext().getSharedPreferences("seekbar_prefs", Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingFragment = FragmentBottomSheetBinding.bind(view)
        val btnApply = bindingFragment.btnApply


        // SharedPreferences'ten son seek bar değerlerini al
        val pomodoroProgress = sharedPreferences.getInt("pomodoroProgress", 5)
        val shortPauseProgress = sharedPreferences.getInt("shortPauseProgress", 0)
        val longPauseProgress = sharedPreferences.getInt("longPauseProgress", 2)
        // seek bar'ların son durumunu ayarla
        bindingFragment.seekBarPomodoro.progress = pomodoroProgress
        bindingFragment.seekBarShortPause.progress = shortPauseProgress
        bindingFragment.seekBarLongPause.progress = longPauseProgress

        // SharedPreferences'ten son Text değerlerini al
        val pomodoroBottomSheetText = sharedPreferences.getInt("pomodoroBottomSheetText", 30)
        val shortPauseBottomSheetText = sharedPreferences.getInt("shortPauseBottomSheetText", 5)
        val longPauseBottomSheetText = sharedPreferences.getInt("longPauseBottomSheetText", 5)
        // Textler'lerın son durumunu ayarla
        bindingFragment.tvSelectedPomodoroTime.text = "$pomodoroBottomSheetText Dakika"
        bindingFragment.tvSelectedShortPauseTime.text=  "$shortPauseBottomSheetText Dakika"
        bindingFragment.tvSelectedLongPauseTime.text=  "$longPauseBottomSheetText Dakika"


        val seekBarPomodoro = bindingFragment.seekBarPomodoro
        val tvSelectedPomodoroTime = bindingFragment.tvSelectedPomodoroTime
        val btnPomodoroMinus = bindingFragment.btnPomodoroMinus
        val btnPomodoroPlus = bindingFragment.btnPomodoroPlus

        val seekBarShortPause = bindingFragment.seekBarShortPause
        val tvSelectedShortPauseTime = bindingFragment.tvSelectedShortPauseTime
        val btnShortPauseMinus = bindingFragment.btnShortPauseMinus
        val btnShortPausePlus = bindingFragment.btnShortPausePlus

        val seekBarLongPause = bindingFragment.seekBarLongPause
        val tvSelectedLongPauseTime = bindingFragment.tvSelectedLongPauseTime
        val btnLongPauseMinus = bindingFragment.btnLongPauseMinus
        val btnLongPausePlus = bindingFragment.btnLongPausePlus

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


        //LONGPAUSE START

        seekBarLongPause.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedTime = (progress + 1) * 5
                tvSelectedLongPauseTime.text = "$selectedTime Dakika"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        btnLongPauseMinus.setOnClickListener {
            val currentProgress = seekBarLongPause.progress
            if (currentProgress > 0) {
                seekBarLongPause.progress = currentProgress - 1
            } else {
                //minimum değere ulaşıyor
            }
        }

        btnLongPausePlus.setOnClickListener {
            val currentProgress = seekBarLongPause.progress
            if (currentProgress < seekBarLongPause.max) {
                seekBarLongPause.progress = currentProgress + 1
            } else {
                // maksimum değere ulaşıyor
            }
        }
        //LONGPAUSE FİNİSH

        // onViewCreated fonksiyonu içinde...
        btnApply.setOnClickListener {
            dismiss()
            // Seçilen zamanı al
            val pomodoroTime = (seekBarPomodoro.progress + 1) * 5 // Dakika cinsinden
            val shortPauseTime = (seekBarShortPause.progress + 1) * 5 // Dakika cinsinden
            val longPauseTime = (seekBarLongPause.progress + 1) * 5 // Dakika cinsinden
            // MainActivity'e veriyi ilet
            bottomSheetListener?.onSelectedTimeChanged(pomodoroTime,shortPauseTime,longPauseTime)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // seek bar değerlerini SharedPreferences'e kaydet
        sharedPreferences.edit().apply {
            putInt("pomodoroProgress", bindingFragment.seekBarPomodoro.progress)
            putInt("shortPauseProgress", bindingFragment.seekBarShortPause.progress)
            putInt("longPauseProgress", bindingFragment.seekBarLongPause.progress)

            val pomodoroTimeText = (bindingFragment.seekBarPomodoro.progress + 1) * 5 // Dakika cinsinden
            val shortPauseTimeText = (bindingFragment.seekBarShortPause.progress + 1) * 5 // Dakika cinsinden
            val longPauseTimeText = (bindingFragment.seekBarLongPause.progress + 1) * 5 // Dakika cinsinden

            putInt("pomodoroBottomSheetText",pomodoroTimeText )
            putInt("shortPauseBottomSheetText", shortPauseTimeText)
            putInt("longPauseBottomSheetText", longPauseTimeText)
            apply()
        }
    }
    interface BottomSheetListener {
        fun onSelectedTimeChanged(pomodoroTime: Int,shortPauseTime: Int,longPauseTime: Int)
    }

}
