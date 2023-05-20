package com.mussayev.sudoku.ui.activity.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewManagerFactory
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.manager.SoundManager
import com.mussayev.sudoku.databinding.ActivityMainBinding
import com.mussayev.sudoku.theme.provider.ThemeProvider
import com.mussayev.sudoku.ui.dialog.RateAppDialog
import com.mussayev.sudoku.ui.dialog.RateAppDialogListener
import com.mussayev.sudoku.ui.viewmodel.ThemeViewModel
import com.mussayev.sudoku.utils.UIUtils
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
RateAppDialogListener {
    companion object {
        private const val UPDATE_APP_REQUEST_CODE = 1020
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appUpdateManager: AppUpdateManager
    private val viewModel: MainViewModel by viewModel()
    private val soundManager: SoundManager by inject()
    private val themeViewModel: ThemeViewModel by viewModel()
    private lateinit var themeProvider: ThemeProvider

    val uiUtils: UIUtils by lazy { UIUtils(window) }

    override fun onCreate(savedInstanceState: Bundle?) {

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        themeViewModel.createThemeProvider(this)
        themeProvider = themeViewModel.getThemeProvider()

        this.setTheme(themeProvider.getTheme())
        windowInsetsController.isAppearanceLightStatusBars = !themeProvider.isDarkTheme()
        window.statusBarColor = ContextCompat.getColor(this, themeProvider.getStatusBarColor())
        window.decorView.rootView.setBackgroundResource(themeProvider.getBackgroundColor())

        this.setTheme(themeProvider.getTheme())

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }

    private fun init() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdate()
        requestReview()

        soundManager.load()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun checkUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(updateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_APP_REQUEST_CODE)
            }
        }
    }

    private fun requestReview() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // Диалоговое окно с запросом на обзор
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // Обработка завершения процесса обзора
                }
            } else {
                // Обработка ошибки при запросе на обзор
            }
        }

//        viewModel.incrementLaunches()
//
//        val launchCount = viewModel.getNumLaunches()
//        if (launchCount >= 10 && launchCount < 100) {
//            val dialog = RateAppDialog()
//            dialog.isCancelable = false
//            dialog.setListener(this)
//            dialog.show(supportFragmentManager, "dialog_rate_app")
//        }
    }

    private fun inProgressUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(updateInfo, AppUpdateType.IMMEDIATE, this, UPDATE_APP_REQUEST_CODE)
            }
        }
    }

    override fun rateAppDialogOnClickOk() {
        viewModel.setNumLaunches(100)
        val packageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    override fun rateAppDialogOnClickCancel() {
        viewModel.setNumLaunches(0)
    }
}