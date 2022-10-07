package com.kono_click.android.click.Shop

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kono_click.android.click.Constants
import com.kono_click.android.click.databinding.ActivityShopBinding
import com.kono_click.android.click.R
import kotlinx.coroutines.withContext

class ShopActivity : AppCompatActivity() {
    var MagnetLevel:Int =0
    var GoldLevel:Int =0
    var SlowLevel:Int =0
    var MoreLevel:Int =0
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: ActivityShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreference.edit()
        binding.userMoney.text = Constants.UserMoney.toString() + " $"
        setLevels()
        setButtons()

        binding.magnetButton.setOnClickListener {
            buy("magnet")
        }

        // TODO: make on Click listener to all Shop items
    }

    private fun setButtons() {
        MagnetLevel = sharedPreference.getInt("Magnet", 5)
        GoldLevel = sharedPreference.getInt("Gold", 5)
        SlowLevel = sharedPreference.getInt("Slow", 5)
        MoreLevel = sharedPreference.getInt("More", 5)

        binding.magnetButton.text=getMoneyfromlevel(MagnetLevel).toString()+" upgrade"
        binding.goldButton.text=getMoneyfromlevel(MagnetLevel).toString()+" upgrade"
        binding.SlowButton.text=getMoneyfromlevel(MagnetLevel).toString()+" upgrade"
        binding.MoreButton.text=getMoneyfromlevel(MagnetLevel).toString()+" upgrade"

    }

    private fun getMoneyfromlevel(level: Int):Int {
        var numtoreturn=0
        when(level){
            5->numtoreturn=250
            6->numtoreturn=500
            7->numtoreturn=1100
            8->numtoreturn=2500
        }
        return numtoreturn
    }

    private fun setLevels() {
        MagnetLevel = sharedPreference.getInt("Magnet", 5)
        GoldLevel = sharedPreference.getInt("Gold", 5)
        SlowLevel = sharedPreference.getInt("Slow", 5)
        MoreLevel = sharedPreference.getInt("More", 5)
        binding.magnetName.text="Level "+(MagnetLevel-4).toString()
        binding.GoldName.text="Level "+(GoldLevel-4).toString()
        binding.SlowName.text="Level "+(SlowLevel-4).toString()
        binding.MoreName.text="Level "+(MoreLevel-4).toString()
    }




    private fun checkMoney(i: Int): Boolean {
        return i<=Constants.UserMoney
    }


    private fun buy( item: String) {

    }


    }



