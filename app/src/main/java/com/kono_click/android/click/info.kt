package com.kono_click.android.click

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kono_click.android.click.databinding.ActivityInfoBinding

class info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        hideSystemUI()

        setContentView(binding.root)
        setDataAmount()
        setDataMoney()
        Constants.resetData()
        val score = Constants.scoree.toInt()
        val highScore = Constants.HighScore.toInt()
        binding.score.text=score.toString()+" $"
        //  Toast.makeText(this,"score"+ Constants.scoree + " h "+ Constants.HighScore,Toast.LENGTH_SHORT).show()

        if (score > highScore) {
            binding.recordimg.setImageResource(R.drawable.new_record)

        } else {
            binding.recordimg.setImageResource(R.drawable.new_record)

        }

        binding.button.setOnClickListener {
            onBackPressed()
        }

    }


    private fun setDataMoney() {
        binding.normalMoney.text=Constants.normalMoey.toString() +" $"
        binding.goldMoney.text=Constants.GoldenMoney.toString()+" $"
        binding.magnetMoney.text=Constants.MagnetMoney.toString()+" $"
//        binding.slowMoney.text=Constants.SlowMoney.toString()+" $"
//        binding.moreMoneyMoney.text=Constants.moreMoneyMoney.toString()+" $"
//        binding.bigHitMoney.text=Constants.BigHitMoney.toString()+" $"
    }

    private fun setDataAmount() {
        binding.moneyAmount.text=Constants.normalMoey.toString()
        binding.goldAmount.text=Constants.GoldenAmount.toString()
        binding.magnetAmount.text=Constants.MagnetAmount.toString()
        binding.slowAmount.text=Constants.SlowAmount.toString()
        binding.moreMoneyAmount.text=Constants.MoreMoneyAmount.toString()
        binding.bigHitAmount.text=Constants.BigHitAmount.toString()
    }

    private fun hideSystemUI() {
        val constraintLayout = findViewById<ScrollView>(R.id.infoBackGround)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,constraintLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}
