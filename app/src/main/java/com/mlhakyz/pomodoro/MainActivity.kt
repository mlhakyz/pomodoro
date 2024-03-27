package com.mlhakyz.pomodoro

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding

import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lftMediumFont = ResourcesCompat.getFont(this, R.font.ltfmedium)!!
        lftBoldFont = ResourcesCompat.getFont(this, R.font.ltfbold)!!
        selectedTimeInMillis = pomodoroTimeMills
    }

    fun pomodoroOnClick(view:View){
            timer?.cancel()
            timeLeftInMillis = 0
            // Button 1'e tıklandığında yapılacak işlemler
            binding.pomodoroBtn.setBackgroundColor(resources.getColor(R.color.golge))
            binding.pomodoroBtn.typeface = lftBoldFont
            binding.textView.text = "25:00"
            selectedTimeInMillis = pomodoroTimeMills
        pauseTimer()
            //Arka Plan ve Count Down Timer Ayarlama
            binding.main.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.startPauseBtn.setTextColor(getColor(R.color.pomodoroColor))

            // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.refreshBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pomodoroColor))

        binding.shortBreakBtn.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.longBreakBtn.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.shortBreakBtn.typeface = lftMediumFont
            binding.longBreakBtn.typeface = lftMediumFont

    }

    fun shortBreakOnClick(view: View){
        timer?.cancel()
        timeLeftInMillis = 0
        // Button 2'ye tıklandığında yapılacak işlemler
        binding.shortBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
        binding.shortBreakBtn.typeface = lftBoldFont
        binding.textView.text = "05:00"
        selectedTimeInMillis = shortPauseTimeMills
        pauseTimer()
        //Arka Plan ve Count Down Timer Ayarlama
        binding.main.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.startPauseBtn.setTextColor(getColor(R.color.shortPauseColor))

        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.refreshBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.shortPauseColor))

        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.longBreakBtn.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.pomodoroBtn.typeface = lftMediumFont
        binding.longBreakBtn.typeface = lftMediumFont


    }

    fun longBreakOnClick(view: View){
        timer?.cancel()
        timeLeftInMillis = 0
        // Button 3'e tıklandığında yapılacak işlemler
        binding.longBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
        binding.longBreakBtn.typeface = lftBoldFont
        binding.textView.text = "15:00"
        selectedTimeInMillis = longPauseTimeMills
      pauseTimer()
        //Arka Plan ve Count Down Timer Ayarlama
        binding.main.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.startPauseBtn.setTextColor(getColor(R.color.longPauseColor))

        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.refreshBtn.iconTint = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.longPauseColor))

        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.shortBreakBtn.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.pomodoroBtn.typeface = lftMediumFont
        binding.shortBreakBtn.typeface = lftMediumFont


    }

    fun btnOnStartStop(view: View) {
        if (!isTimerRunning) {

            startTimer(if (timeLeftInMillis.toInt() == 0) selectedTimeInMillis else timeLeftInMillis)
            binding.refreshBtn.visibility = View.VISIBLE
        } else {
                pauseTimer()
        }
    }


    private fun startTimer(durationInMillis: Long){
        timer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(p0: Long) {
                timeLeftInMillis = p0 // Geri sayım süresini güncelle
                binding.textView.text = timeFormat.format(p0)
            }

            override fun onFinish() {
                binding.textView.text = "00:00"
                isTimerRunning = false
                binding.startPauseBtn.text = "START"
            }
        }.start()

        isTimerRunning = true
        binding.startPauseBtn.text = "PAUSE"

    }
    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.startPauseBtn.text = "START"
        binding.refreshBtn.visibility = View.INVISIBLE
    }

}