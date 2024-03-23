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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



    }

    fun btnOnStartStop (view: View){
   object :CountDownTimer(1500000,1000){

            override fun onTick(p0: Long) {
                var   milliLeft=p0;
                var min = (p0/(1000*60));
                var  sec = ((p0/1000)-min*60);
                binding.textView.text = "${timeFormat.format(p0)}"
            }

            override fun onFinish() {
                binding.textView.text = "0:00"
            }

        }








    }


}