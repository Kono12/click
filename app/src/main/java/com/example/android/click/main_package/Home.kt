package com.example.android.click.main_package

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android.click.Constants
import com.example.android.click.R
import com.example.android.click.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class home : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"
    private var UserMoney : Long? = null


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



        sharedPreference = requireActivity().getSharedPreferences(
            getString(R.string.highscore),
            Context.MODE_PRIVATE
        )
        editor = sharedPreference.edit()

        UserMoney = sharedPreference.getLong("UserMoney",0)
        binding.userMoney.text=UserMoney.toString() + " $"
        Constants.UserMoney= UserMoney as Long

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt

        binding.StartTest.setOnClickListener {
            GlobalScope.launch {

            }
            try {
                if (mInterstitialAd != null) {
                //    mInterstitialAd?.show(requireActivity())
                }
            } catch (e: Exception) {
            }
            Navigation.findNavController(requireActivity(), R.id.my_nav_host_fragment)
                .navigate(R.id.action_home_to_the_game)
        }

        binding.time1.setOnClickListener {
            Constants.time=60
            binding.timeScreen.text=" 60 s "
        }
        binding.time2.setOnClickListener {
                Constants.time=30
                binding.timeScreen.text=" 30 s "

        }
        binding.time3.setOnClickListener {
            Constants.time=15
            binding.timeScreen.text=" 15 s "
        }

    }
}



