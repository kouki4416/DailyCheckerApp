package com.pyunku.dailychecker.calendar.presentation

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
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.pyunku.dailychecker.ui.theme.DailyCheckerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private val calendarViewModel: CalendarViewModel by viewModel()
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            AndroidView(factory = {
                                AdView(it).apply{
                                    setAdSize(AdSize.BANNER)
                                    //adUnitId = "ca-app-pub-7719172067804321/1743343072"
                                    //Test
                                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                                    loadAd(AdRequest.Builder().build())
                                }
                            })
                        }, content = {
                            CalendarScreen(
                                calendarViewModel.state.collectAsState(),
                                onCheckDate = { date ->
                                    calendarViewModel.addCheckedDate(date)
                                    calendarViewModel.playClickSound(this)
                                },
                                onUncheckDate = { date ->
                                    calendarViewModel.deleteCheckedDate(date)
                                }
                            )
                        }
                    )
                }
            }
        }

        // AdMob
        MobileAds.initialize(this)
    }
}
