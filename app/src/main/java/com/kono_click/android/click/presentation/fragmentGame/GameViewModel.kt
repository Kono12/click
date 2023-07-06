package com.kono_click.android.click.presentation.fragmentGame

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kono_click.android.click.R
import com.kono_click.android.click.data.repository.BaseRepository
import com.kono_click.android.click.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(val repository: BaseRepository) : ViewModel() {


     lateinit var ballwall: MediaPlayer
     lateinit var beeb: MediaPlayer
     lateinit var bullet: MediaPlayer
     lateinit var firec: MediaPlayer
     lateinit var gun: MediaPlayer
     lateinit var notification: MediaPlayer
     lateinit var sword: MediaPlayer

     var containerW: Int = 0
     var containerH: Int = 0

     var dollarW: Float = 0f
     var dollarH: Float = 0f

     var gameEnded = false

    //Control the game from here
     var breakLoopToStop = false
     var score: Int = 0
     var timer: Int = Constants.time
     var time = Constants.time

    //    var defminSpeed = 1200
//    var defmaxSpeed = 2200
     var minSpeed = 3000
     var maxSpeed = 3500
     var hitBox = 105
     var delayer: Long = 280

    //Abilities
    // var isGoldenG = false
     var isSlowG = false
     var isMoreMoneyG = false
     var isBigHitG = false


    //Variables used for Timers
    //slow
     var tim1 = 0

    //BigHit
     var tim2 = 0

    //More Money
     var tim3 = 0

    //magnet
     var tim4 = 0


     var magn = false

    //Ability level
     var goldenLevel: Int = Constants.GoldLevel
     var magnetLevel = Constants.MagmetLevel
     var slowLevel = Constants.SlowMotionLevel
     var moreMoneyLevel = Constants.MoreMoneyLevel
     var bigHitLevel = Constants.BigHitLevel

     var isStop: Boolean = false
     var cancelIt = false
     var arr = Array(100) { i -> i + 1 }

    // one time use item
     var allGolden = false
     var tenSec = false


    //time phases
    //60 -> 30
    var phaseone = true

    //30 -> 20
    var phaseTwo = false

    //20 -> 0
    var phaseThree = false

    fun getUserMoney() = repository.getUserMoney()
    fun setUserMoney(userMoney: Long) {
        repository.setUserMoney(userMoney)
    }

    fun getNumberOfAllGoldenTokens() = repository.getNumberOfAllGoldenTokens()
    fun setNumberOfAllGoldenTokens(numberOfAllGoldenTokens: Long) {
        repository.setNumberOfAllGoldenTokens(numberOfAllGoldenTokens)
    }

    fun getNumberOfTenSecTokens() = repository.getNumberOfTenSecTokens()
    fun setNumberOfTenSecTokens(numberOfTenSecTokens: Long) {
        repository.setNumberOfTenSecTokens(numberOfTenSecTokens)
    }

    fun setIsAllGolden(isAllGolden: Boolean) {
        repository.setIsAllGolden(isAllGolden)
    }

    fun setIsTenSec(isTenSec: Boolean) {
        repository.setIsTenSec(isTenSec)
    }

    fun setHighScore(highScore: Int) {
        repository.setHighScore(highScore)
    }

    fun getHighScore() = repository.getHighScore()

    fun gameEndCalculator(score: Int) {
        var i = getHighScore()
        Constants.HighScore = i
        Constants.scoree = score
        if (i < score) {
            setHighScore(score)
        }
        val money = getUserMoney()
        if (money + score <= 999999)
            setUserMoney(money + score)
        else
            setUserMoney(999999)
    }

    fun returnControlVariables(
    ): ControlObject {

        //todo : remember to fix speed
        var delayer = 300
        var minSpeed = 3300
        var maxSpeed = 3800

        if (phaseone) {
            delayer = 300
            minSpeed = 3700
            maxSpeed = 4100

            if (isBigHitG || isMoreMoneyG || isSlowG) {
                if (isSlowG) {

                    minSpeed = 5000
                    maxSpeed = 5500
                }
                if (isMoreMoneyG) {
                    delayer = 200
                }
                if (isBigHitG) {
                    delayer = 100
                    if (!isSlowG) {
                        // speed = (Math.random() * 2600 + 3100).toLong()
                        minSpeed = 2900
                        maxSpeed = 3400
                    }
                }
            }
            return ControlObject(minSpeed, maxSpeed, delayer)
        } else if (phaseTwo) {

            delayer = 220
            minSpeed = 2500
            maxSpeed = 3000
            // speed = (Math.random() * 2600 + 2900).toLong()

            if (isBigHitG || isMoreMoneyG || isSlowG) {
                if (isSlowG) {
                    minSpeed = 3500
                    maxSpeed = 4000
                }
                if (isMoreMoneyG) {
                    delayer = 170
                }
                if (isBigHitG) {
                    delayer = 100
                    if (!isSlowG) {
                        minSpeed = 1800
                        maxSpeed = 2700
                    }
                }
            }
            return ControlObject(minSpeed, maxSpeed, delayer)
        } else if (phaseThree) {
            delayer = 180
            minSpeed = 1600
            maxSpeed = 2200
            if (isBigHitG || isMoreMoneyG || isSlowG) {
                if (isSlowG) {
                    minSpeed = 2500
                    maxSpeed = 3000
                }
                if (isMoreMoneyG) {
                    delayer = 140

                }
                if (isBigHitG) {
                    delayer = 100
                    if (!isSlowG) {
                        minSpeed = 2200
                        maxSpeed = 2600
                    }
                }
            }
            return ControlObject(minSpeed, maxSpeed, delayer)
        } else {
            return ControlObject(minSpeed, maxSpeed, delayer)
        }
    }

    private fun shuffleArray(ar: Array<Int>) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        try {
            val rnd: Random = ThreadLocalRandom.current()
            for (i in ar.size - 1 downTo 1) {
                val index: Int = rnd.nextInt(i + 1)
                // Simple swap
                val a = ar[index]
                ar[index] = ar[i]
                ar[i] = a
            }
        } catch (E: java.lang.Exception) {

        }
    }

    private fun generateRandom(arr: Array<Int>): Int {
        shuffleArray(arr)
        try {
            return arr[((Math.random() * 99 + 0).toInt())]
        } catch (E: java.lang.Exception) {
            return arr[64]
        }
    }

    suspend fun delayForPhase() {
        if (!phaseThree)
            delay(80)
    }

    fun getRandomNumber(): Int =
        if (allGolden) 25 else generateRandom(arr)

    fun checkIfIsSpecialAbility(): Boolean =
        if (allGolden) true else (0..100).random() % 5 == 0

    fun getDollarImage(abilityType: AbilityType): Int =
         when (abilityType) {
            AbilityType.IS_MAGNET -> {
               R.drawable.ic_magnet
            }

            AbilityType.NONE -> {
                R.drawable.ic_baseline_attach_money_24
            }

            AbilityType.IS_GOLDEN -> {
                R.drawable.ic_golden_dollar
            }

            AbilityType.IS_SLOW -> {
                R.drawable.ic_slow
            }

            AbilityType.IS_MORE_MONEY -> {
                R.drawable.ic_more_money
            }

            AbilityType.IS_BIG_HIT -> {R.drawable.ic_best
            }
        }

    fun setPhaseOne() {
        phaseThree = false
        phaseone = false
        phaseTwo = true
    }

    fun setPhaseTwo() {
        phaseThree = true
        phaseone = false
        phaseTwo = false
    }

    fun decideTheAbility(
        isSpecialAbility: Boolean,
        num: Int,
    ): AbilityType {
        if (isSpecialAbility || allGolden) {
            if (num <= 25) { //5%
                return AbilityType.IS_GOLDEN
            } //4%
            else if (num == 35 || num == 45 || num == 50 || num == 65) {
                return AbilityType.IS_MAGNET
            } //5%
            else if ((num == 70 || num == 75 || num == 85 || num == 55 || num == 60) && phaseThree) {
                return AbilityType.IS_SLOW
            }// 4%
            else if (num == 40 || num == 80 || num == 85 || num == 95) {
                return AbilityType.IS_MORE_MONEY
            } //2%
            else if (num == 90 || num == 30) {
                return AbilityType.IS_BIG_HIT
            }
        } else {//85%
            return AbilityType.NONE
        }
        return AbilityType.NONE
    }

    fun changePhase() {
        val changeOne = if (tenSec) 58 else 48
        val changeTwo = if (tenSec) 40 else 30
        if (timer == changeOne) {
            setPhaseOne()
        }
        if (timer == changeTwo) {
            setPhaseTwo()
        }
    }

    fun endTheGame() {
        breakLoopToStop = true
        if (!gameEnded) {
            gameEndCalculator(score)
            gameEnded = true
        }
    }

    fun checkOneTimeUseItems() {
        if (Constants.AllGolden > 0 && Constants.isAllGolden) {
            allGolden = true
            val allG = getNumberOfAllGoldenTokens() - 1
            setNumberOfAllGoldenTokens(allG)
            setIsAllGolden(false)
            Constants.AllGolden = allG
        }
        if (Constants.tenSec > 0 && Constants.isTenSec) {
            tenSec = true
            val tenS = getNumberOfTenSecTokens() - 1
            setNumberOfTenSecTokens(tenS)
            setIsTenSec(false)
            Constants.tenSec = tenS
            timer += 10
            Constants.time += 10
        }
    }

    fun collectDollarsOnly(abilityType: AbilityType) {
        if (abilityType == AbilityType.IS_GOLDEN) {
            collectGold()
            Constants.MagnetMoney += goldenLevel
        } else {
            collectDollars()
            Constants.MagnetMoney++
        }
    }

     fun collectDollars() {
        playSound(sword)
        Constants.normalMoey++
        score++
    }

    fun collectGold() {
        playSound(bullet)
        score += goldenLevel
        Constants.GoldenAmount++
        Constants.GoldenMoney += goldenLevel
    }

    fun playSound(mediaPlayer: MediaPlayer) {
        if (Constants.sound) {
            viewModelScope.launch {
                try {
                    if (mediaPlayer.isPlaying)
                        mediaPlayer.seekTo(0)
                    mediaPlayer.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun slowMotionEffect() {
        isSlowG = true
        playSound(gun)
        Constants.SlowAmount++
    }

    fun isBigHitEffect() {
        isBigHitG = true
        playSound(notification)
        Constants.BigHitAmount++
    }

    fun moreMoneyEffect() {
        isMoreMoneyG = true
        playSound(beeb)
        Constants.MoreMoneyAmount++
    }

    fun magnetEffect() {
        playSound(ballwall)
        Constants.MagnetAmount++
    }

}