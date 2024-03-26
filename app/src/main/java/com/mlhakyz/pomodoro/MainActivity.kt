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
    private var timeLeftInMillis: Long = 1500000 // 25 dakika
    private lateinit var lftMediumFont: Typeface
    private lateinit var lftBoldFont: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lftMediumFont = ResourcesCompat.getFont(this, R.font.ltfmedium)!!
        lftBoldFont = ResourcesCompat.getFont(this, R.font.ltfbold)!!


    }

        fun pomodoroOnClick(view:View){
            // Button 1'e tıklandığında yapılacak işlemler
            binding.pomodoroBtn.setBackgroundColor(resources.getColor(R.color.golge))
            binding.pomodoroBtn.typeface = lftBoldFont

            // Diğer düğmelerin arka plan rengini varsayılan değere döndür
            binding.shortBreakBtn.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.longBreakBtn.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.shortBreakBtn.typeface = lftMediumFont
            binding.longBreakBtn.typeface = lftMediumFont

            //Arka Plan ve Count Down Timer Ayarlama
            binding.main.setBackgroundColor(getColor(R.color.pomodoroColor))
            binding.startPauseBtn.setTextColor(getColor(R.color.pomodoroColor))
        }



    fun shortBreakOnClick(view: View){
        // Button 2'ye tıklandığında yapılacak işlemler
        binding.shortBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
        binding.shortBreakBtn.typeface = lftBoldFont

        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.longBreakBtn.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.pomodoroBtn.typeface = lftMediumFont
        binding.longBreakBtn.typeface = lftMediumFont

        //Arka Plan ve Count Down Timer Ayarlama
        binding.main.setBackgroundColor(getColor(R.color.shortPauseColor))
        binding.startPauseBtn.setTextColor(getColor(R.color.shortPauseColor))

    }

    fun longBreakOnClick(view: View){
        // Button 3'e tıklandığında yapılacak işlemler
        binding.longBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
        binding.longBreakBtn.typeface = lftBoldFont

        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.shortBreakBtn.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.pomodoroBtn.typeface = lftMediumFont
        binding.shortBreakBtn.typeface = lftMediumFont


        //Arka Plan ve Count Down Timer Ayarlama
        binding.main.setBackgroundColor(getColor(R.color.longPauseColor))
        binding.startPauseBtn.setTextColor(getColor(R.color.longPauseColor))

    }



        fun btnOnStartStop(view: View) {
            if (!isTimerRunning) {
                startTimer()
            } else {
                pauseTimer()
            }
        }


    private fun startTimer(){
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(p0: Long) {
                timeLeftInMillis = p0
                binding.textView.text = timeFormat.format(p0)
            }

            override fun onFinish() {
                binding.textView.text = "00:00"
                isTimerRunning = false
                binding.startPauseBtn.text = "Start"
            }
        }.start()

        isTimerRunning = true
        binding.startPauseBtn.text = "Pause"

    }
    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.startPauseBtn.text = "Start"
    }

}