package com.kono_click.android.click.Shop

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kono_click.android.click.Constants
import com.kono_click.android.click.Constants.sound
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.ActivityShopBinding

class ShopActivity : AppCompatActivity() {

    var MagnetLevel: Int = 0
    var GoldLevel: Int = 0
    var SlowLevel: Int = 0
    var MoreLevel: Int = 0

    val max_Level = 9

    //toasts
    var toast1 = false
    var toast2 = false

    private lateinit var bought: MediaPlayer
    private lateinit var maxlevel: MediaPlayer


    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private lateinit var binding: ActivityShopBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bought = MediaPlayer.create(this, R.raw.bullet)
        maxlevel = MediaPlayer.create(this, R.raw.firec)

        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreference = getSharedPreferences(getString(R.string.highscore), Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        setVariables()
        setLevels()
        setButtons()

        binding.userMoney.text = Constants.UserMoney.toString() + " $"

        binding.magnetButton.setOnClickListener {
            if(buy(MagnetLevel)) {
                editor.putInt("Magnet", ++MagnetLevel).commit()
                Constants.MagmetLevel = MagnetLevel
                setVariables()
                if (MagnetLevel == 7) {
                    Constants.MagmetLevel = 8
                } else if (MagnetLevel == 8) {
                    Constants.MagmetLevel = 11
                } else if (MagnetLevel == 9) {
                    Constants.MagmetLevel = 15
                }
                ResetScreenData()
            }
        }

        binding.goldButton.setOnClickListener {
            if (buy(GoldLevel)) {
                editor.putInt("Gold", ++GoldLevel).commit()
                Constants.GoldLevel = GoldLevel
                setVariables()
                ResetScreenData()

            }
        }

        binding.SlowButton.setOnClickListener {
            if(buy(SlowLevel)) {
                editor.putInt("Slow", ++SlowLevel).commit()
                Constants.SlowMotionLevel = SlowLevel
                setVariables()
                //todo
                ResetScreenData()
            }
        }

        binding.MoreButton.setOnClickListener {
            if(buy(MoreLevel)) {
                editor.putInt("More", ++MoreLevel).commit()
                Constants.MoreMoneyLevel = MoreLevel
                setVariables()
                //todo
                ResetScreenData()
            }
        }

    }

    private fun ResetScreenData() {
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
        if (MagnetLevel >= 9) {
            binding.magnetButton.text = "Can't"
        } else {
            binding.magnetButton.text = getMoneyfromlevel(MagnetLevel).toString() + " $"
        }
        if (GoldLevel >= 9) {
            binding.goldButton.text = "Can't"
        } else {
            binding.goldButton.text = getMoneyfromlevel(GoldLevel).toString() + " $"
        }
        if (SlowLevel >= 9) {
            binding.SlowButton.text = "Can't"
        } else {
            binding.SlowButton.text = getMoneyfromlevel(SlowLevel).toString() + " $"
        }
        if (MoreLevel >= 9) {
            binding.MoreButton.text = "Can't"
        } else {
            binding.MoreButton.text = getMoneyfromlevel(MoreLevel).toString() + " $"
        }
    }

    private fun getMoneyfromlevel(level: Int):Int {
        var numtoreturn=0
        when(level){
            5->numtoreturn=1000
            6->numtoreturn=2500
            7->numtoreturn=5000
            8 -> numtoreturn = 10000
            9 -> 0
        }
        return numtoreturn
    }

    private fun setLevels() {
        if (MagnetLevel >= 9) {
            binding.magnetName.text = "MAX"
        } else {
            binding.magnetName.text = "Level " + (MagnetLevel - 4).toString()
        }
        if (GoldLevel >= 9) {
            binding.GoldName.text = "MAX"
        } else {
            binding.GoldName.text = "Level " + (GoldLevel - 4).toString()
        }
        if (SlowLevel >= 9) {
            binding.SlowName.text = "MAX"
        } else {
            binding.SlowName.text = "Level " + (SlowLevel - 4).toString()
        }
        if (MoreLevel >= 9) {
            binding.MoreName.text = "MAX"
        } else {
            binding.MoreName.text = "Level " + (MoreLevel - 4).toString()
        }
    }

    private fun checkMoney(i: Int): Boolean = i <= Constants.UserMoney

    private fun buy(level: Int): Boolean {

        if (level >= 9) {
            if (!toast1) {
                Toast.makeText(this, "Max", Toast.LENGTH_SHORT).show()
                toast1 = true
            }
            if (sound) {
                if (maxlevel.isPlaying)
                    maxlevel.seekTo(0)
                maxlevel.start()
            }
            return false
        } else {

            val nextUpgradeCoast = getCurrentUpgradeCoast(level)
            if (checkMoney(nextUpgradeCoast) && nextUpgradeCoast != 0) {
                cutFromShared(nextUpgradeCoast)
                if (sound) {
                    if (bought.isPlaying)
                        bought.seekTo(0)
                    bought.start()
                }
                return true
            } else {
                if (!toast2) {
                    Toast.makeText(this, "No Money", Toast.LENGTH_SHORT).show()
                    toast2 = true
                }
                if (sound) {
                    if (maxlevel.isPlaying)
                        maxlevel.seekTo(0)
                    maxlevel.start()
                }
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
        else if (level == 6) priceToReturn = 2500
        else if (level == 7) priceToReturn = 5000
        else if (level == 8) priceToReturn = 10000
        else if (level == 9) priceToReturn = 0
        return priceToReturn
    }

}