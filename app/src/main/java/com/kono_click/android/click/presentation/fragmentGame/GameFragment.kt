package com.kono_click.android.click.presentation.fragmentGame

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.kono_click.android.click.utils.Constants
import com.kono_click.android.click.R
import com.kono_click.android.click.databinding.FragmentGameBinding
import com.kono_click.android.click.presentation.activityInfo.InfoActivity
import com.kono_click.android.click.utils.Constants.time
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: FragmentGameBinding

    private lateinit var container: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdds()
        showScore()
        setUpMedeiaPlayers()
        hideTimers()
        viewModel.checkOneTimeUseItems()
        startTimer()
        makeItRain(view)
    }

    private fun startTimer() {
        binding.Timer.text = viewModel.time.toString()
        lifecycleScope.launch {
            delay((Math.random() * 250 + 150).toLong())
            withContext(Dispatchers.Main) {
                lifecycleScope.launch {
                    gameTimer()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun makeItRain(view: View) {
        GlobalScope.launch {
            while (true) {
                if (viewModel.isStop) {
                    continue
                }
                viewModel.delayForPhase()
                val num = viewModel.getRandomNumber()
                val isSpecialAbility = viewModel.checkIfIsSpecialAbility()

                if (viewModel.timer < 0) continue

                withContext(Dispatchers.Main) {
                    try {
                        setContainerSize(view)
                        setControlVariables()
                        createFallingDollar(isSpecialAbility, num)
                    } catch (_: Exception) {
                    }
                }
                if (viewModel.breakLoopToStop) {
                    break
                }
                delay(viewModel.delayer)
            }
        }
    }

    private fun createFallingDollar(isSpecialAbility: Boolean, num: Int) {
        val newDollar: ImageView = AppCompatImageView(requireActivity())
        val abilityType = viewModel.decideTheAbility(isSpecialAbility, num)
        newDollar.setImageResource(viewModel.getDollarImage(abilityType))
        container.addView(newDollar)
        createDollarAttributes(newDollar, abilityType)
        newDollar.setOnClickListener { newDollar ->
            dollarClicked(abilityType, newDollar)
        }

    }

    private fun dollarClicked(abilityType: AbilityType, newDollar: View) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                if (abilityType != AbilityType.NONE) {
                    if (abilityType == AbilityType.IS_GOLDEN) {
                        viewModel.collectGold()
                    } else if (abilityType == AbilityType.IS_SLOW) {
                        viewModel.slowMotionEffect()
                        startSlowMotionTimer()
                    } else if (abilityType == AbilityType.IS_BIG_HIT) {
                        viewModel.isBigHitEffect()
                        startBigHitTimer()
                    } else if (abilityType == AbilityType.IS_MORE_MONEY) {
                        viewModel.moreMoneyEffect()
                        startMoreMoneyTimer()
                    } else if (abilityType == AbilityType.IS_MAGNET) {
                        viewModel.magnetEffect()
                       startMagnetTimer()
                    }
                } else {
                    viewModel.collectDollars()
                }

                binding.txt.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.Green
                    )
                )
                binding.txt.text = viewModel.score.toString()
                newDollar.visibility = View.GONE
            } catch (_: Exception) {
            }
        }
    }
    private fun startMagnetTimer() {
        if (viewModel.tim4 > 0) {
            viewModel.tim4 = viewModel.magnetLevel
            binding.magnetTimer.text =
                viewModel.tim4.toString()
        } else {
            GlobalScope.launch {
                showAbilityTimer(
                    binding.magnet,
                    binding.magnetTimer
                )
                viewModel.magn = true
                viewModel.tim4 = viewModel.magnetLevel
                while (viewModel.tim4 != 0) {
                    binding.magnetTimer.text =
                        viewModel.tim4.toString()
                    delay(1000)
                    if (!viewModel.isStop) viewModel.tim4--
                }
                hideAbilityTimer(
                    binding.magnet,
                    binding.magnetTimer
                )
                viewModel.magn = false
            }
        }
    }

    private fun startMoreMoneyTimer() {
        if (viewModel.tim3 > 0) {
            viewModel.tim3 = viewModel.moreMoneyLevel
            binding.moreMoneyTimer.text =
                viewModel.tim3.toString()
        } else {
            GlobalScope.launch {
                showAbilityTimer(
                    binding.moreMoney,
                    binding.moreMoneyTimer
                )
                viewModel.tim3 = viewModel.moreMoneyLevel
                while (viewModel.tim3 != 0) {
                    binding.moreMoneyTimer.text = viewModel.tim3.toString()
                    delay(1000)
                    if (!viewModel.isStop) viewModel.tim3--
                }
                hideAbilityTimer(
                    binding.moreMoney,
                    binding.moreMoneyTimer
                )
                viewModel.isMoreMoneyG = false
            }
        }
    }

    private fun startBigHitTimer() {
        if (viewModel.tim2 > 0) {
            viewModel.tim2 = viewModel.bigHitLevel
            binding.bigHitTimer.text = viewModel.tim2.toString()
        } else {
            GlobalScope.launch {
                showAbilityTimer(
                    binding.bigHit,
                    binding.bigHitTimer
                )
                viewModel.tim2 = viewModel.bigHitLevel
                while (viewModel.tim2 != 0) {
                    binding.bigHitTimer.text = viewModel.tim2.toString()
                    delay(1000)
                    if (!viewModel.isStop) viewModel.tim2--
                }
                hideAbilityTimer(
                    binding.bigHit,
                    binding.bigHitTimer
                )
                viewModel.isBigHitG = false
            }
        }
    }

    private fun startSlowMotionTimer() {
        if (viewModel.tim1 > 0) {
            viewModel.tim1 = viewModel.slowLevel
            binding.slowTimer.text = viewModel.tim1.toString()
        } else {
            //start timer
            GlobalScope.launch(Dispatchers.Main) {
                showAbilityTimer(binding.slow, binding.slowTimer)
                viewModel.tim1 = viewModel.slowLevel
                while (viewModel.tim1 > 0) {
                    binding.slowTimer.text = viewModel.tim1.toString()
                    delay(1000)
                    if (!viewModel.isStop) viewModel.tim1--
                }
                hideAbilityTimer(
                    binding.slow,
                    binding.slowTimer
                )
                viewModel.isSlowG = false
            }
        }
    }

    private fun createDollarAttributes(newDollar: ImageView, abilityType: AbilityType) {
        setDollarDimentions(newDollar)
        setDollarAnimation(newDollar, abilityType)
    }

    private fun setDollarAnimation(newDollar: ImageView, abilityType: AbilityType) {

        val mover = ObjectAnimator.ofFloat(
            newDollar, View.TRANSLATION_Y,
            -viewModel.dollarH, viewModel.containerH + viewModel.dollarH
        )
        mover.interpolator = AccelerateInterpolator(1f)

        val rotator = ObjectAnimator.ofFloat(
            newDollar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )

        rotator.interpolator = LinearInterpolator()

        val set = AnimatorSet()

        set.playTogether(mover, rotator)

        val speed: Long = (Math.random() * viewModel.minSpeed + viewModel.maxSpeed).toLong()
        set.duration = speed

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (viewModel.magn && newDollar.visibility != View.GONE && viewModel.timer > 0 && !viewModel.isStop) {
                    viewModel.collectDollarsOnly(abilityType)
                    try {
                        viewModel.playSound(viewModel.sword)
                        binding.txt.setTextColor(resources.getColor(R.color.Green))
                        binding.txt.text = viewModel.score.toString()
                    } catch (_: Exception) {
                    }
                }
                container.removeView(newDollar)
            }
        })
        set.start()
    }

    private fun setDollarDimentions(newDollar: ImageView) {
        newDollar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        newDollar.setPadding(viewModel.hitBox, viewModel.hitBox, viewModel.hitBox, viewModel.hitBox)
        newDollar.translationX = (Math.random().toFloat() *
                viewModel.containerW - viewModel.dollarW / 2)
    }

    private fun setContainerSize(view: View) {
        val dollar: ImageView = view.findViewById(R.id.star)
        container = dollar.parent as ViewGroup
        viewModel.containerW = container.width
        viewModel.containerH = container.height
        viewModel.dollarW = dollar.width.toFloat()
        viewModel.dollarH = dollar.height.toFloat()
    }

    private fun setControlVariables() {
        val controlObject: ControlObject =
            viewModel.returnControlVariables()
        viewModel.minSpeed = controlObject.minSpeed
        viewModel.maxSpeed = controlObject.maxSpeed
        viewModel.delayer = controlObject.delayer.toLong()
    }

    private fun showScore() {
        val bounce = AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce)
        binding.txt.visibility = View.VISIBLE
        binding.txt.startAnimation(bounce)
    }

    private fun hideTimers() {
        hideAbilityTimer(binding.bigHitTimer, binding.bigHit)
        hideAbilityTimer(binding.magnet, binding.magnetTimer)
        hideAbilityTimer(binding.slow, binding.slowTimer)
        hideAbilityTimer(binding.moreMoney, binding.moreMoneyTimer)
    }

    private fun showAbilityTimer(view1: View, view2: View) {
        try {
            view1.visibility = View.VISIBLE
            view2.visibility = View.VISIBLE
        } catch (_: Exception) {
        }
    }

    private fun hideAbilityTimer(view1: View, view2: View) {
        try {
            view1.visibility = View.GONE
            view2.visibility = View.GONE
        } catch (_: Exception) {
        }
    }

    private fun showAdds() {
        try {
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
            }
        } catch (_: Exception) {
        }
    }

    override fun onDestroy() {
        if (!viewModel.gameEnded) {
            viewModel.gameEndCalculator(viewModel.score)
            showAdds()
        }
        super.onDestroy()
    }

    private fun setUpMedeiaPlayers() {
        viewModel.ballwall = MediaPlayer.create(activity, R.raw.ballwall)
        viewModel.beeb = MediaPlayer.create(activity, R.raw.beeb)
        viewModel.bullet = MediaPlayer.create(activity, R.raw.bullet)
        viewModel.firec = MediaPlayer.create(activity, R.raw.firec)
        viewModel.gun = MediaPlayer.create(activity, R.raw.gun)
        viewModel.notification = MediaPlayer.create(activity, R.raw.notification)
        viewModel.sword = MediaPlayer.create(activity, R.raw.sword)
    }

    private suspend fun gameTimer() {
        while (time > 0) {
            delay(1000)
            withContext(Dispatchers.Main) {
                viewModel.changePhase()
                if (!viewModel.isStop) {
                    viewModel.timer--
                }
                binding.Timer.text = viewModel.timer.toString()
                if (viewModel.timer == 0) {
                    viewModel.endTheGame()
                    if (!viewModel.gameEnded) {
                        showAdds()
                    }
                    startActivity(Intent(activity, InfoActivity::class.java))
                    findNavController().popBackStack()
                    viewModel.cancelIt = true
                    this.cancel()
                }
            }

        }
    }

    override fun onStop() {
        viewModel.isStop = true
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        viewModel.isStop = false
    }

    private fun setUpAdds() {
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

data class ControlObject(var minSpeed: Int, var maxSpeed: Int, var delayer: Int)
enum class AbilityType {
    NONE, IS_GOLDEN, IS_MAGNET, IS_SLOW, IS_MORE_MONEY, IS_BIG_HIT
}