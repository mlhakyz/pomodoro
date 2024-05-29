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
    private var pomodoroTimeMills: Long = 1800000 // 30 dakika
    private var shortPauseTimeMills: Long = 300000 // 5 dakika
    private var longPauseTimeMills: Long = 900000 // 15 dakika
    private var selectedTimeInMillis: Long = 0
    private lateinit var lftMediumFont: Typeface
    private lateinit var lftBoldFont: Typeface
    private var timeLeftInMillis: Long = 0 // Kaldığı yerden devam etmek için tutulan süre
    private var mediaPlayer: MediaPlayer? = null
    private var restartControl : String = "pomodoro"
    private var pomodoroTimeText: String = "30:00"
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

        lftMediumFont = ResourcesCompat.getFont(this, R.font.ltfmedium)!!
        lftBoldFont = ResourcesCompat.getFont(this, R.font.ltfbold)!!

        sharedPrefTimeSettings  = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        editor = sharedPrefTimeSettings.edit()

        val pomodoroSharedPrefTime = sharedPrefTimeSettings.getInt(keyPomodoroName,pomodoroTimeMills.toInt())
        val pomodoroSharedPrefSec = sharedPrefTimeSettings.getInt("keySecPomodoroName",30)

        if (pomodoroSharedPrefSec == 5 ){
            pomodoroTimeText = "0$pomodoroSharedPrefSec:00"
        }
        else{
            pomodoroTimeText = "$pomodoroSharedPrefSec:00"
        }
        binding.timeText.text = pomodoroTimeText

        pomodoroTimeMills = pomodoroSharedPrefTime.toLong()

        selectedTimeInMillis = pomodoroTimeMills


        mediaPlayer = MediaPlayer.create(this, R.raw.buttononof)

    }
    override fun onSelectedTimeChanged(pomodoroTime: Int, shortPauseTime:Int, longPauseTime:Int) {
        //Pomodoro İşlemleri
        val newPomodoroTime = pomodoroTime *60000L
        pomodoroTimeMills = newPomodoroTime.toLong()
        selectedTimeInMillis = pomodoroTimeMills

        if (pomodoroTime == 5 ){
            pomodoroTimeText = "0$pomodoroTime:00"
        }
        else{
            pomodoroTimeText = "$pomodoroTime:00"
        }

        binding.timeText.text = pomodoroTimeText

        editor.putInt(keyPomodoroName,newPomodoroTime.toInt())
        editor.putInt("keySecPomodoroName",pomodoroTime)

        //ShortPause İşlemleri
        val newShortPauseTime = shortPauseTime *60000L
        shortPauseTimeMills = newShortPauseTime.toLong()
        if (shortPauseTime == 5 ){
            shortPauseTimeText = "0$shortPauseTime:00"
        }
        else{
            shortPauseTimeText = "$shortPauseTime:00"
        }

        binding.timeText.text = shortPauseTimeText

        editor.putInt(keyShortPauseName,newShortPauseTime.toInt())
        editor.putInt("keySecShortPauseName",shortPauseTime)

        //LongPause İşlemleri

        val newLongPauseTime = longPauseTime *60000L
        shortPauseTimeMills = newShortPauseTime.toLong()
        if (longPauseTime == 5 ){
            longPauseTimeText = "0$longPauseTime:00"
        }
        else{
            longPauseTimeText = "$longPauseTime:00"
        }

        binding.timeText.text = longPauseTimeText

        editor.putInt(keyLongPauseTimeName,newLongPauseTime.toInt())
        editor.putInt("keySecLongPauseName",longPauseTime)

        // Seçilen süreye göre selectedTimeInMillis değerini güncelle
        selectedTimeInMillis = if (restartControl == "pomodoro") {
            newPomodoroTime
        } else if (restartControl == "shortpause") {
            newShortPauseTime
        }
        else {
            newLongPauseTime
        }

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

// Timer görünümünü ayarlar ve arka plan rengini değiştirir
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
        val pomodoroSharedPrefSec = sharedPrefTimeSettings.getInt("keySecPomodoroName",30)
        if (pomodoroSharedPrefSec == 5 ){
            pomodoroTimeText = "0$pomodoroSharedPrefSec:00"
        }
        else{
            pomodoroTimeText = "$pomodoroSharedPrefSec:00"
        }
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

   private fun handleShortPause() {
        val shortPauseSharedPrefTime = sharedPrefTimeSettings.getInt(keyShortPauseName,shortPauseTimeMills.toInt())
        val shortPauseSharedPrefSec = sharedPrefTimeSettings.getInt("keySecShortPauseName",5)

        if (shortPauseSharedPrefSec == 5 ){
            shortPauseTimeText = "0$shortPauseSharedPrefSec:00"
        }
        else{
            shortPauseTimeText = "$shortPauseSharedPrefSec:00"
        }

        binding.timeText.text = shortPauseTimeText

        shortPauseTimeMills = shortPauseSharedPrefTime.toLong()

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
    fun shortPauseOnClick(view: View){
        handleShortPause()
    }

    fun longPauseOnClick(view: View){

        val longPauseSharedPrefTime = sharedPrefTimeSettings.getInt(keyLongPauseTimeName,longPauseTimeMills.toInt())
        val longPauseSharedPrefSec = sharedPrefTimeSettings.getInt("keySecLongPauseName",15)

        if (longPauseSharedPrefSec == 5 ){
            longPauseTimeText = "0$longPauseSharedPrefSec:00"
        }
        else{
            longPauseTimeText = "$longPauseSharedPrefSec:00"
        }

        binding.timeText.text = longPauseTimeText

        longPauseTimeMills = longPauseSharedPrefTime.toLong()


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

    private fun handleOnStartStop(){
        println("onStartclick: "+selectedTimeInMillis)
        if (restartControl== "pomodoro"){
            selectedTimeInMillis = 5000
        }

        mediaPlayer?.start()
        if (!isTimerRunning) {

            startTimer(if (timeLeftInMillis.toInt() == 0) selectedTimeInMillis else timeLeftInMillis)
            binding.refreshBtn.visibility = View.VISIBLE
        } else {
            pauseTimer()
        }
    }
    fun btnOnStartStop(view: View) {

        handleOnStartStop()
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

                if (restartControl == "pomodoro"){
                    handleShortPause()
                    handleOnStartStop()
                }
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
