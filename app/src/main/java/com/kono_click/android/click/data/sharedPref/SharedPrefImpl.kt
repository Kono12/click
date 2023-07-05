package com.kono_click.android.click.data.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.kono_click.android.click.R

class SharedPrefImpl(context: Context) {

    private lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    init {
        sharedPreference = context.getSharedPreferences(
            context.getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreference.edit()

    }

    //getters
    fun getUserMoney() = sharedPreference.getLong("UserMoney", 0)
    fun getHighScore() = sharedPreference.getInt("high", 0)

    fun getSound() = sharedPreference.getBoolean("sound", true)

    fun getAllGolden() = sharedPreference.getLong("AllGolden", 0).toInt()
    fun getTenSec() = sharedPreference.getLong("TenSec", 0).toInt()
    fun getIsAllGolden() = sharedPreference.getBoolean("UseGolden", false)
    fun getIsTenSec() = sharedPreference.getBoolean("UseTenSec", false)

    //levels
    fun getMagnetLevel() = sharedPreference.getInt("Magnet", 5)
    fun getGoldLevel() = sharedPreference.getInt("Gold", 5)
    fun getSlowMotionLevel() = sharedPreference.getInt("Slow", 5)
    fun getMoreMoneyLevel() = sharedPreference.getInt("More", 5)

    //setters

    fun setHighScore(score: Int) {
        editor.putInt("high", score).commit()
    }

    fun setUserMoney(money: Long) {
        editor.putLong("UserMoney", money).commit()
    }

    fun setSound(isOn: Boolean) {
        editor.putBoolean("sound", false).commit()
    }

    fun setIsTenSec(useTenSec: Boolean) {
        editor.putBoolean("UseTenSec", useTenSec).commit()
    }

    fun setIsAllGolden(useGolden: Boolean) {
        editor.putBoolean("UseGolden", useGolden).commit()
    }

    fun setAllGold(numberOFAllGoldenTokens: Long) {
        editor.putLong("AllGolden", numberOFAllGoldenTokens).commit()
    }

    fun setTenSec(numberOFTenSecTokens: Long) {
        editor.putLong("TenSec", numberOFTenSecTokens).commit()
    }

    //levels

    fun setMagnetLevel(level: Int) {
        editor.putInt("Magnet", level).commit()
    }

    fun setGoldLevel(level: Int) {
        editor.putInt("Gold", level).commit()
    }

    fun setSlowMotionLevel(level: Int) {
        editor.putInt("Slow", level).commit()
    }

    fun setMoreMoneyLevel(level: Int) {
        editor.putInt("More", level).commit()
    }

}
