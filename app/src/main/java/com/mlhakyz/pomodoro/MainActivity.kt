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
    private var shortPomodoroCycles: Int = 0
    private var longPomodoroCycles: Int = 0
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

        pomodoroTimeText = "${pomodoroSharedPrefSec.toString().padStart(2, '0')}:00"

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

        pomodoroTimeText = "${pomodoroTime.toString().padStart(2, '0')}:00"

        binding.timeText.text = pomodoroTimeText

        editor.putInt(keyPomodoroName,newPomodoroTime.toInt())
        editor.putInt("keySecPomodoroName",pomodoroTime)

        //ShortPause İşlemleri
        val newShortPauseTime = shortPauseTime *60000L
        shortPauseTimeMills = newShortPauseTime.toLong()

        shortPauseTimeText = "${shortPauseTime.toString().padStart(2, '0')}:00"


        binding.timeText.text = shortPauseTimeText

        editor.putInt(keyShortPauseName,newShortPauseTime.toInt())
        editor.putInt("keySecShortPauseName",shortPauseTime)

        //LongPause İşlemleri

        val newLongPauseTime = longPauseTime *60000L
        longPauseTimeMills = newLongPauseTime.toLong()

        longPauseTimeText = "${longPauseTime.toString().padStart(2, '0')}:00"


        binding.timeText.text = longPauseTimeText

        editor.putInt(keyLongPauseTimeName,newLongPauseTime.toInt())
        editor.putInt("keySecLongPauseName",longPauseTime)

        // Seçilen süreye göre selectedTimeInMillis değerini güncelle
        selectedTimeInMillis = when (restartControl) {
            "pomodoro" -> newPomodoroTime
            "shortpause" -> newShortPauseTime
            else -> newLongPauseTime

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
private fun setTimerProperties(btn: View, timeText: String, restartControl: String, selectedTimeInMillis: Long, bgColor: Int, settingsBtnColor: Int,timeTextColor: Int,startBtnColor: Int,startBtnIconColor: Int, refreshBtnColor: Int, refreshBtnIconColor: Int) {

        timer?.cancel()
        timeLeftInMillis = 0
        btn.setBackgroundColor(resources.getColor(Colors.buttonShadowColor))

        binding.timeText.text = timeText
        this.restartControl = restartControl
        this.selectedTimeInMillis = selectedTimeInMillis
        pauseTimer()
        binding.main.setBackgroundColor(getColor(bgColor))
    binding.settingsBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this,timeTextColor))
    binding.timeText.setTextColor(ContextCompat.getColor(this, timeTextColor))
        binding.startPauseBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, startBtnColor))
        binding.startPauseBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, startBtnIconColor))
        binding.refreshBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, refreshBtnColor))
        binding.refreshBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, refreshBtnIconColor))

    val selectedButton = when (btn) {
        binding.pomodoroBtn -> {
            binding.pomodoroBtn.typeface = lftBoldFont
            binding.shortPauseBtn.setBackgroundColor(getColor(Colors.pomodoroColor))
            binding.longPauseBtn.setBackgroundColor(getColor(Colors.pomodoroColor))
            lftMediumFont to listOf(binding.shortPauseBtn, binding.longPauseBtn)
        }
        binding.shortPauseBtn -> {
            binding.shortPauseBtn.typeface = lftBoldFont
            binding.pomodoroBtn.setBackgroundColor(getColor(Colors.shortPauseColor))
            binding.longPauseBtn.setBackgroundColor(getColor(Colors.shortPauseColor))
            lftMediumFont to listOf(binding.pomodoroBtn, binding.longPauseBtn)
        }
        else -> {
            binding.longPauseBtn.typeface = lftBoldFont
            binding.pomodoroBtn.setBackgroundColor(getColor(Colors.longPauseColor))
            binding.shortPauseBtn.setBackgroundColor(getColor(Colors.longPauseColor))
            lftMediumFont to listOf(binding.pomodoroBtn, binding.shortPauseBtn)
        }
    }

    selectedButton.second.forEach { it.typeface = selectedButton.first }

    }

    private fun handlePomodoro(){
        val pomodoroSharedPrefSec = sharedPrefTimeSettings.getInt("keySecPomodoroName",30)
        pomodoroTimeText = "${pomodoroSharedPrefSec.toString().padStart(2, '0')}:00"

        setTimerProperties(
            binding.pomodoroBtn,
            pomodoroTimeText,
            "pomodoro",
            pomodoroTimeMills,
            Colors.pomodoroColor,
            Colors.pomodoroTimeTextColor,
            Colors.pomodoroTimeTextColor,
            Colors.pomodoroStartButtonColor,
            Colors.pomodoroTimeTextColor,
            Colors.pomodoroRefreshButtonColor,
            Colors.pomodoroTimeTextColor
        )
    }

    fun pomodoroOnClick(view:View){
      handlePomodoro()
    }

   private fun handleShortPause() {
        val shortPauseSharedPrefTime = sharedPrefTimeSettings.getInt(keyShortPauseName,shortPauseTimeMills.toInt())
        val shortPauseSharedPrefSec = sharedPrefTimeSettings.getInt("keySecShortPauseName",5)

       shortPauseTimeText = "${shortPauseSharedPrefSec.toString().padStart(2, '0')}:00"

       binding.timeText.text = shortPauseTimeText

        shortPauseTimeMills = shortPauseSharedPrefTime.toLong()

        setTimerProperties(
            binding.shortPauseBtn,
            shortPauseTimeText,
            "shortpause",
            shortPauseTimeMills,
            Colors.shortPauseColor,
            Colors.shortTimeTextColor,
            Colors.shortTimeTextColor,
            Colors.shortStartButtonColor,
            Colors.shortTimeTextColor,
            Colors.shortRefreshButtonColor,
            Colors.shortTimeTextColor
        )

    }
    fun shortPauseOnClick(view: View){
        handleShortPause()
    }

    private fun handleLongPause(){
        val longPauseSharedPrefTime = sharedPrefTimeSettings.getInt(keyLongPauseTimeName,longPauseTimeMills.toInt())
        val longPauseSharedPrefSec = sharedPrefTimeSettings.getInt("keySecLongPauseName",15)

        longPauseTimeText = "${longPauseSharedPrefSec.toString().padStart(2, '0')}:00"

        binding.timeText.text = longPauseTimeText

        longPauseTimeMills = longPauseSharedPrefTime.toLong()


        setTimerProperties(
            binding.longPauseBtn,
            longPauseTimeText,
            "longpause",
            longPauseTimeMills,
            Colors.longPauseColor,
            Colors.longTimeTextColor,
            Colors.longTimeTextColor,
            Colors.longStartButtonColor,
            Colors.longTimeTextColor,
            Colors.longRefreshButtonColor,
            Colors.longTimeTextColor
        )
    }
    fun longPauseOnClick(view: View){
        handleLongPause()

    }

    @Synchronized // belirli iş parçacıkları bloke olmasın diye bu etiketi kullanıyoruz
    private fun handleOnStartStop(){
        println("onStartclick: "+selectedTimeInMillis)

        //Test Short Time
        /*if (restartControl== "pomodoro"){
            selectedTimeInMillis = 5000
        }*/
        selectedTimeInMillis = 5000
        mediaPlayer?.start()
        when {
            !isTimerRunning -> {
                val startTime = if (timeLeftInMillis.toInt() == 0) selectedTimeInMillis else timeLeftInMillis
                startTimer(startTime)
                binding.refreshBtn.visibility = View.VISIBLE
            }
            else -> pauseTimer()
        }
    }
    fun btnOnStartStop(view: View) {
        handleOnStartStop()
    }
   private fun refresh() {
        timer?.cancel()
        timeLeftInMillis = 0

       when(restartControl){
           "pomodoro" -> {
               binding.timeText.text = pomodoroTimeText
               pauseTimer()
               binding.refreshBtn.visibility = View.INVISIBLE
           }
           "shortpause" -> {
               binding.timeText.text = shortPauseTimeText
               pauseTimer()
               binding.refreshBtn.visibility = View.INVISIBLE
           }
           "longpause" -> {
               binding.timeText.text = longPauseTimeText
               pauseTimer()
               binding.refreshBtn.visibility = View.INVISIBLE
           }
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

    private fun startNextTimer() {

        when (restartControl) {
            "pomodoro" -> {
                shortPomodoroCycles++
                if (shortPomodoroCycles == 4) {
                    shortPomodoroCycles = 0
                    handleLongPause()
                } else {
                    handleShortPause()
                }
            }
            "shortpause", "longpause" -> {
                handlePomodoro()
            }
        }
        // Yeni zamanlayıcıyı başlat
        handleOnStartStop()
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
                binding.startPauseBtn.setIconResource(R.drawable.pause)
                startNextTimer() // Yeni zamanlayıcıyı başlat

            }
        }.start()

        isTimerRunning = true
        binding.startPauseBtn.setIconResource(R.drawable.pause)

    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.startPauseBtn.setIconResource(R.drawable.play_icon)
        binding.refreshBtn.visibility = View.INVISIBLE
    }
}

object Colors {
    val pomodoroColor: Int = R.color.pomodoroColor
    val pomodoroRefreshButtonColor: Int = R.color.pomodoroRefreshButtonColor
    val pomodoroStartButtonColor: Int = R.color.pomodoroStartButtonColor
    val pomodoroTimeTextColor: Int = R.color.pomodoroTimeTextColor
    val shortPauseColor: Int = R.color.shortPauseColor
    val shortRefreshButtonColor: Int = R.color.shortRefreshButtonColor
    val shortStartButtonColor: Int = R.color.shortStartButtonColor
    val shortTimeTextColor: Int = R.color.shortTimeTextColor
    val longPauseColor: Int = R.color.longPauseColor
    val longRefreshButtonColor: Int = R.color.longRefreshButtonColor
    val longStartButtonColor: Int = R.color.longStartButtonColor
    val longTimeTextColor: Int = R.color.longTimeTextColor
    val buttonShadowColor: Int = R.color.golge

}
