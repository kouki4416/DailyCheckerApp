package com.pyunku.dailychecker.calendar.presentation

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.navigation.SetupNavGraph
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
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
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
