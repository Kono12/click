package com.kono_click.android.click.main_package

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kono_click.android.click.Constants
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.FragmentRunTestBinding
import com.kono_click.android.click.info
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.max

class the_game : Fragment() {


    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"

    var cancelIt = false
    lateinit var sharedPreferencee: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentRunTestBinding
    var arr = Array(100,{i-> i + 1})

    //Ability level
    var GoldenLevel = 5
    var MagnetLevel = 5
    var slowLevel = 5
    var moreMoneyLevel = 5
    var bigHitLevel = 5


    //Control the game from here
    var BreakLoop = false
    var score = 0
    var timer = Constants.time
    var time = Constants.time
    var timeBetweenMoney: Long = 90
    var minSpeed = 1200
    var maxSpeed = 2200
    var hitBox = 110
    var delayer: Long = 300


    var magn = false

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
        InterstitialAd.load(requireActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest,
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

        hideTimers()
        binding.Timer.text = time.toString()
        sharedPreferencee = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferencee.edit()
        lifecycleScope.launch {
            delay((Math.random() * 800 + 200).toLong())

            withContext(Dispatchers.Main) {
                lifecycleScope.launch {
                    repeat(time) {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            timer--
                            binding.Timer.text = timer.toString()
                            if (timer == 0) {
                                BreakLoop = true
                                GameEnd()
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

        binding.bigHitTimer.visibility=View.GONE
        binding.bigHit.visibility=View.GONE

        binding.magnet.visibility=View.GONE
        binding.magnetTimer.visibility=View.GONE

        binding.slow.visibility=View.GONE
        binding.slowTimer.visibility=View.GONE

        binding.moreMoney.visibility=View.GONE
        binding.moreMoneyTimer.visibility=View.GONE

    }

    fun MakeItRain(view: View) {
        GlobalScope.launch {
            while (true) {
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
                            } else if (num==35 ||num == 45 || num==50 || num == 65) {
                                newStar.setImageResource(R.drawable.ic_magnet)
                                isMagnet = true
                            } else if (num == 70 || num == 75 || num == 85 || num == 55 || num==60) {
                                newStar.setImageResource(R.drawable.ic_slow)
                                isSlow = true
                            } else if (num==40 || num == 80 || num==85 || num == 95) {
                                newStar.setImageResource(R.drawable.ic_more_money)
                                isMoreMoney = true
                            } else if (num == 90) {
                                newStar.setImageResource(R.drawable.ic_best)
                                isBigHit = true
                            } } else {
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
                        mover.interpolator = AccelerateInterpolator(1f)
                        val rotator = ObjectAnimator.ofFloat(
                            newStar, View.ROTATION,
                            (Math.random() * 1080).toFloat()
                        )
                        rotator.interpolator = LinearInterpolator()

                        val set = AnimatorSet()
                        set.playTogether(mover, rotator)

                        set.duration = (Math.random() * maxSpeed + minSpeed).toLong()


                        set.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                if (magn) {
                                    if (isGolden) {
                                        score += GoldenLevel
                                        isGolden = false
                                        Constants.GoldenAmount++
                                        Constants.GoldenMoney+=GoldenLevel
                                        Constants.MagnetMoney+=GoldenLevel
                                    } else
                                        Constants.normalMoey++
                                        Constants.MagnetMoney++
                                        score++

                                    binding.txt.setTextColor(resources.getColor(R.color.Green))
                                    binding.txt.text = score.toString()
                                }
                                container.removeView(newStar)
                            }
                        })
                        set.start()

                        newStar.setOnClickListener {



                            if (newStar.visibility == View.GONE) {
                            } else {
                                lifecycleScope.launch {
                                    withContext(Dispatchers.Main) {
                                        if (timer == 0) {
                                            newStar.visibility = View.GONE
                                        }

                                        if (isSpecialAbility) {
                                            if (isGolden) {


                                                if (isBigHit)
                                                    Constants.BigHitMoney+=GoldenLevel
                                                if (isMagnet)
                                                    Constants.MagnetMoney+=GoldenLevel
                                                if (isSlow)
                                                    Constants.SlowMoney+=GoldenLevel
                                                if(isMoreMoney)
                                                    Constants.moreMoneyMoney+=GoldenLevel

                                                score += GoldenLevel
                                                Constants.GoldenMoney+=GoldenLevel
                                                isGolden = false
                                                Constants.GoldenAmount++
                                            } else if (isSlow) {
                                                Constants.SlowAmount++
                                                minSpeed = 2600
                                                maxSpeed = 3000
                                                delayer = 200
                                                //todo : start timer for 5 seconds then restore them to 900-1900
                                                GlobalScope.launch() {
                                                    withContext(Dispatchers.Main) {
                                                        binding.slow.visibility = View.VISIBLE
                                                        binding.slowTimer.visibility = View.VISIBLE
                                                    }
                                                    var slowLeveltimer = slowLevel
                                                    repeat(slowLeveltimer) {
                                                        withContext(Dispatchers.Main) {
                                                            binding.slowTimer.text =
                                                                slowLeveltimer.toString()
                                                        }
                                                        delay(1000)
                                                        slowLeveltimer--
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        binding.slowTimer.visibility = View.GONE
                                                        binding.slow.visibility = View.GONE

                                                    }
                                                    delayer = 300
                                                    minSpeed = 1200
                                                    maxSpeed = 2200
                                                    isSlow = false

                                                }
                                            } else if (isBigHit) {

                                                Constants.BigHitAmount++
                                                delayer=20
                                                minSpeed=2200
                                                maxSpeed=4000
                                                GlobalScope.launch() {
                                                    withContext(Dispatchers.Main) {
                                                        binding.bigHit.visibility = View.VISIBLE
                                                        binding.bigHitTimer.visibility = View.VISIBLE
                                                    }
                                                    var BigHittimer = bigHitLevel
                                                    repeat(BigHittimer) {
                                                        withContext(Dispatchers.Main) {
                                                            binding.bigHitTimer.text =
                                                                BigHittimer.toString()
                                                        }
                                                        delay(1000)
                                                        BigHittimer--
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        binding.bigHit.visibility = View.GONE
                                                        binding.bigHitTimer.visibility = View.GONE
                                                        isBigHit = false

                                                    }
                                                    delayer=300
                                                    minSpeed = 1200
                                                    maxSpeed = 2200
                                                }


                                            } else if (isMoreMoney) {
                                                Constants.MoreMoneyAmount++
                                                delayer = 50
                                                GlobalScope.launch() {
                                                    withContext(Dispatchers.Main) {
                                                        binding.moreMoney.visibility = View.VISIBLE
                                                        binding.moreMoneyTimer.visibility =
                                                            View.VISIBLE
                                                    }
                                                    var BigHittimer = moreMoneyLevel
                                                    repeat(BigHittimer) {
                                                        withContext(Dispatchers.Main) {
                                                            binding.moreMoneyTimer.text =
                                                                BigHittimer.toString()
                                                        }
                                                        delay(1000)
                                                        BigHittimer--
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        binding.moreMoney.visibility = View.GONE
                                                        binding.moreMoneyTimer.visibility =
                                                            View.GONE
                                                        isMoreMoney = false

                                                    }
                                                    delayer = 300
                                                }
                                            } else if (isMagnet) {
                                                Constants.MagnetAmount++
                                                GlobalScope.launch() {
                                                    delayer = 200
                                                    withContext(Dispatchers.Main) {
                                                        binding.magnet.visibility = View.VISIBLE
                                                        binding.magnetTimer.visibility =
                                                            View.VISIBLE
                                                    }
                                                    magn = true
                                                    var BigHittimer = MagnetLevel
                                                    repeat(BigHittimer) {
                                                        withContext(Dispatchers.Main) {
                                                            binding.magnetTimer.text =
                                                                BigHittimer.toString()
                                                        }
                                                        delay(1000)
                                                        BigHittimer--
                                                    }
                                                    withContext(Dispatchers.Main) {
                                                        binding.magnet.visibility = View.GONE
                                                        binding.magnetTimer.visibility = View.GONE
                                                        isMagnet = false
                                                    }
                                                    magn = false
                                                    delayer = 300
                                                }
                                            }
                                        } else {

                                            Constants.normalMoey++
                                            score++
                                            if (isBigHit)
                                                Constants.BigHitMoney++
                                            if (isMagnet)
                                                Constants.MagnetMoney++
                                            if (isSlow)
                                                Constants.SlowMoney++
                                            if(isMoreMoney)
                                                Constants.moreMoneyMoney++


                                        }

                                        binding.txt.setTextColor(resources.getColor(R.color.Green))
                                        binding.txt.text = score.toString()
                                        newStar.visibility = View.GONE
                                }
                            }
                        }
                        }
                    } catch (e: Exception) {
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
        }catch (E:java.lang.Exception){
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
        GameEnd()
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
        }catch (E:java.lang.Exception){

        }
    }
}