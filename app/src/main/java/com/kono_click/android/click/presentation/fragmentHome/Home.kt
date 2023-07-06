package com.kono_click.android.click.presentation.fragmentHome

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kono_click.android.click.utils.Constants
import com.kono_click.android.click.utils.Constants.AllGolden
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
class HomeFragment : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var userMoney: Long? = null
    private var score: Int? = null

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var rotate: Animation
    private lateinit var bounce: Animation
    private lateinit var fadeIn: Animation
    private lateinit var fadeOut: Animation
    private lateinit var leftToRight: Animation
    private lateinit var rightToLeft: Animation

    private lateinit var clickSound: MediaPlayer
    private lateinit var soundModeClicked: MediaPlayer


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
        //setupAdds()
        setAnimations()
        startAnimations()
        setSoundAsLastTime()
        setVariables()
        setOnClickListeners()
        setViews()
    }

    private fun setViews() {
        val txt = score.toString() + " $"
        binding.BestScore.text = txt
    }

    private fun setOnClickListeners() {
        binding.StartTest.setOnClickListener {
            playClickSound()
            //showAdd()
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_home_to_the_game)
        }

        binding.ShopButton.setOnClickListener {
            playClickSound()
            startActivity(Intent(activity, ShopActivity::class.java))
        }

        binding.SoundBtn.setOnClickListener {
            triggerSoundMode()
        }

        binding.tenSecSwitch.setOnCheckedChangeListener { _, state ->
            viewModel.setIsTenSec(state)
            isTenSec = state
        }
        binding.goldenSwitch.setOnCheckedChangeListener { _, state ->
            viewModel.setIsAllGolden(state)
            isTenSec = state
        }
    }

    private fun triggerSoundMode() {
        if (sound) {
            sound = false
            viewModel.setSound(false)
            binding.SoundBtn.setImageResource(R.drawable.sound_off)
        } else {
            soundModeClicked.start()
            sound = true
            viewModel.setSound(true)
            binding.SoundBtn.setImageResource(R.drawable.sound_on)
        }
    }

    private fun playClickSound() {
        if (sound) {
            clickSound.start()
        }
    }

    private fun showAdd() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
        } catch (_: Exception) {
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
        binding.upperView.visibility = View.VISIBLE
        binding.upperView.startAnimation(leftToRight)

        binding.lowerView.visibility = View.VISIBLE
        binding.lowerView.startAnimation(rightToLeft)

        // Words

        binding.upperText.visibility = View.VISIBLE
        binding.LowerText.visibility = View.VISIBLE
        if (!animationSeen) {
            binding.upperText.startAnimation(fadeOut)
            binding.LowerText.startAnimation(fadeOut)
            animationSeen = true
        }

    }

    private fun setSoundAsLastTime() {
        soundModeClicked = MediaPlayer.create(activity, R.raw.mouse_clickmp3)
        clickSound = MediaPlayer.create(activity, R.raw.touch)
        val soundd = viewModel.getSound()
        if (soundd) {
            sound = true
            binding.SoundBtn.setImageResource(R.drawable.sound_on)
        } else {

            sound = false
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

    @SuppressLint("SetTextI18n")
    private fun setVariables() {
        userMoney = viewModel.getUserMoney()
        binding.userMoney.text = "$userMoney $"
        Constants.UserMoney = userMoney as Long
        score = viewModel.getHighScore()

        AllGolden = viewModel.getNumberOfAllGoldenTokens()
        tenSec = viewModel.getNumberOfTenSecTokens()

        isAllGolden = viewModel.getIsAllGolden()
        isTenSec = viewModel.getIsTenSec()

        binding.tenSecSwitch.isChecked = isTenSec
        binding.goldenSwitch.isChecked = isAllGolden

        if (AllGolden == 0L) {
            isAllGolden = false
            binding.AllGoldenSwitchText.visibility = View.GONE
            binding.goldenSwitch.visibility = View.GONE
        } else {
            isAllGolden = true
            binding.AllGoldenSwitchText.visibility = View.VISIBLE
            binding.goldenSwitch.visibility = View.VISIBLE
        }
        if (tenSec == 0L) {
            isTenSec = false
            binding.tenSecSwitchText.visibility = View.GONE
            binding.tenSecSwitch.visibility = View.GONE
        } else {
            isTenSec = true
            binding.tenSecSwitchText.visibility = View.VISIBLE
            binding.tenSecSwitch.visibility = View.VISIBLE
        }

        val magnetLevel = viewModel.getMagnetLevel()
        viewModel.saveMagnetLevelToConstants(magnetLevel)

        val goldLevel = viewModel.getGoldLevel()
        viewModel.saveGoldLevelToConstants(goldLevel)

        Constants.SlowMotionLevel = viewModel.getSlowMotionLevel()
        Constants.MoreMoneyLevel = viewModel.getMoreMoneyLevel()
    }

    override fun onResume() {
        setVariables()
        val txt = score.toString() + " $"
        binding.BestScore.text = txt
        super.onResume()
    }
    private fun setupAdds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(), "ca-app-pub-4031659564383807/4979093119", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }
}