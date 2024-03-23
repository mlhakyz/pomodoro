package com.mlhakyz.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
                binding.button.text = "Start"
            }
        }.start()

        isTimerRunning = true
        binding.button.text = "Pause"

    }
    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.button.text = "Start"
    }

}