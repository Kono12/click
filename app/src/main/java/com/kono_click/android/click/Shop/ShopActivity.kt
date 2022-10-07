package com.kono_click.android.click.Shop

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kono_click.android.click.Constants
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.ActivityShopBinding

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

        sharedPreference = getSharedPreferences(getString(R.string.highscore), Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        setVariables()
        setLevels()
        setButtons()

        binding.userMoney.text = Constants.UserMoney.toString() + " $"

        binding.magnetButton.setOnClickListener {
            if(buy(MagnetLevel)){
                editor.putInt("Magnet",++MagnetLevel).commit()
                Constants.MagmetLevel=MagnetLevel
                ResetScreenData()
            }
        }

        binding.goldButton.setOnClickListener {
            if(buy(GoldLevel)){
                editor.putInt("Gold",++GoldLevel).commit()
                Constants.GoldLevel=GoldLevel
                ResetScreenData()
            }
        }

        binding.SlowButton.setOnClickListener {
            if(buy(SlowLevel)){
                editor.putInt("Slow",++SlowLevel).commit()
                Constants.SlowMotionLevel=SlowLevel
                ResetScreenData()
            }
        }

        binding.MoreButton.setOnClickListener {
            if(buy(MoreLevel)){
                editor.putInt("More",++MoreLevel).commit()
                Constants.MoreMoneyLevel=MoreLevel
                ResetScreenData()
            }
        }

    }

    private fun ResetScreenData() {
        setVariables()
        setButtons()
        setLevels()
        binding.userMoney.text = Constants.UserMoney.toString() + " $"
    }

    private fun setVariables() {
        MagnetLevel = sharedPreference.getInt("Magnet",5)
        GoldLevel = sharedPreference.getInt("Gold", 5)
        SlowLevel = sharedPreference.getInt("Slow", 5)
        MoreLevel = sharedPreference.getInt("More", 5)
    }

    private fun setButtons() {
        binding.magnetButton.text = getMoneyfromlevel(MagnetLevel).toString() + " $"
        binding.goldButton.text = getMoneyfromlevel(GoldLevel).toString() + " $"
        binding.SlowButton.text = getMoneyfromlevel(SlowLevel).toString() + " $"
        binding.MoreButton.text = getMoneyfromlevel(MoreLevel).toString() + " $"
    }

    private fun getMoneyfromlevel(level: Int):Int {
        var numtoreturn=0
        when(level){
            5->numtoreturn=1000
            6->numtoreturn=3000
            7->numtoreturn=8000
            8->numtoreturn=15000
        }
        return numtoreturn
    }

    private fun setLevels() {
        binding.magnetName.text="Level "+(MagnetLevel-4).toString()
        binding.GoldName.text="Level "+(GoldLevel-4).toString()
        binding.SlowName.text="Level "+(SlowLevel-4).toString()
        binding.MoreName.text="Level "+(MoreLevel-4).toString()
    }

    private fun checkMoney(i: Int): Boolean = i <= Constants.UserMoney

    private fun buy(level: Int): Boolean {

        if (level >= 9) {
            Toast.makeText(this, "Max", Toast.LENGTH_SHORT).show()
            return false
        } else {

            val nextUpgradeCoast = getCurrentUpgradeCoast(level)
           if (checkMoney(nextUpgradeCoast)){
               cutFromShared(nextUpgradeCoast)
               return true
           }else{
               Toast.makeText(this, "No Money", Toast.LENGTH_SHORT).show()
               return false
           }

        }

    }

    private fun cutFromShared(nextUpgradeCoast: Int) {
        var i = sharedPreference.getLong("UserMoney", 0)
        i-=nextUpgradeCoast
        editor.putLong("UserMoney",i).commit()
        Constants.UserMoney=i
    }

    private fun getCurrentUpgradeCoast(level: Int): Int {
        var priceToReturn = 0
        if (level == 5) priceToReturn = 1000
        else if (level == 6) priceToReturn = 3000
        else if (level == 7) priceToReturn = 8000
        else if (level == 8) priceToReturn = 15000
        return priceToReturn
    }

}