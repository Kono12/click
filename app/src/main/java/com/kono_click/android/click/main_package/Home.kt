package com.kono_click.android.click.main_package

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kono_click.android.click.Constants
import com.kono_click.android.click.Constants.GoldLevel
import com.kono_click.android.click.Constants.MagmetLevel
import com.kono_click.android.click.Constants.sound
import com.kono_click.android.click.R
import com.kono_click.android.click.Shop.ShopActivity
import com.kono_click.android.click.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class home : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"
    private var UserMoney: Long? = null

    private lateinit var clickSound:MediaPlayer
    private lateinit var soundModeClicked:MediaPlayer

    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(requireActivity(), "ca-app-pub-4031659564383807/4979093119", adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    Log.d(TAG, adError?.toString().toString())
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    Log.d(TAG, "Ad was loaded")
//                    mInterstitialAd = interstitialAd
//                }
//            })


        soundModeClicked=MediaPlayer.create(activity,R.raw.mouse_clickmp3)
        clickSound=MediaPlayer.create(activity,R.raw.touch)

        sharedPreference = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreference.edit()

        UserMoney = sharedPreference.getLong("UserMoney", 0)
        binding.userMoney.text = UserMoney.toString() + " $"
        Constants.UserMoney = UserMoney as Long

        setVariables()

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt

        binding.StartTest.setOnClickListener {
            if (sound)
                clickSound.start()

            try {
                if (mInterstitialAd != null) {
                    //    mInterstitialAd?.show(requireActivity())
                }
            } catch (e: Exception) {
            }
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_home_to_the_game)
        }

        binding.ShopButton.setOnClickListener {
            if (sound){
            clickSound.start()}
            startActivity(Intent(activity, ShopActivity::class.java))
        }

         setSoundAsLastTime()
        binding.SoundBtn.setOnClickListener {
            if (Constants.sound) {
                Constants.sound = false
                editor.putBoolean("sound",false).commit()
                binding.SoundBtn.setImageResource(R.drawable.sound_off)
            } else {
                soundModeClicked.start()
                Constants.sound = true
                editor.putBoolean("sound",true).commit()
                binding.SoundBtn.setImageResource(R.drawable.sound_on)
            }
        }

    }

    private fun setSoundAsLastTime() {
        var soundd = sharedPreference.getBoolean("sound", true)
         if (soundd){
             Constants.sound = true
             binding.SoundBtn.setImageResource(R.drawable.sound_on)
         }else{

             Constants.sound = false
             binding.SoundBtn.setImageResource(R.drawable.sound_off)
         }
    }


    private fun setVariables() {
        var MagmetLevel1 = sharedPreference.getInt("Magnet",5)

        if (MagmetLevel1 == 7) {
            MagmetLevel=8
        } else if (MagmetLevel1 == 8){
            MagmetLevel=11
        }
        else if (MagmetLevel1 == 9){
            MagmetLevel=15
        }

        var GoldLevel1 = sharedPreference.getInt("Gold", 5)
        if (GoldLevel1 == 7) {
            Constants.GoldLevel=8
        } else if (GoldLevel1 == 8){
            Constants.GoldLevel=11
        }
        else if (GoldLevel1 == 9){
            Constants.GoldLevel=15
        }

        Constants.SlowMotionLevel = sharedPreference.getInt("Slow", 5)
        Constants.MoreMoneyLevel = sharedPreference.getInt("More", 5)
    }

    override fun onResume() {


        UserMoney = sharedPreference.getLong("UserMoney", 0)
        binding.userMoney.text = UserMoney.toString() + " $"
        Constants.UserMoney = UserMoney as Long

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt

        super.onResume()
    }

}



