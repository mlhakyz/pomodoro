import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mlhakyz.pomodoro.R
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding
import com.mlhakyz.pomodoro.databinding.FragmentBottomSheetBinding

// BottomSheetFragment.kt
class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bindingFragment: FragmentBottomSheetBinding
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

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val selectedTime = (progress + 1) * 5
                tvSelectedTime.text = "$selectedTime Dakika"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnApply.setOnClickListener {
            dismiss()
            // buraya Seçilen zaman ile yapılacak kısım yazılacak
            val selectedTime = (seekBar.progress + 1) * 5 // Dakika cinsinden
            
        }
    }
}
