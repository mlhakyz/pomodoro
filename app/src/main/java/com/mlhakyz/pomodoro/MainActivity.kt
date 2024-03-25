package com.mlhakyz.pomodoro

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val timeFormat: SimpleDateFormat = SimpleDateFormat("mm:ss")
    private var isTimerRunning: Boolean = false
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 1500000 // 25 dakika

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




    }

        fun pomodoroOnClick(view:View){
            // Button 1'e tıklandığında yapılacak işlemler
            changeButtonBackground(binding.pomodoroBtn)
            // Diğer düğmelerin arka plan rengini varsayılan değere döndür
            binding.shortBreakBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))
            binding.longBreakBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))
            // Button 1'e özgü işlemler
            binding.pomodoroBtn.setBackgroundColor(resources.getColor(R.color.golge))
        }



    fun shortBreakOnClick(view: View){
        // Button 2'ye tıklandığında yapılacak işlemler
        changeButtonBackground(binding.shortBreakBtn)
        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))
        binding.longBreakBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))
        // Button 2'ye özgü işlemler
        binding.shortBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
    }

    fun longBreakOnClick(view: View){
        // Button 3'e tıklandığında yapılacak işlemler
        changeButtonBackground(binding.longBreakBtn)

        // Diğer düğmelerin arka plan rengini varsayılan değere döndür
        binding.pomodoroBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))
        binding.shortBreakBtn.setBackgroundColor(getColor(R.color.HomebackgroundColorkoyu))

        // Button 3'e özgü işlemler

        binding.longBreakBtn.setBackgroundColor(resources.getColor(R.color.golge))
    }
    private fun changeButtonBackground(button: Button) {
        // Tıklanan düğmenin arkaplan rengini değiştir
        button.setBackgroundColor(getColor(R.color.red))
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
                binding.pomodoroBtn.text = "Start"
            }
        }.start()

        isTimerRunning = true
        binding.pomodoroBtn.text = "Pause"

    }
    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.pomodoroBtn.text = "Start"
    }

}