package com.example.android.click.main_package

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.click.Constants
import com.example.android.click.R
import com.example.android.click.databinding.FragmentRunTestBinding
import com.example.android.click.info
import kotlinx.coroutines.*


class runTest : Fragment() {

    lateinit var sharedPreferencee: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var binding: FragmentRunTestBinding
    var score = 0
    var timer = 60
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

        sharedPreferencee = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferencee.edit()
        lifecycleScope.launch {
            delay((Math.random() * 1000 + 200).toLong())

            withContext(Dispatchers.Main) {
                lifecycleScope.launch {
                    repeat(60) {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            timer--
                            binding.Timer.text = timer.toString()
                            if (timer == 0) {
                                BreakLoop = true
                                this.cancel()
                            }
                        }
                    }
                }
                MakeItRain(view)
            }
            //todo: Start timer

            this.cancel()
        }

    }

    fun MakeItRain(view: View) {
        lifecycleScope.launch {
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
                    set.duration = (Math.random() * 1900 + 800).toLong()


                    set.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            container.removeView(newStar)
                        }
                    })
                    set.start()
                    newStar.setOnClickListener {
                        // TODO: make score change
                        lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                score++
                                binding.txt.setTextColor(resources.getColor(R.color.Green))
                                binding.txt.text = score.toString()
                                newStar.visibility = View.GONE
                            }
                        }
//                        Toast.makeText(requireActivity(), "hh", Toast.LENGTH_SHORT).show()
                    }

                }

                if (BreakLoop) {
                    GameEnd()
                    break

                }
                delay(400)
            }
        }
    }

    private suspend fun GameEnd() {

        withContext(Dispatchers.Main) {
            var i = sharedPreferencee.getInt("high",0)
            Constants.HighScore=i
            Constants.score=score
            if (i<score){
                editor.putInt("high",score).commit()
            }

           startActivity(Intent(activity,info::class.java))
            findNavController().popBackStack()
        }

    }


}