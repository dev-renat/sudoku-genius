package com.mussayev.sudoku.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.manager.AdManagerImpl
import com.mussayev.sudoku.data.manager.MusicManager
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.data.manager.VibrationManager
import com.mussayev.sudoku.data.remote.FirebaseRemote
import com.mussayev.sudoku.data.repository.*
import com.mussayev.sudoku.domain.manager.AdManager
import com.mussayev.sudoku.ui.activity.main.MainViewModel
import com.mussayev.sudoku.ui.fragment.account.AccountViewModel
import com.mussayev.sudoku.ui.fragment.endgame.EndgameViewModel
import com.mussayev.sudoku.ui.fragment.game.GameViewModel
import com.mussayev.sudoku.ui.fragment.home.HomeViewModel
import com.mussayev.sudoku.ui.fragment.leaderboard.LeaderboardViewModel
import com.mussayev.sudoku.ui.fragment.settings.SettingsViewModel
import com.mussayev.sudoku.ui.viewmodel.AuthViewModel
import com.mussayev.sudoku.ui.viewmodel.DifficultyViewModel
import com.mussayev.sudoku.ui.viewmodel.ThemeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

//    single {
//        //AppDatabase.getInstance(androidContext())
//        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_db").build()
//    }
//    single { get<AppDatabase>().getSudokuDao() }

    single { FirebaseRemote() }
    single { PreferencesManager(get()) }
    single { SettingsRepository(get()) }
    single { DifficultyRepository(get()) }
    single { ThemeRepository(get()) }
    single { GameRepository(get(),get(),get(), get(), get(), get()) }
    single { GeneratorRepository() }
    single { StorageGameRepository(get(), get()) }
    single { SoundManager(get(), get()) }
    single { MusicManager(get(), get()) }
    single { VibrationManager(get(), get()) }

    //
    single { FirebaseAuth.getInstance() }
    single { provideGoogleSignInClient(androidContext()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    //
    single<AdManager> { AdManagerImpl(get()) }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { SettingsViewModel(get(), get(), get()) }
    viewModel { GameViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { EndgameViewModel(get()) }
    viewModel { LeaderboardViewModel(get()) }
    viewModel { ThemeViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { DifficultyViewModel(get()) }
}

fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}