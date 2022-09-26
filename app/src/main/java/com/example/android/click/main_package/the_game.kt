package com.example.android.click.main_package

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
import com.example.android.click.Constants
import com.example.android.click.R
import com.example.android.click.databinding.FragmentRunTestBinding
import com.example.android.click.info
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.*

class the_game : Fragment() {


    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"

        lateinit var sharedPreferencee: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        private lateinit var binding: FragmentRunTestBinding
        var score = 0
        var timer = Constants.time
        var time = Constants.time
        var BreakLoop = false

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

           binding.Timer.text=time.toString()
            sharedPreferencee = requireActivity().getSharedPreferences(
                getString(R.string.highscore),
                Context.MODE_PRIVATE
            )
            editor = sharedPreferencee.edit()
            GlobalScope.launch {
                delay((Math.random() * 800 + 200).toLong())

                withContext(Dispatchers.Main) {
                    GlobalScope.launch {
                        repeat(time) {
                            delay(1000)
                            withContext(Dispatchers.Main) {
                                timer--
                                binding.Timer.text = timer.toString()
                                if (timer == 0) {
                                    BreakLoop = true
                                    GameEnd()
                                    this.cancel()
                                }
                            }
                        }
                    }

                    MakeItRain(view)
                }
                //todo: Start timer
            }

        }

        fun MakeItRain(view: View) {
            GlobalScope.launch {
                while (true) {
                    if(timer<2)continue
                    delay(300)
                    withContext(Dispatchers.Main) {

                        val star: ImageView = view.findViewById(R.id.star)
                        val container = star.parent as ViewGroup
                        val containerW = container.width
                        val containerH = container.height
                        var starW: Float = star.width.toFloat()
                        var starH: Float = star.height.toFloat()


                        val newStar = AppCompatImageView(requireActivity())
                        newStar.setImageResource(R.drawable.ic_baseline_attach_money_24)
                        newStar.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT
                        )
                        container.addView(newStar)
                        newStar.setPadding(100, 100, 100, 100)

                        newStar.translationX = Math.random().toFloat() *
                                containerW - starW / 2

                        // TODO: random positioned view and timer (both after some time)
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

                        // TODO: listener for random view get score to our data base
                        val set = AnimatorSet()
                        set.playTogether(mover, rotator)
                        set.duration = (Math.random() * 1900 + 600).toLong()


                        set.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                container.removeView(newStar)
                            }
                        })
                        set.start()

                        newStar.setOnClickListener {
                            // TODO: make score change
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    if (timer==0){
                                        newStar.visibility = View.GONE
                                    }else {
                                        score++
                                        binding.txt.setTextColor(resources.getColor(R.color.Green))
                                        binding.txt.text = score.toString()
                                        newStar.visibility = View.GONE
                                    }
                                }
                            }
                        }

                    }


                    if (BreakLoop) {
                        break

                    }
                    delay(90)
                }
            }
        }

        private fun GameEnd() {

            var i = sharedPreferencee.getInt("high",0)
            Constants.HighScore=i
            Constants.scoree=score
            if (i<score){
                editor.putInt("high",score).commit()
            }

            var money = sharedPreferencee.getLong("UserMoney",0)
            editor.putLong("UserMoney", money + score).commit()
            try {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                }
            } catch (e: Exception) {
            }
            startActivity(Intent(activity, info::class.java))
            findNavController().popBackStack()


        }


    }

