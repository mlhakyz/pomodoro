package com.mlhakyz.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mlhakyz.pomodoro.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



    }

    fun btnOnStartStop (view: View){
   object :CountDownTimer(1500000,1000){

            override fun onTick(p0: Long) {

                binding.textView.text = "${p0}"
            }

            override fun onFinish() {
                binding.textView.text = "0:00"
            }

        }








    }


}