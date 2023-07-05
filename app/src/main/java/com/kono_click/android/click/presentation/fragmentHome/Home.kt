package com.kono_click.android.click.presentation.fragmentHome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.kono_click.android.click.utils.Constants
import com.kono_click.android.click.utils.Constants.AllGolden
import com.kono_click.android.click.utils.Constants.MagmetLevel
import com.kono_click.android.click.utils.Constants.animationSeen
import com.kono_click.android.click.utils.Constants.isAllGolden
import com.kono_click.android.click.utils.Constants.isTenSec
import com.kono_click.android.click.utils.Constants.sound
import com.kono_click.android.click.utils.Constants.tenSec
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.FragmentHomeBinding
import com.kono_click.android.click.presentation.activityShop.ShopActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class home : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"
    private var UserMoney: Long? = null

    private lateinit var rotate: Animation
    private lateinit var bounce: Animation
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    private lateinit var leftToRight: Animation
    private lateinit var rightToLeft : Animation

    private lateinit var clickSound: MediaPlayer
    private lateinit var soundModeClicked: MediaPlayer

    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        setAnimations()
        startAnimations()
        soundModeClicked = MediaPlayer.create(activity, R.raw.mouse_clickmp3)
        clickSound = MediaPlayer.create(activity, R.raw.touch)

        sharedPreference = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreference.edit()

        //todo: hack
      //  editor.putLong("UserMoney", Long.MAX_VALUE).commit()

        UserMoney = sharedPreference.getLong("UserMoney", 0)
        binding.userMoney.text = UserMoney.toString() + " $"
        Constants.UserMoney = UserMoney as Long

        setVariables()
        setSoundAsLastTime()
        setOnClickListeners()

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt


    }

    private fun setOnClickListeners() {
        binding.StartTest.setOnClickListener {
            if (sound)
                clickSound.start()

//            try {
//                if (mInterstitialAd != null) {
//                        mInterstitialAd?.show(requireActivity())
//                }
//            } catch (e: Exception) {
//            }

            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_home_to_the_game)
        }

        binding.ShopButton.setOnClickListener {
            if (sound) {
                clickSound.start()
            }
            startActivity(Intent(activity, ShopActivity::class.java))
        }

        binding.SoundBtn.setOnClickListener {
            if (Constants.sound) {
                Constants.sound = false
                editor.putBoolean("sound", false).commit()
                binding.SoundBtn.setImageResource(R.drawable.sound_off)
            } else {
                soundModeClicked.start()
                Constants.sound = true
                editor.putBoolean("sound", true).commit()
                binding.SoundBtn.setImageResource(R.drawable.sound_on)
            }
        }

        binding.tenSecSwitch.setOnCheckedChangeListener { compoundButton, state ->
            editor.putBoolean("UseTenSec", state).commit()
            isTenSec = state
        }
        binding.goldenSwitch.setOnCheckedChangeListener { compoundButton, state ->
            editor.putBoolean("UseGolden", state).commit()
            isTenSec = state
        }
    }

    private fun startAnimations() {
        // buttons
        binding.StartTest.visibility = View.VISIBLE
        binding.StartTest.startAnimation(leftToRight)

        binding.ShopButton.visibility = View.VISIBLE
        binding.ShopButton.startAnimation(rightToLeft)

        binding.SoundBtn.visibility = View.VISIBLE
        binding.SoundBtn.startAnimation(leftToRight)

        // lines
        binding.upperView.visibility=View.VISIBLE
        binding.upperView.startAnimation(leftToRight)

        binding.lowerView.visibility=View.VISIBLE
        binding.lowerView.startAnimation(rightToLeft)

        // Words

        binding.upperText.visibility = View.VISIBLE
        binding.LowerText.visibility = View.VISIBLE
        if(!animationSeen) {
            binding.upperText.startAnimation(fadeOut)
            binding.LowerText.startAnimation(fadeOut)
            animationSeen = true
        }

    }

    private fun setSoundAsLastTime() {
        var soundd = sharedPreference.getBoolean("sound", true)
        if (soundd) {
            Constants.sound = true
            binding.SoundBtn.setImageResource(R.drawable.sound_on)
        } else {

            Constants.sound = false
            binding.SoundBtn.setImageResource(R.drawable.sound_off)
        }
    }

    private fun setAnimations() {
        rotate = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate)
        bounce = AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce)
        fadeIn = AnimationUtils.loadAnimation(requireActivity(), R.anim.fadein)
        fadeOut = AnimationUtils.loadAnimation(requireActivity(), R.anim.fadeout)
        leftToRight = AnimationUtils.loadAnimation(requireActivity(), R.anim.lefttoright)
        rightToLeft = AnimationUtils.loadAnimation(requireActivity(), R.anim.righttoleft)

    }

    private fun setVariables() {

        AllGolden = sharedPreference.getLong("AllGolden", 0).toInt()
        tenSec = sharedPreference.getLong("TenSec", 0).toInt()

        isAllGolden = sharedPreference.getBoolean("UseGolden", false)
        isTenSec = sharedPreference.getBoolean("UseTenSec", false)

        binding.tenSecSwitch.isChecked = isTenSec
        binding.goldenSwitch.isChecked = isAllGolden

        if (AllGolden == 0) {
            isAllGolden=false
            binding.AllGoldenSwitchText.visibility = View.GONE
            binding.goldenSwitch.visibility = View.GONE
        } else {
            isAllGolden=true
            binding.AllGoldenSwitchText.visibility = View.VISIBLE
            binding.goldenSwitch.visibility = View.VISIBLE
        }
        if (tenSec == 0) {
            isTenSec=false
            binding.tenSecSwitchText.visibility = View.GONE
            binding.tenSecSwitch.visibility = View.GONE
        } else {
            isTenSec=true
            binding.tenSecSwitchText.visibility = View.VISIBLE
            binding.tenSecSwitch.visibility = View.VISIBLE
        }


        var MagmetLevel1 = sharedPreference.getInt("Magnet", 5)

        if (MagmetLevel1 == 7) {
            MagmetLevel = 8
        } else if (MagmetLevel1 == 8) {
            MagmetLevel = 11
        } else if (MagmetLevel1 == 9) {
            MagmetLevel = 15
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

        setVariables()

        UserMoney = sharedPreference.getLong("UserMoney", 0)
        binding.userMoney.text = UserMoney.toString() + " $"
        Constants.UserMoney = UserMoney as Long

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt

        super.onResume()
    }


}



