package com.kono_click.android.click.presentation.fragmentHome

import androidx.lifecycle.ViewModel
import com.kono_click.android.click.data.repository.BaseRepository
import com.kono_click.android.click.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: BaseRepository) : ViewModel() {

    fun getUserMoney() = repository.getUserMoney()
    fun getHighScore() = repository.getHighScore()
    fun getSound() = repository.getSound()
    fun setSound(isSoundOn: Boolean) = repository.setSound(isSoundOn)
    fun getNumberOfAllGoldenTokens() = repository.getNumberOfAllGoldenTokens()
    fun getNumberOfTenSecTokens() = repository.getNumberOfTenSecTokens()
    fun getIsAllGolden() = repository.getIsAllGolden()
    fun setIsAllGolden(isAllGolden: Boolean) = repository.setIsAllGolden(isAllGolden)
    fun getIsTenSec() = repository.getIsTenSec()
    fun setIsTenSec(isTenSec: Boolean) = repository.setIsTenSec(isTenSec)
    fun getMagnetLevel() = repository.getMagnetLevel()
    fun getGoldLevel() = repository.getGoldLevel()
    fun getSlowMotionLevel() = repository.getSlowMotionLevel()
    fun getMoreMoneyLevel() = repository.getMoreMoneyLevel()
    fun saveMagnetLevelToConstants(magmetLevel1: Int) {
        if (magmetLevel1 == 7) {
            Constants.MagmetLevel = 8
        } else if (magmetLevel1 == 8) {
            Constants.MagmetLevel = 11
        } else if (magmetLevel1 == 9) {
            Constants.MagmetLevel = 15
        }
    }

    fun saveGoldLevelToConstants(goldLevel1: Int) {
        if (goldLevel1 == 7) {
            Constants.GoldLevel=8
        } else if (goldLevel1 == 8){
            Constants.GoldLevel=11
        }
        else if (goldLevel1 == 9){
            Constants.GoldLevel=15
        }
    }


}