package com.kono_click.android.click.presentation

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

        binding = ActivityShopBinding.inflate(layoutInflater)

        hideSystemUI()

        bought = MediaPlayer.create(this, R.raw.bullet)
        maxlevel = MediaPlayer.create(this, R.raw.firec)

        setContentView(binding.root)

        sharedPreference = getSharedPreferences(getString(R.string.highscore), Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        setShopOntTimeUseItems()
        setShopAbilityItems()
        setBuyItems()
        setVariables()
        setLevels()
        setButtons()

        binding.userMoney.text = Constants.UserMoney.toString() + " $"

        binding.MagnetItem.itemButton.setOnClickListener {
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

        binding.GoldenItem.itemButton.setOnClickListener {
            if (buy(GoldLevel)) {
                editor.putInt("Gold", ++GoldLevel).commit()
                Constants.GoldLevel = GoldLevel
                setVariables()
                ResetScreenData()

            }
        }

        binding.SlowItem.itemButton.setOnClickListener {
            if(buy(SlowLevel)) {
                editor.putInt("Slow", ++SlowLevel).commit()
                Constants.SlowMotionLevel = SlowLevel
                setVariables()
                //todo
                ResetScreenData()
            }
        }

        binding.MoreMoneyItem.itemButton.setOnClickListener {
            if(buy(MoreLevel)) {
                editor.putInt("More", ++MoreLevel).commit()
                Constants.MoreMoneyLevel = MoreLevel
                setVariables()
                ResetScreenData()
            }
        }

    }




    private fun setBuyItems() {
        binding.AllGolden.itemButton.setOnClickListener {
             buyOneTimeItem(2000,2)
        }
        binding.addTen.itemButton.setOnClickListener {
            buyOneTimeItem(600,1)
        }
    }

    private fun buyOneTimeItem(coast : Int , itemNumber : Int){
        if (checkMoney(coast)){
            if (sound) {
                if (bought.isPlaying)
                    bought.seekTo(0)
                bought.start()
            }
            cutFromShared(coast)

            if (itemNumber == 2){
            var allGolden = sharedPreference.getLong("AllGolden",0)+1
            editor.putLong("AllGolden", allGolden.toLong()).commit()
                Constants.AllGolden=allGolden.toInt()
            }else if (itemNumber == 1){
                var tenSec = sharedPreference.getLong("TenSec",0)+1
                editor.putLong("TenSec", tenSec.toLong()).commit()
                Constants.tenSec=tenSec.toInt()
            }
            ResetScreenData()
            setShopOntTimeUseItems()
        }else{
            if (!toast2) {
                Toast.makeText(this, "No Money", Toast.LENGTH_SHORT).show()
                toast2 = true
            }
            if (sound) {
                if (maxlevel.isPlaying)
                    maxlevel.seekTo(0)
                maxlevel.start()
            }
        }
    }
    private fun setShopOntTimeUseItems() {
        // add 10 to one use game
        binding.addTen.itemImage.setImageResource(R.drawable.ic_baseline_timer_10_24)
        binding.addTen.itemName.text="add 10 S"
        binding.addTen.itemButton.text="600$"
        binding.addTen.itemCount.text=sharedPreference.getLong("TenSec",0).toString()

        // all golden for 10k
        binding.AllGolden.itemImage.setImageResource(R.drawable.ic_baseline_monetization_on_24)
        binding.AllGolden.itemName.text="All Golden"
        binding.AllGolden.itemButton.text="2000$"
        binding.AllGolden.itemCount.text=sharedPreference.getLong("AllGolden",0).toString()


    }

    private fun setShopAbilityItems() {
     //magnet
        binding.MagnetItem.itemImage.setImageResource(R.drawable.ic_magnet2)
        binding.MagnetItem.itemCount.visibility=View.GONE

     //golden
        binding.GoldenItem.itemImage.setImageResource(R.drawable.ic_golden_dollar)
        binding.GoldenItem.itemCount.visibility=View.GONE

        //slow

        binding.SlowItem.itemImage.setImageResource(R.drawable.ic_slow)
        binding.SlowItem.itemCount.visibility=View.GONE
     //more money

        binding.MoreMoneyItem.itemImage.setImageResource(R.drawable.ic_more_money)
        binding.MoreMoneyItem.itemCount.visibility=View.GONE
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
            binding.MagnetItem.itemButton.text = "Can't"
        } else {
            binding.MagnetItem.itemButton.text = getMoneyfromlevel(MagnetLevel).toString() + " $"
        }
        if (GoldLevel >= 9) {
            binding.GoldenItem.itemButton.text = "Can't"
        } else {
            binding.GoldenItem.itemButton.text = getMoneyfromlevel(GoldLevel).toString() + " $"
        }
        if (SlowLevel >= 9) {
            binding.SlowItem.itemButton.text = "Can't"
        } else {
            binding.SlowItem.itemButton.text = getMoneyfromlevel(SlowLevel).toString() + " $"
        }
        if (MoreLevel >= 9) {
            binding.MoreMoneyItem.itemButton.text = "Can't"
        } else {
            binding.MoreMoneyItem.itemButton.text = getMoneyfromlevel(MoreLevel).toString() + " $"
        }
    }

    private fun getMoneyfromlevel(level: Int):Int {
        var numtoreturn=0
        when(level){
            5->numtoreturn=200
            6->numtoreturn=500
            7->numtoreturn=1500
            8 -> numtoreturn = 3000
            9 -> 0
        }
        return numtoreturn
    }

    private fun setLevels() {
        if (MagnetLevel >= 9) {
            binding.MagnetItem.itemName.text = "MAX"
        } else {
            binding.MagnetItem.itemName.text = "Level " + (MagnetLevel - 4).toString()
        }
        if (GoldLevel >= 9) {
            binding.GoldenItem.itemName.text = "MAX"
        } else {
            binding.GoldenItem.itemName.text = "Level " + (GoldLevel - 4).toString()
        }
        if (SlowLevel >= 9) {
            binding.SlowItem.itemName.text = "MAX"
        } else {
            binding.SlowItem.itemName.text = "Level " + (SlowLevel - 4).toString()
        }
        if (MoreLevel >= 9) {
            binding.MoreMoneyItem.itemName.text = "MAX"
        } else {
            binding.MoreMoneyItem.itemName.text = "Level " + (MoreLevel - 4).toString()
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
        if (level == 5) priceToReturn = 200
        else if (level == 6) priceToReturn = 500
        else if (level == 7) priceToReturn = 1500
        else if (level == 8) priceToReturn = 3000
        else if (level == 9) priceToReturn = 0
        return priceToReturn
    }

    private fun hideSystemUI() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.shopBackGround)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,constraintLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


}