package com.example.android.click

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android.click.databinding.ActivityInfoBinding

class info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = Constants.scoree.toInt()
        val highScore = Constants.HighScore.toInt()

        //  Toast.makeText(this,"score"+ Constants.scoree + " h "+ Constants.HighScore,Toast.LENGTH_SHORT).show()

        if (score > highScore) {
            binding.recordimg.setImageResource(R.drawable.new_record)
            binding.text1.text = "Good Job"
            binding.text2.text = "New Record"
            binding.text3.text = "Try to Break it"
        } else {
            binding.recordimg.setImageResource(R.drawable.goodjob)
            binding.text1.text = "Good Job"
            binding.text2.text = "But you can Break your record"
            binding.text3.text = "Try to Break it"

        }

        binding.button.setOnClickListener {
            onBackPressed()
        }


    }
}