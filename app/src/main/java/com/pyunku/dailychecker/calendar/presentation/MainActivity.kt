package com.pyunku.dailychecker.calendar.presentation

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.android.gms.ads.MobileAds
import com.pyunku.dailychecker.MainScreen
import com.pyunku.dailychecker.R
import com.pyunku.dailychecker.data.UserPreferences
import com.pyunku.dailychecker.data.UserPreferencesRepository
import com.pyunku.dailychecker.ui.theme.DailyCheckerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferencesState: StateFlow<UserPreferences> =
            userPreferencesRepository.userDataStream.stateIn(
                scope = lifecycleScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UserPreferences()
            )
        setContent {
            DailyCheckerTheme(
                userPreferencesStateFlow = userPreferencesState
            ) {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }

        // AdMob
        MobileAds.initialize(this)
    }

    private fun playClickSound(context: Context) {
        val mp: MediaPlayer = MediaPlayer.create(context, R.raw.click_sound)
        mp.start()
    }
}
