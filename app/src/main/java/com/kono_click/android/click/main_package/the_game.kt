package com.kono_click.android.click.main_package

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kono_click.android.click.Constants
import com.kono_click.android.click.Constants.sound
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.FragmentRunTestBinding
import com.kono_click.android.click.info
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class the_game : Fragment() {


    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"

    var isStop = false
    var cancelIt = false
    lateinit var sharedPreferencee: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentRunTestBinding
    var arr = Array(100, { i -> i + 1 })

    // one time use item
    var allGolden = false
    var tenSec = false

    private lateinit var ballwall: MediaPlayer
    private lateinit var beeb: MediaPlayer
    private lateinit var bullet: MediaPlayer
    private lateinit var firec: MediaPlayer
    private lateinit var gun: MediaPlayer
    private lateinit var notification: MediaPlayer
    private lateinit var sword: MediaPlayer


    var GameEnded = false


    //Abilities
    var isGoldenG = false
    var isSlowG = false
    var isMoreMoneyG = false
    var isBigHitG = false

    //Ability level
    var GoldenLevel = Constants.GoldLevel
    var MagnetLevel = Constants.MagmetLevel
    var slowLevel = Constants.SlowMotionLevel
    var moreMoneyLevel = Constants.MoreMoneyLevel
    var bigHitLevel = Constants.BigHitLevel

    //time phases
    //60 -> 30
    var phaseone = true

    //30 -> 20
    var phaseTwo = false

    //20 -> 0
    var phaseThree = false


    //Control the game from here
    var BreakLoop = false
    var score = 0
    var timer = Constants.time
    var time = Constants.time

    //    var defminSpeed = 1200
//    var defmaxSpeed = 2200
    var minSpeed = 3000
    var maxSpeed = 3500
    var hitBox = 105
    var delayer: Long = 280

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

    var methods = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRunTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adRequest = AdRequest.Builder().build()
        // ads
        InterstitialAd.load(requireActivity(), "ca-app-pub-4031659564383807/4979093119", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString().toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                }
            })

        sharedPreferencee = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferencee.edit()


        showScore()
        CreatMedeiaPlayers()
        hideTimers()
        checkOneTimeUseItems()
        binding.Timer.text = time.toString()


        lifecycleScope.launch {
            //small delay before start
            delay((Math.random() * 250 + 150).toLong())

            withContext(Dispatchers.Main) {

                lifecycleScope.launch {
                    gameTimer()
                }

                MakeItRain(view)
            }
        }

    }

    private fun checkOneTimeUseItems() {
        if (Constants.AllGolden > 0 && Constants.isAllGolden) {
            allGolden = true
            var allG = sharedPreferencee.getLong("AllGolden", 0) - 1
            editor.putLong("AllGolden", allG)
                .putBoolean("UseGolden", false)
                .commit()
            Constants.AllGolden = allG.toInt()
        }
        if (Constants.tenSec > 0 && Constants.isTenSec) {
            tenSec = true
            var tenS = sharedPreferencee.getLong("TenSec", 0) - 1
            editor.putLong("TenSec", tenS)
                .putBoolean("UseTenSec", false)
                .commit()
            Constants.tenSec = tenS.toInt()
            timer += 10
            time += 10
        }
    }

    private fun showScore() {
        val bounce = AnimationUtils.loadAnimation(requireActivity(),R.anim.bounce)
        binding.txt.visibility=View.VISIBLE
        binding.txt.startAnimation(bounce)
    }

    private fun hideTimers() {

        binding.bigHitTimer.visibility = View.GONE
        binding.bigHit.visibility = View.GONE

        binding.magnet.visibility = View.GONE
        binding.magnetTimer.visibility = View.GONE

        binding.slow.visibility = View.GONE
        binding.slowTimer.visibility = View.GONE

        binding.moreMoney.visibility = View.GONE
        binding.moreMoneyTimer.visibility = View.GONE

    }

    fun MakeItRain(view: View) {


        GlobalScope.launch {


            while (true) {
                //if (isStop) continue
                if(!phaseThree)
                delay(80)
                else if (phaseThree){
                }
                //val num = ((0..100).random()) // generated random from 0 to 100 included
                // is used to decide if it's special or not
                var num = if ( allGolden ) 25 else generateRandom()
                val isSpecialAbility = if(allGolden)true else (0..100).random() % 5 == 0

                if (timer < 0) continue
                //delay(delayer)

                withContext(Dispatchers.Main) {
                    try {

                        val star: ImageView = view.findViewById(R.id.star)
                        val container = star.parent as ViewGroup
                        val containerW = container.width
                        val containerH = container.height

                        var starW: Float = star.width.toFloat()
                        var starH: Float = star.height.toFloat()


                        val newStar = AppCompatImageView(requireActivity())

                        //Abilities
                        var isGolden = false
                        var isMagnet = false
                        var isSlow = false
                        var isMoreMoney = false
                        var isBigHit = false


                        //using the random number generated before to decide what ability it is
                        if (isSpecialAbility || allGolden) {
                            if (num <= 25 ) { //5%
                                newStar.setImageResource(R.drawable.ic_golden_dollar)
                                isGolden = true
                            } //4%
                            else if (num == 35 || num == 45 || num == 50 || num == 65) {
                                newStar.setImageResource(R.drawable.ic_magnet)
                                isMagnet = true
                            } //5%
                            else if ((num == 70 || num == 75 || num == 85 || num == 55 || num == 60)&& phaseThree) {
                                newStar.setImageResource(R.drawable.ic_slow)
                                isSlow = true
                            }// 4%
                            else if (num == 40 || num == 80 || num == 85 || num == 95) {
                                newStar.setImageResource(R.drawable.ic_more_money)
                                isMoreMoney = true
                            } //2%
                            else if (num == 90 || num == 30) {
                                newStar.setImageResource(R.drawable.ic_best)
                                isBigHit = true
                            }
                        } else {//85%
                            newStar.setImageResource(R.drawable.ic_baseline_attach_money_24)
                        }


                        val controlObject: ControlObject =
                            returnControlVariables(isBigHitG, isSlowG, isMoreMoneyG)
                        minSpeed = controlObject.minSpeed
                        maxSpeed = controlObject.maxSpeed
                        delayer = controlObject.delayer.toLong()


                        newStar.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        container.addView(newStar)
                        newStar.setPadding(hitBox, hitBox, hitBox, hitBox)

                        newStar.translationX = Math.random().toFloat() *
                                containerW - starW / 2

                        val mover = ObjectAnimator.ofFloat(
                            newStar, View.TRANSLATION_Y,
                            -starH, containerH + starH
                        )

                        mover.interpolator = AccelerateInterpolator(1f)

                        val rotator = ObjectAnimator.ofFloat(
                            newStar, View.ROTATION,
                            (Math.random() * 1080).toFloat()
                        )
                        rotator.interpolator = LinearInterpolator()

                        val set = AnimatorSet()
                        set.playTogether(mover, rotator)

                        var speed: Long
                        speed = (Math.random() * minSpeed + maxSpeed).toLong()
                        set.duration = speed

                        set.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                if (magn && newStar.visibility != View.GONE && timer > 0 && !isStop) {
                                    if (isGolden) {
                                        playBulletSound()
                                        score += GoldenLevel
                                        Constants.GoldenAmount++
                                        Constants.GoldenMoney += GoldenLevel
                                        Constants.MagnetMoney += GoldenLevel
                                        isGolden = false

                                    } else {
                                        Constants.normalMoey++
                                        Constants.MagnetMoney++
                                        score++
                                    }
                                    try {
                                        platSwordSound()
                                        binding.txt.setTextColor(resources.getColor(R.color.Green))
                                        binding.txt.text = score.toString()
                                    } catch (E: Exception) {
                                    }
                                }
                                container.removeView(newStar)
                            }
                        })

                        set.start()

                        newStar.setOnClickListener {
                            lifecycleScope.launch {
                                try {
                                    withContext(Dispatchers.Main) {
                                        if (isSpecialAbility) {
                                            if (isGolden) {

                                                playBulletSound()

                                                score += GoldenLevel
                                                Constants.GoldenMoney += GoldenLevel
                                                Constants.GoldenAmount++
                                                isGolden = false
                                            } else if (isSlow) {

                                                isSlowG = true
                                                playGunSound()
                                                Constants.SlowAmount++

                                                //if the timer is on start it again
                                                if (tim1 > 0) {tim1 = slowLevel
                                                    withContext(Dispatchers.Main) {
                                                        binding.slowTimer.text =
                                                            tim1.toString()
                                                    }
                                                }
                                                //start timer
                                                else {
                                                    GlobalScope.launch() {
                                                        withContext(Dispatchers.Main) {
                                                            showSlowMotionTimer()
                                                        }
                                                        //setting the timer's value by level
                                                        tim1 = slowLevel

                                                        // slow motion timer
                                                        while (tim1 > 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.slowTimer.text =
                                                                    tim1.toString()
                                                            }
                                                            delay(1000)
                                                            if(!isStop) tim1--
                                                        }

                                                        withContext(Dispatchers.Main) {
                                                            hideSlowMotionTimer()
                                                        }
                                                        // stopping the slow motion effect
                                                        isSlowG = false
                                                    }
                                                }
                                            } else if (isBigHit) {

                                                isBigHitG = true
                                                playNotificationSound()
                                                Constants.BigHitAmount++

                                                if (tim2 > 0){
                                                    tim2 = bigHitLevel
                                                    withContext(Dispatchers.Main) {
                                                        binding.bigHitTimer.text =
                                                            tim2.toString()
                                                    }
                                                }
                                                else {
                                                    GlobalScope.launch() {
                                                        withContext(Dispatchers.Main) {
                                                            showIsBigTimer()
                                                        }

                                                        tim2 = bigHitLevel

                                                        while (tim2 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.bigHitTimer.text =
                                                                    tim2.toString()
                                                            }
                                                            delay(1000)
                                                            if(!isStop) tim2--
                                                        }

                                                        withContext(Dispatchers.Main) {
                                                            hideIsBigTimer()
                                                        }

                                                        isBigHitG = false
                                                    }
                                                }
                                            } else if (isMoreMoney) {

                                                isMoreMoneyG = true
                                                startBeebSound()
                                                Constants.MoreMoneyAmount++

                                                if (tim3 > 0) {
                                                    tim3 = moreMoneyLevel
                                                    withContext(Dispatchers.Main) {
                                                        binding.moreMoneyTimer.text =
                                                            tim3.toString()
                                                    }
                                                }
                                                else {

                                                    GlobalScope.launch() {
                                                        withContext(Dispatchers.Main) {
                                                            showMoreMoneyTimer()
                                                        }
                                                        tim3 = moreMoneyLevel

                                                        while (tim3 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.moreMoneyTimer.text =
                                                                    tim3.toString()
                                                            }
                                                            delay(1000)
                                                            if(!isStop) tim3--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            hideMoreMoneyTimer()
                                                        }
                                                        isMoreMoneyG = false
                                                    }
                                                }
                                            } else if (isMagnet) {

                                                startBallWallSound()
                                                Constants.MagnetAmount++

                                                if (tim4 > 0) {
                                                    tim4 = MagnetLevel
                                                    withContext(Dispatchers.Main) {
                                                        binding.magnetTimer.text =
                                                            tim4.toString()
                                                    }
                                                }
                                                else {
                                                    GlobalScope.launch() {

                                                        withContext(Dispatchers.Main) {
                                                            showMagnetTimer()
                                                        }
                                                        magn = true
                                                        tim4 = MagnetLevel
                                                        while (tim4 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.magnetTimer.text =
                                                                    tim4.toString()
                                                            }
                                                            delay(1000)
                                                            if(!isStop) tim4--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            hideMagnetTimer()
                                                        }
                                                        magn = false
                                                    }
                                                }
                                            }
                                        } else {
                                            //normal money
                                            platSwordSound()
                                            Constants.normalMoey++
                                            score++
                                        }

                                        binding.txt.setTextColor(ContextCompat.getColor(requireActivity(), R.color.Green))
                                        binding.txt.text = score.toString()
                                        newStar.visibility = View.GONE
                                    }

                                } catch (E: Exception) {
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                if (BreakLoop) {
                    break
                }
                delay(delayer)
            }
        }
    }

    private fun hideMagnetTimer() {
        try {
            binding.magnet.visibility =
                View.GONE
            binding.magnetTimer.visibility =
                View.GONE
        } catch (E: Exception) {
        }
    }

    private fun showMagnetTimer() {
        try {
            binding.magnet.visibility =
                View.VISIBLE
            binding.magnetTimer.visibility =
                View.VISIBLE
        } catch (E: Exception) {
        }
    }

    private fun startBallWallSound() {
        GlobalScope.launch {
            try {
                if (sound) {
                    if (ballwall.isPlaying())
                        ballwall.seekTo(0)
                    ballwall.start()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun hideMoreMoneyTimer() {
        try {
            binding.moreMoney.visibility =
                View.GONE
            binding.moreMoneyTimer.visibility =
                View.GONE
        } catch (E: Exception) {
        }
    }

    private fun showMoreMoneyTimer() {
        try {
            binding.moreMoney.visibility =
                View.VISIBLE
            binding.moreMoneyTimer.visibility =
                View.VISIBLE
        } catch (E: Exception) {
        }
    }

    private fun hideIsBigTimer() {
        try {
            binding.bigHit.visibility =
                View.GONE
            binding.bigHitTimer.visibility =
                View.GONE
        } catch (E: Exception) {
        }
    }

    private fun showIsBigTimer() {
        try {
            binding.bigHit.visibility =
                View.VISIBLE
            binding.bigHitTimer.visibility =
                View.VISIBLE
        } catch (E: Exception) {
        }
    }

    private fun hideSlowMotionTimer() {
        try {
            binding.slowTimer.visibility =
                View.GONE
            binding.slow.visibility = View.GONE
        } catch (E: Exception) {
        }
    }

    private fun showSlowMotionTimer() {
        try {
            //   making timer visible
            binding.slow.visibility =
                View.VISIBLE
            binding.slowTimer.visibility =
                View.VISIBLE
        } catch (E: Exception) {
        }
    }

    private fun playNotificationSound() {
        if (sound) {
            lifecycleScope.launch {
                try {
                    if (notification.isPlaying())
                        notification.seekTo(0)
                    notification.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun playGunSound() {
        if (sound) {
            lifecycleScope.launch {
                try {
                    if (gun.isPlaying())
                        gun.seekTo(0)
                    gun.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun startBeebSound() {
        if (sound) {
            lifecycleScope.launch {
                try {
                    if (beeb.isPlaying())
                        beeb.seekTo(0)
                    beeb.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun platSwordSound() {
        if (sound) {
            lifecycleScope.launch {
                try {
                    if (sword.isPlaying())
                        sword.seekTo(0)
                    sword.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun playBulletSound() {
        if (sound) {
            lifecycleScope.launch {
                try {
                    if (bullet.isPlaying())
                        bullet.seekTo(0)
                    bullet.start()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun generateRandom(): Int {
        shuffleArray(arr)
        try {
            return arr[((Math.random() * 99 + 0).toInt())]
        } catch (E: java.lang.Exception) {
            return arr[64]
        }
    }

    private fun GameEnd() {
        var i = sharedPreferencee.getInt("high", 0)
        Constants.HighScore = i
        Constants.scoree = score
        if (i < score) {
            editor.putInt("high", score).commit()
        }

        var money = sharedPreferencee.getLong("UserMoney", 0)
        editor.putLong("UserMoney", money + score).commit()
//        //ads
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
        } catch (e: Exception) {
        }
    }

    override fun onDestroy() {
        if (!GameEnded) GameEnd()

        super.onDestroy()
    }

    fun shuffleArray(ar: Array<Int>) {
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

    private fun CreatMedeiaPlayers() {
        ballwall = MediaPlayer.create(activity, R.raw.ballwall)
        beeb = MediaPlayer.create(activity, R.raw.beeb)
        bullet = MediaPlayer.create(activity, R.raw.bullet)
        firec = MediaPlayer.create(activity, R.raw.firec)
        gun = MediaPlayer.create(activity, R.raw.gun)
        notification = MediaPlayer.create(activity, R.raw.notification)
        sword = MediaPlayer.create(activity, R.raw.sword)
    }

    private suspend fun returnControlVariables(isBigHit: Boolean, isSlow: Boolean, isMoreMoney: Boolean): ControlObject {

        // will be changed any way
        var delayer = 300
        var minSpeed = 3300
        var maxSpeed = 3800

        if (phaseone) {
            delayer = 300
            minSpeed = 3700
            maxSpeed = 4100

            if (isBigHit || isMoreMoney || isSlow) {
                if (isSlow) {

                    minSpeed = 5000
                    maxSpeed = 5500
                }
                if (isMoreMoney) {
                    delayer = 200
                }
                if (isBigHit) {
                    delayer = 100
                    if (!isSlow) {
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

            if (isBigHit || isMoreMoney || isSlow) {
                if (isSlow) {
                    minSpeed = 3500
                    maxSpeed = 4000
                }
                if (isMoreMoney) {
                    delayer = 170
                }
                if (isBigHit) {
                    delayer = 100
                    if (!isSlow) {
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
            if (isBigHit || isMoreMoney || isSlow) {
                if (isSlow) {
                    minSpeed = 2500
                    maxSpeed = 3000
                }
                if (isMoreMoney) {
                    delayer = 140

                }
                if (isBigHit) {
                    delayer = 100
                    if (!isSlow) {
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

    private suspend fun gameTimer() {
        while (time>0){
            delay(1000)
            withContext(Dispatchers.Main) {
                var changeOne = if (tenSec) 58 else 48
                var changeTwo = if (tenSec) 40 else 30
                if (timer == changeOne) {
                    phaseThree = false
                    phaseone = false
                    phaseTwo = true
                  //  Toast.makeText(activity, "phase 2", Toast.LENGTH_SHORT).show()
                }

                if (timer == changeTwo) {
                    phaseThree = true
                    phaseone = false
                    phaseTwo = false
                //    Toast.makeText(activity, "phase 3", Toast.LENGTH_SHORT).show()

                }
                if (!isStop){
                    timer--
                }
                binding.Timer.text = timer.toString()
                if (timer == 0) {
                    BreakLoop = true
                    if (!GameEnded) {
                        GameEnd()
                        GameEnded = true
                    }
                    startActivity(Intent(activity, info::class.java))
                    findNavController().popBackStack()
                    cancelIt = true
                    this.cancel()
                }
            }

        }
    }

    override fun onStop() {
        isStop=true
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        isStop=false
    }

}


data class ControlObject(var minSpeed: Int, var maxSpeed: Int, var delayer: Int) {
}