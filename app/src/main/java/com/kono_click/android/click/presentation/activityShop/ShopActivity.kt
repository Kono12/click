package com.kono_click.android.click.presentation.activityShop

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kono_click.android.click.utils.Constants
import com.kono_click.android.click.utils.Constants.sound
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.ActivityShopBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopActivity : AppCompatActivity() {

    var magnetLevel: Int = 0
    var goldLevel: Int = 0
    var slowLevel: Int = 0
    var moreLevel: Int = 0

    var toast1 = false
    var toast2 = false

    private lateinit var bought: MediaPlayer
    private lateinit var maxlevel: MediaPlayer

    private val viewModel: ShopViewModel by viewModels()

    private lateinit var binding: ActivityShopBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        hideSystemUI()
        setSounds()
        setContentView(binding.root)
        setOnClicks()
        setShopOntTimeUseItems()
        setShopAbilityItems()
        setBuyItems()
        setVariables()
        setLevels()
        setButtons()

        binding.userMoney.text = Constants.UserMoney.toString() + " $"

        binding.MagnetItem.itemButton.setOnClickListener {
            if (buy(magnetLevel)) {
                viewModel.setMagnetLevel(++magnetLevel)
                Constants.MagmetLevel = magnetLevel
                setVariables()
                viewModel.saveMagnetLevelToConstants(magnetLevel)
                resetScreenData()
            }
        }

        binding.GoldenItem.itemButton.setOnClickListener {
            if (buy(goldLevel)) {
                viewModel.setGoldLevel(++goldLevel)
                Constants.GoldLevel = goldLevel
                setVariables()
                resetScreenData()
            }
        }

        binding.SlowItem.itemButton.setOnClickListener {
            if (buy(slowLevel)) {
                viewModel.setSlowMotionLevel(++slowLevel)
                Constants.SlowMotionLevel = slowLevel
                setVariables()
                resetScreenData()
            }
        }

        binding.MoreMoneyItem.itemButton.setOnClickListener {
            if (buy(moreLevel)) {
                viewModel.setMoreMoneyLevel(++moreLevel)
                Constants.MoreMoneyLevel = moreLevel
                setVariables()
                resetScreenData()
            }
        }
    }

    private fun setSounds() {
        bought = MediaPlayer.create(this, R.raw.bullet)
        maxlevel = MediaPlayer.create(this, R.raw.firec)
    }

    private fun setOnClicks() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setBuyItems() {
        binding.AllGolden.itemButton.setOnClickListener {
            buyOneTimeItem(1600, 2)
        }
        binding.addTen.itemButton.setOnClickListener {
            buyOneTimeItem(400, 1)
        }
    }

    private fun buyOneTimeItem(coast: Int, itemNumber: Int) {
        if (viewModel.checkMoney(coast)) {
            if (sound) {
                if (bought.isPlaying)
                    bought.seekTo(0)
                bought.start()
            }
            viewModel.cutFromShared(coast)
            viewModel.buyOneTimeItem(itemNumber)
            resetScreenData()
            setShopOntTimeUseItems()

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
        }
    }

    private fun setShopOntTimeUseItems() {
        // add 10 to one use game
        binding.addTen.itemImage.setImageResource(R.drawable.ic_baseline_timer_10_24)
        binding.addTen.itemName.text = "add 10 S"
        binding.addTen.itemButton.text = "400$"
        binding.addTen.itemCount.text = viewModel.getNumberOfTenSecTokens().toString()

        // all golden for game
        binding.AllGolden.itemImage.setImageResource(R.drawable.ic_baseline_monetization_on_24)
        binding.AllGolden.itemName.text = "All Golden"
        binding.AllGolden.itemButton.text = "1600$"
        binding.AllGolden.itemCount.text = viewModel.getNumberOfAllGoldenTokens().toString()


    }

    private fun setShopAbilityItems() {
        //magnet
        binding.MagnetItem.itemImage.setImageResource(R.drawable.ic_magnet2)
        binding.MagnetItem.itemCount.visibility = View.GONE

        //golden
        binding.GoldenItem.itemImage.setImageResource(R.drawable.ic_golden_dollar)
        binding.GoldenItem.itemCount.visibility = View.GONE

        //slow

        binding.SlowItem.itemImage.setImageResource(R.drawable.ic_slow)
        binding.SlowItem.itemCount.visibility = View.GONE
        //more money

        binding.MoreMoneyItem.itemImage.setImageResource(R.drawable.ic_more_money)
        binding.MoreMoneyItem.itemCount.visibility = View.GONE
    }

    private fun resetScreenData() {
        setButtons()
        setLevels()
        binding.userMoney.text = Constants.UserMoney.toString() + " $"
    }

    private fun setVariables() {
        magnetLevel = viewModel.getMagnetLevel()
        goldLevel = viewModel.getGoldLevel()
        slowLevel = viewModel.getSlowMotionLevel()
        moreLevel = viewModel.getMoreMoneyLevel()
    }

    private fun setButtons() {
        if (magnetLevel >= 9) {
            binding.MagnetItem.itemButton.text = "Can't"
        } else {
            binding.MagnetItem.itemButton.text = viewModel.getMoneyfromlevel(magnetLevel).toString() + " $"
        }
        if (goldLevel >= 9) {
            binding.GoldenItem.itemButton.text = "Can't"
        } else {
            binding.GoldenItem.itemButton.text = viewModel.getMoneyfromlevel(goldLevel).toString() + " $"
        }
        if (slowLevel >= 9) {
            binding.SlowItem.itemButton.text = "Can't"
        } else {
            binding.SlowItem.itemButton.text = viewModel.getMoneyfromlevel(slowLevel).toString() + " $"
        }
        if (moreLevel >= 9) {
            binding.MoreMoneyItem.itemButton.text = "Can't"
        } else {
            binding.MoreMoneyItem.itemButton.text = viewModel.getMoneyfromlevel(moreLevel).toString() + " $"
        }
    }
    private fun setLevels() {
        if (magnetLevel >= 9) {
            binding.MagnetItem.itemName.text = "MAX"
        } else {
            binding.MagnetItem.itemName.text = "Level " + (magnetLevel - 4).toString()
        }
        if (goldLevel >= 9) {
            binding.GoldenItem.itemName.text = "MAX"
        } else {
            binding.GoldenItem.itemName.text = "Level " + (goldLevel - 4).toString()
        }
        if (slowLevel >= 9) {
            binding.SlowItem.itemName.text = "MAX"
        } else {
            binding.SlowItem.itemName.text = "Level " + (slowLevel - 4).toString()
        }
        if (moreLevel >= 9) {
            binding.MoreMoneyItem.itemName.text = "MAX"
        } else {
            binding.MoreMoneyItem.itemName.text = "Level " + (moreLevel - 4).toString()
        }
    }
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
            val nextUpgradeCoast = viewModel.getCurrentUpgradeCoast(level)
            if (viewModel.checkMoney(nextUpgradeCoast) && nextUpgradeCoast != 0) {
                viewModel.cutFromShared(nextUpgradeCoast)
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
    private fun hideSystemUI() {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.shopBackGround)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, constraintLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}