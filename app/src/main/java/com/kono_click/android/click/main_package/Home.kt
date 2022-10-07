package com.kono_click.android.click.main_package

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.kono_click.android.click.Constants
import com.kono_click.android.click.R
import com.kono_click.android.click.Shop.ShopActivity
import com.kono_click.android.click.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class home : Fragment() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Moha"
    private var UserMoney: Long? = null


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

        UserMoney = sharedPreference.getLong("UserMoney", 0)
        binding.userMoney.text = UserMoney.toString() + " $"
        Constants.UserMoney = UserMoney as Long

        var score = sharedPreference.getInt("high", 0)

        var txt = score.toString() + " $"
        binding.BestScore.text = txt

        binding.StartTest.setOnClickListener {
            lifecycleScope.launch {
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
        binding.ShopButton.setOnClickListener {
            startActivity(Intent(activity, ShopActivity::class.java))
        }

        binding.SettingsBtn.setOnClickListener {
            Toast.makeText(activity,"TODO",Toast.LENGTH_SHORT).show()

        }

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



