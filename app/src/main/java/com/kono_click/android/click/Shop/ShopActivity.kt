package com.kono_click.android.click.Shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kono_click.android.click.Constants
import com.kono_click.android.click.databinding.ActivityShopBinding
import com.google.android.material.snackbar.Snackbar

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        changeWallPaper()
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.userMoney.text = Constants.UserMoney.toString() + " $"


        // TODO: make on Click listener to all Shop items
    }

    private fun changeWallPaper() {
        TODO("Not yet implemented")
    }

    private fun buy(money: Long, skin: String) {
        if (Constants.UserMoney >= money) {
            Constants.UserMoney -= money

            //todo:Open Skin to be used

        } else {
            Snackbar.make(binding.goldCard, "Not Enough Money", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun Upgrade(ability: String, currentLevel: Int, maxLevel: Int, money: Int) {
        if (Constants.UserMoney < money) {
            Snackbar.make(binding.goldCard, "Not Enough Money", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (currentLevel == maxLevel - 1) {
            Constants.UserMoney -= money
            // TODO: now it is max so we need to remove upgrade button
        } else {
            // TODO: upgrade the ability to next level
        }

    }


}
