package com.mlhakyz.pomodoro

import BottomSheetFragment
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() , BottomSheetFragment.BottomSheetListener {
    private lateinit var binding: ActivityMainBinding
    private val timeFormat: SimpleDateFormat = SimpleDateFormat("mm:ss")
    private var isTimerRunning: Boolean = false
    private var timer: CountDownTimer? = null
    private var pomodoroTimeMills: Long = 1500000 // 25 dakika
    private var shortPauseTimeMills: Long = 300000 // 5 dakika
    private var longPauseTimeMills: Long = 900000 // 15 dakika
    private var selectedTimeInMillis: Long = 0
    private lateinit var lftMediumFont: Typeface
    private lateinit var lftBoldFont: Typeface
    private var timeLeftInMillis: Long = 0 // Kaldığı yerden devam etmek için tutulan süre
    private var mediaPlayer: MediaPlayer? = null
    private var restartControl : String = "pomodoro"
    private var pomodoroTimeText: String = "25:00"
    private var shortPauseTimeText: String = "05:00"
    private var longPauseTimeText: String = "15:00"
    val PREFS_FILENAME = "com.mlhakyz.pomodoro"
    val keyPomodoroName = "pomodoro_duration"
    val keyShortPauseName = "short_pause_duration"
    val keyLongPauseTimeName = "long_pause_duration"
    private lateinit var sharedPrefTimeSettings: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPrefTimeSettings  = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        editor = sharedPrefTimeSettings.edit()

        val pomodoroSharedPrefTime = sharedPrefTimeSettings.getInt(keyPomodoroName,pomodoroTimeMills.toInt())
        val pomodoroSharedPrefSec = sharedPrefTimeSettings.getInt("keySecPomodoroName",25)

        binding.timeText.text = pomodoroSharedPrefSec.toString() + ":00"
        pomodoroTimeMills = pomodoroSharedPrefTime.toLong()
        lftMediumFont = ResourcesCompat.getFont(this, R.font.ltfmedium)!!
        lftBoldFont = ResourcesCompat.getFont(this, R.font.ltfbold)!!
        selectedTimeInMillis = pomodoroTimeMills

        mediaPlayer = MediaPlayer.create(this, R.raw.buttononof)

    }
    override fun onSelectedTimeChanged(pomodoroTime: Int) {
        // BottomSheetFragment'dan gelen veriyi kullan
        Toast.makeText(this, "Seçilen zaman: $pomodoroTime dakika", Toast.LENGTH_SHORT).show()
        println("dakika: "+pomodoroTime)

        val newPomodoroTime = pomodoroTime *60000
        pomodoroTimeMills = newPomodoroTime.toLong()
        selectedTimeInMillis = pomodoroTimeMills
        pomodoroTimeText = pomodoroTime.toString() + ":00"
        binding.timeText.text = pomodoroTimeText


        editor.putInt(keyPomodoroName,newPomodoroTime)
        editor.putInt("keySecPomodoroName",pomodoroTime)

        editor.apply()
        refresh()
        println("millisaniye: "+selectedTimeInMillis)
        // MainActivity'de yapılacak işlemler...
    }


    override fun onPause() {
        super.onPause()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setTimerProperties(btn: View, timeText: String, restartControl: String, selectedTimeInMillis: Long, bgColor: Int, textColor: Int, refreshBtnColor: Int) {
        timer?.cancel()
        timeLeftInMillis = 0
        btn.setBackgroundColor(resources.getColor(Colors.buttonShadowColor))

        binding.timeText.text = timeText
        this.restartControl = restartControl
        this.selectedTimeInMillis = selectedTimeInMillis
        pauseTimer()
        binding.main.setBackgroundColor(getColor(bgColor))
        binding.startPauseBtn.setTextColor(getColor(textColor))
        binding.refreshBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, refreshBtnColor))

        // Set typeface for the appropriate button
        if (btn == binding.pomodoroBtn) {
            binding.pomodoroBtn.typeface = lftBoldFont
            binding.shortPauseBtn.setBackgroundColor(getColor(Colors.pomodoroColor))
            binding.longPauseBtn.setBackgroundColor(getColor(Colors.pomodoroColor))
            binding.shortPauseBtn.typeface = lftMediumFont
            binding.longPauseBtn.typeface = lftMediumFont

        } else if(btn == binding.shortPauseBtn) {
            binding.shortPauseBtn.typeface = lftBoldFont
            binding.pomodoroBtn.setBackgroundColor(getColor(Colors.shortPauseColor))
            binding.longPauseBtn.setBackgroundColor(getColor(Colors.shortPauseColor))
            binding.pomodoroBtn.typeface = lftMediumFont
            binding.longPauseBtn.typeface = lftMediumFont
        }
        else{
            binding.longPauseBtn.typeface = lftBoldFont
            binding.pomodoroBtn.setBackgroundColor(getColor(Colors.longPauseColor))
            binding.shortPauseBtn.setBackgroundColor(getColor(Colors.longPauseColor))
            binding.pomodoroBtn.typeface = lftMediumFont
            binding.shortPauseBtn.typeface = lftMediumFont
        }

    }
    fun pomodoroOnClick(view:View){

        setTimerProperties(
            binding.pomodoroBtn,
            pomodoroTimeText,
            "pomodoro",
            pomodoroTimeMills,
            Colors.pomodoroColor,
            Colors.pomodoroColor,
            Colors.pomodoroColor
        )

    }

    fun shortPauseOnClick(view: View){

        setTimerProperties(
            binding.shortPauseBtn,
            shortPauseTimeText,
            "shortpause",
            shortPauseTimeMills,
            Colors.shortPauseColor,
            Colors.shortPauseColor,
            Colors.shortPauseColor
        )
    }

    fun longPauseOnClick(view: View){

        setTimerProperties(
            binding.longPauseBtn,
            longPauseTimeText,
            "longpause",
            longPauseTimeMills,
            Colors.longPauseColor,
            Colors.longPauseColor,
            Colors.longPauseColor
        )




    }
    fun btnOnStartStop(view: View) {

        println("onStartclick: "+selectedTimeInMillis)

        mediaPlayer?.start()
        if (!isTimerRunning) {

            startTimer(if (timeLeftInMillis.toInt() == 0) selectedTimeInMillis else timeLeftInMillis)
            binding.refreshBtn.visibility = View.VISIBLE
        } else {
            pauseTimer()
        }
    }
   private fun refresh() {
        timer?.cancel()
        timeLeftInMillis = 0
        if (restartControl == "pomodoro"){
            binding.timeText.text = pomodoroTimeText
            pauseTimer()
            binding.refreshBtn.visibility = View.INVISIBLE
        }
        else if (restartControl == "shortpause"){
            binding.timeText.text = shortPauseTimeText
            pauseTimer()
            binding.refreshBtn.visibility = View.INVISIBLE
        }else{
            binding.timeText.text = longPauseTimeText
            pauseTimer()
            binding.refreshBtn.visibility = View.INVISIBLE
        }
    }
    fun btnOnRefresh(view: View){
      refresh()
    }

    fun btnOnSettings(view:View){

        val bottomSheetFragment = BottomSheetFragment()

        // MainActivity sınıfının BottomSheetListener'ı implement ettiğini belirt
        bottomSheetFragment.setBottomSheetListener(this)

        // BottomSheetFragment'ı göster
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun startTimer(durationInMillis: Long){
        timer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(p0: Long) {
                timeLeftInMillis = p0 // Geri sayım süresini güncelleme
                binding.timeText.text = timeFormat.format(p0)
            }

            override fun onFinish() {
                binding.timeText.text = "00:00"
                isTimerRunning = false
                binding.startPauseBtn.text = "START"

            }
        }.start()

        isTimerRunning = true
        binding.startPauseBtn.text = "STOP"

    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.startPauseBtn.text = "START"
        binding.refreshBtn.visibility = View.INVISIBLE
    }
}
object Colors {
    val pomodoroColor: Int = R.color.pomodoroColor
    val shortPauseColor: Int = R.color.shortPauseColor
    val longPauseColor: Int = R.color.longPauseColor
    val buttonShadowColor: Int = R.color.golge

}
