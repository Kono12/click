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
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
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
import kotlin.text.Typography.bullet

class the_game : Fragment() {


    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"

    var cancelIt = false
    lateinit var sharedPreferencee: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentRunTestBinding
    var arr = Array(100, { i -> i + 1 })


    private lateinit var ballwall: MediaPlayer
    private lateinit var beeb: MediaPlayer
    private lateinit var bullet: MediaPlayer
    private lateinit var firec: MediaPlayer
    private lateinit var gun: MediaPlayer
    private lateinit var notification: MediaPlayer
    private lateinit var sword: MediaPlayer


    var GameEnded = false

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


    //timers
    var Timer1 = false
    var Timer2 = false
    var Timer3 = false
    var Timer4 = false

    //Control the game from here
    var BreakLoop = false
    var score = 0
    var timer = Constants.time
    var time = Constants.time
    var timeBetweenMoney: Long = 90
    var minSpeed = 1200
    var maxSpeed = 2200
    var hitBox = 100
    var defauldDelayer = 300
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var adRequest = AdRequest.Builder().build()
        // remember to put yours
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
        CreatMedeiaPlayers()
        hideTimers()
        binding.Timer.text = time.toString()
        sharedPreferencee = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferencee.edit()
        lifecycleScope.launch {
            delay((Math.random() * 500 + 200).toLong())

            withContext(Dispatchers.Main) {
                lifecycleScope.launch {
                    repeat(time) {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            if (timer == 48) {
                                phaseThree = false
                                phaseone = false
                                phaseTwo = true
                               // Toast.makeText(activity,"phase 2" ,Toast.LENGTH_SHORT).show()
                            }

                            if (timer == 25) {
                                phaseThree = true
                                phaseone = false
                                phaseTwo = false
                                Toast.makeText(activity,"phase 3" ,Toast.LENGTH_SHORT).show()

                            }
                            timer--
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

                MakeItRain(view)
            }
        }

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
                if (tim1 == 0 && tim2 == 0 && tim3 == 0 && tim4 == 0) delayer = 300
                //val num = ((0..100).random()) // generated random from 0 to 100 included
                val num = generateRandom()
                val isSpecialAbility = ((0..100).random()) % 5 == 0

                //Abilities
                var isGolden = false
                var isMagnet = false
                var isSlow = false
                var isMoreMoney = false
                var isBigHit = false

                if (timer < 2) continue
                delay(delayer)
                withContext(Dispatchers.Main) {
                    try {

                        val star: ImageView = view.findViewById(R.id.star)
                        val container = star.parent as ViewGroup
                        val containerW = container.width
                        val containerH = container.height

                        var starW: Float = star.width.toFloat()
                        var starH: Float = star.height.toFloat()


                        val newStar = AppCompatImageView(requireActivity())
                        if (isSpecialAbility) {
                            if (num <= 25) {
                                newStar.setImageResource(R.drawable.ic_golden_dollar)
                                isGolden = true
                            } else if (num == 35 || num == 45 || num == 50 || num == 65) {
                                newStar.setImageResource(R.drawable.ic_magnet)
                                isMagnet = true
                            } else if (num == 70 || num == 75 || num == 85 || num == 55 || num == 60) {
                                newStar.setImageResource(R.drawable.ic_slow)
                                isSlow = true
                            } else if (num == 40 || num == 80 || num == 85 || num == 95) {
                                newStar.setImageResource(R.drawable.ic_more_money)
                                isMoreMoney = true
                            } else if (num == 90 || num==30) {
                                newStar.setImageResource(R.drawable.ic_best)
                                isBigHit = true
                            }
                        } else {
                            newStar.setImageResource(R.drawable.ic_baseline_attach_money_24)
                        }

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
                    var numm: Float = 1f
                    mover.interpolator = AccelerateInterpolator(numm)

                    val rotator = ObjectAnimator.ofFloat(
                        newStar, View.ROTATION,
                        (Math.random() * 1080).toFloat()
                    )
                    rotator.interpolator = LinearInterpolator()

                    val set = AnimatorSet()
                    set.playTogether(mover, rotator)

                    var speed: Long
                    if (phaseone) {

                        delayer = 250
                        speed = (Math.random() * 3400 + 3900).toLong()

                        if (isBigHit || isMoreMoney || isSlow) {
                            if (isSlow) {
                                speed = (Math.random() * 5300 + 5700).toLong()
                            }
                            if (isMoreMoney) {
                                delayer = 200
                            }
                            if (isBigHit) {
                                delayer = 900
                                speed = (Math.random() * 2600 + 3100).toLong()
                            }
                        }
                        set.duration = speed
                    } else if (phaseTwo) {

                        delayer = 150
                        speed = (Math.random() * 2600 + 2900).toLong()

                        if (isBigHit || isMoreMoney || isSlow) {
                            if (isSlow) {
                                speed = (Math.random() * 3900 + 3999).toLong()
                            }
                            if (isMoreMoney) {
                                delayer = 120
                            }
                            if (isBigHit) {
                                delayer = 60
                                speed = (Math.random() * 1800 + 2700).toLong()
                            }
                        }
                        set.duration = speed
                    } else if (phaseThree) {
                        delayer = 80
                        speed = (Math.random() * 1700 + 2200).toLong()

                        if (isBigHit || isMoreMoney || isSlow) {
                            if (isSlow) {
                                speed = (Math.random() * 3800 + 4500).toLong()
                            }
                            if (isMoreMoney) {
                                delayer = 50

                            }
                            if (isBigHit) {
                                delayer = 20
                                speed = (Math.random() * 1700 + 2500).toLong()
                            }
                        }
                        set.duration = speed
                    }


                    set.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            if (magn && newStar.visibility!=View.GONE && timer>0) {
                                if (isGolden) {
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
                                    score += GoldenLevel
                                    isGolden = false
                                    Constants.GoldenAmount++
                                    Constants.GoldenMoney += GoldenLevel
                                        Constants.MagnetMoney += GoldenLevel
                                    } else
                                        Constants.normalMoey++
                                    Constants.MagnetMoney++
                                    score++
                                 try {
                                     if(Constants.sound){
                                         lifecycleScope.launch {
                                             try {
                                                 if (sword.isPlaying())
                                                     sword.seekTo(0)
//                                                    sword.release()
//                                                    sword=MediaPlayer.create(activity,R.raw.sword)
                                                 sword.start()
                                             } catch (e: java.lang.Exception) {
                                                 e.printStackTrace()
                                             }
                                         }
                                     }
                                     binding.txt.setTextColor(resources.getColor(R.color.Green))
                                     binding.txt.text = score.toString()
                                 }catch (E:Exception){}
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


                                            score += GoldenLevel
                                            Constants.GoldenMoney += GoldenLevel
                                            isGolden = false
                                            Constants.GoldenAmount++

                                        } else if (isSlow) {

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

                                                Constants.SlowAmount++
                                                if (tim1 > 0) tim1 = slowLevel
                                                else {
                                                    GlobalScope.launch() {
                                                        Timer1 = true
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.slow.visibility =
                                                                    View.VISIBLE
                                                                binding.slowTimer.visibility =
                                                                    View.VISIBLE
                                                            } catch (E: Exception) {
                                                            }
                                                        }
                                                        tim1 = slowLevel
                                                        while (tim1 > 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.slowTimer.text =
                                                                    tim1.toString()
                                                            }
                                                            delay(1000)
                                                            tim1--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.slowTimer.visibility =
                                                                    View.GONE
                                                                binding.slow.visibility = View.GONE
                                                            } catch (E: Exception) {
                                                            }

                                                        }
                                                        isSlow = false
                                                        Timer1 = false
                                                    }
                                                }
                                            } else if (isBigHit) {

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


                                                Constants.BigHitAmount++
                                                if (tim2 > 0) {
                                                    tim2 = bigHitLevel
                                                } else {
                                                    Timer2 = true
                                                    GlobalScope.launch() {
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.bigHit.visibility =
                                                                    View.VISIBLE
                                                                binding.bigHitTimer.visibility =
                                                                    View.VISIBLE
                                                            } catch (E: Exception) {
                                                            }
                                                        }
                                                        tim2 = bigHitLevel
                                                        while (tim2 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.bigHitTimer.text =
                                                                    tim2.toString()
                                                            }
                                                            delay(1000)
                                                            tim2--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.bigHit.visibility =
                                                                    View.GONE
                                                                binding.bigHitTimer.visibility =
                                                                    View.GONE
                                                            } catch (E: Exception) {
                                                            }

                                                        }
                                                        isBigHit = false
                                                        Timer2 = false
                                                    }
                                                }
                                            } else if (isMoreMoney) {

                                                if(sound){
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

                                                Constants.MoreMoneyAmount++
                                                if (tim3 > 0) tim3 = moreMoneyLevel
                                                else {
                                                    Timer3 = true

                                                    GlobalScope.launch() {
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.moreMoney.visibility =
                                                                    View.VISIBLE
                                                                binding.moreMoneyTimer.visibility =
                                                                    View.VISIBLE
                                                            } catch (E: Exception) {
                                                            }
                                                        }
                                                        tim3 = moreMoneyLevel
                                                        while (tim3 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.moreMoneyTimer.text =
                                                                    tim3.toString()
                                                            }
                                                            delay(1000)
                                                            tim3--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.moreMoney.visibility =
                                                                    View.GONE
                                                                binding.moreMoneyTimer.visibility =
                                                                    View.GONE
                                                            } catch (E: Exception) {
                                                            }
                                                        }
                                                        Timer3 = false
                                                    }
                                                }
                                            } else if (isMagnet) {

                                                GlobalScope.launch {
                                                    try {
                                                        if(sound) {
                                                            if (ballwall.isPlaying())
                                                                ballwall.seekTo(0)
                                                            ballwall.start()
                                                        }
                                                    } catch (e: java.lang.Exception) {
                                                        e.printStackTrace()
                                                    }
                                                }


                                                Constants.MagnetAmount++
                                                if (tim4 > 0) tim4 = MagnetLevel
                                                else {
                                                    Timer4 = true
                                                    GlobalScope.launch() {

                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.magnet.visibility =
                                                                    View.VISIBLE
                                                                binding.magnetTimer.visibility =
                                                                    View.VISIBLE
                                                            } catch (E: Exception) {
                                                            }
                                                        }
                                                        magn = true
                                                        tim4 = MagnetLevel
                                                        while (tim4 != 0) {
                                                            withContext(Dispatchers.Main) {
                                                                binding.magnetTimer.text =
                                                                    tim4.toString()
                                                            }
                                                            delay(1000)
                                                            tim4--
                                                        }
                                                        withContext(Dispatchers.Main) {
                                                            try {
                                                                binding.magnet.visibility =
                                                                    View.GONE
                                                                binding.magnetTimer.visibility =
                                                                    View.GONE
                                                            } catch (E: Exception) {
                                                            }
                                                            isMagnet = false
                                                        }
                                                        magn = false
                                                            Timer4 = false
                                                    }
                                                }
                                            }
                                        } else {

                                            if(Constants.sound){
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
                                            Constants.normalMoey++
                                            score++
                                        }

                                        binding.txt.setTextColor(resources.getColor(R.color.Green))
                                        binding.txt.text = score.toString()
                                        newStar.visibility = View.GONE
                                    }

                                } catch (E: Exception) {
                                }
                            }
                        }
                      }
                    catch (e: Exception) {
                      }
                }


                if (BreakLoop) {
                    break

                }
                delay(timeBetweenMoney)


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


    // Implementing Fisher–Yates shuffle
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
}