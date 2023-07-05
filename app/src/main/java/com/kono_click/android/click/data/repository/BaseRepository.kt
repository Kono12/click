package com.kono_click.android.click.data.repository

import com.kono_click.android.click.data.sharedPref.SharedPrefImpl

class BaseRepository(val sharedPreferences: SharedPrefImpl) {

    fun getUserMoney() = sharedPreferences.getUserMoney()
    fun setUserMoney(userMoney: Long) = sharedPreferences.setUserMoney(userMoney)

    fun getHighScore() = sharedPreferences.getHighScore()
    fun setHighScore(highScore: Int) = sharedPreferences.setHighScore(highScore)

    fun getSound() = sharedPreferences.getSound()
    fun setSound(isSoundOn: Boolean) = sharedPreferences.setSound(isSoundOn)

    fun getNumberOfAllGoldenTokens() = sharedPreferences.getAllGolden()
    fun setNumberOfAllGoldenTokens(numberOfTokens: Long) =
        sharedPreferences.setAllGold(numberOfTokens)

    fun getNumberOfTenSecTokens() = sharedPreferences.getTenSec()
    fun setNumberOfTenSecTokens(numberOfTokens: Long) = sharedPreferences.setTenSec(numberOfTokens)

    fun getIsAllGolden() = sharedPreferences.getIsAllGolden()
    fun setIsAllGolden(isAllGolden: Boolean) = sharedPreferences.setIsAllGolden(isAllGolden)

    fun getIsTenSec() = sharedPreferences.getIsTenSec()
    fun setIsTenSec(isTenSec: Boolean) = sharedPreferences.setIsTenSec(isTenSec)

    fun getMagnetLevel() = sharedPreferences.getMagnetLevel()
    fun setMagnetLevel(magnetLevel: Int) = sharedPreferences.setMagnetLevel(magnetLevel)

    fun getGoldLevel() = sharedPreferences.getGoldLevel()
    fun setGoldLevel(goldLevel: Int) = sharedPreferences.setGoldLevel(goldLevel)

    fun getSlowMotionLevel() = sharedPreferences.getSlowMotionLevel()
    fun setSlowMotionLevel(slowMotionLevel: Int) =
        sharedPreferences.setSlowMotionLevel(slowMotionLevel)

    fun getMoreMoneyLevel() = sharedPreferences.getMoreMoneyLevel()
    fun setMoreMoneyLevel(moreMoneyLevel: Int) = sharedPreferences.setMoreMoneyLevel(moreMoneyLevel)


}