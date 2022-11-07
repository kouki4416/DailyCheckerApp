package com.pyunku.dailychecker.calendar.presentation

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pyunku.dailychecker.MainScreen
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.ui.theme.DailyCheckerTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    //var interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(this, "", adRequest, object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    //interstitialAd = null
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    //interstitialAd?.show(this@MainActivity)
                }
            })

            DailyCheckerTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
        // TODO insert admob to bottombar
//        AndroidView(factory = {
//            AdView(it).apply{
//                setAdSize(AdSize.BANNER)
//                //adUnitId = "ca-app-pub-7719172067804321/1743343072"
//                //Test
//                adUnitId = "ca-app-pub-3940256099942544/6300978111"
//                loadAd(AdRequest.Builder().build())
//            }
//        })

        // AdMob
        MobileAds.initialize(this)
    }

    private fun playClickSound(context: Context){
        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.click_sound)
        mp.start()
    }
}
