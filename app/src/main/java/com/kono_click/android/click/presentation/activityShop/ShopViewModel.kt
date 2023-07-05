package com.kono_click.android.click.presentation.activityShop

import androidx.lifecycle.ViewModel
import com.kono_click.android.click.data.repository.BaseRepository
import com.kono_click.android.click.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(val repository: BaseRepository) : ViewModel() {
    fun getUserMoney() = repository.getUserMoney()
    fun setUserMoney(userMoney:Long){ repository.setUserMoney(userMoney)}
    fun getNumberOfAllGoldenTokens() = repository.getNumberOfAllGoldenTokens()
    fun setNumberOfAllGoldenTokens(numberOfAllGoldenTokens:Long){ repository.setNumberOfAllGoldenTokens(numberOfAllGoldenTokens)}
    fun getNumberOfTenSecTokens() = repository.getNumberOfTenSecTokens()
    fun setNumberOfTenSecTokens(numberOfTenSecTokens:Long){ repository.setNumberOfTenSecTokens(numberOfTenSecTokens)}
    fun getMagnetLevel() = repository.getMagnetLevel()
    fun setMagnetLevel(magnetLevel:Int){ repository.setMagnetLevel(magnetLevel)}
    fun getGoldLevel() = repository.getGoldLevel()
    fun setGoldLevel(goldLevel:Int){ repository.setGoldLevel(goldLevel)}
    fun getSlowMotionLevel() = repository.getSlowMotionLevel()
    fun setSlowMotionLevel(slowMotionLevel:Int){ repository.setSlowMotionLevel(slowMotionLevel)}
    fun getMoreMoneyLevel() = repository.getMoreMoneyLevel()
    fun setMoreMoneyLevel(moreMoneyLevel:Int){ repository.setMoreMoneyLevel(moreMoneyLevel)}
    fun saveMagnetLevelToConstants(magnetLevel: Int) {
        when (magnetLevel) {
            7 -> {
                Constants.MagmetLevel = 8
            }
            8 -> {
                Constants.MagmetLevel = 11
            }
            9 -> {
                Constants.MagmetLevel = 15
            }
        }
    }
    fun getCurrentUpgradeCoast(level: Int): Int {
        var priceToReturn = 0
        when (level) {
            5 -> priceToReturn = 200
            6 -> priceToReturn = 500
            7 -> priceToReturn = 1200
            8 -> priceToReturn = 3000
            9 -> priceToReturn = 0
        }
        return priceToReturn
    }
    fun cutFromShared(nextUpgradeCoast: Int) {
        var i = getUserMoney()
        i -= nextUpgradeCoast
        setUserMoney(i)
        Constants.UserMoney = i
    }
     fun checkMoney(i: Int): Boolean = i <= Constants.UserMoney

     fun getMoneyfromlevel(level: Int): Int {
        var numtoreturn = 0
        when (level) {
            5 -> numtoreturn = 200
            6 -> numtoreturn = 500
            7 -> numtoreturn = 1200
            8 -> numtoreturn = 3000
            9 -> 0
        }
        return numtoreturn
    }

    fun buyOneTimeItem(itemNumber: Int) {
        if (itemNumber == 2) {
            val allGolden = getNumberOfAllGoldenTokens() + 1
            setNumberOfAllGoldenTokens(allGolden)
            Constants.AllGolden = allGolden
        } else if (itemNumber == 1) {
            val tenSec = getNumberOfTenSecTokens() + 1
            setNumberOfTenSecTokens(tenSec)
            Constants.tenSec = tenSec
        }
    }

}