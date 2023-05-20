package com.mussayev.sudoku.ui.fragment.home

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.appbar.MaterialToolbar
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.Difficulty
import com.mussayev.sudoku.data.model.Theme
import com.mussayev.sudoku.databinding.FragmentHomeBinding
import com.mussayev.sudoku.ui.adapter.ThemeAdapter
import com.mussayev.sudoku.ui.dialog.*
import com.mussayev.sudoku.ui.viewmodel.AuthViewModel
import com.mussayev.sudoku.ui.viewmodel.AuthenticationState
import com.mussayev.sudoku.ui.viewmodel.DifficultyViewModel
import com.mussayev.sudoku.ui.viewmodel.ThemeViewModel
import com.mussayev.sudoku.utils.DifficultyUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(),
    GameRestartConfirmationDialogListener,
    AuthDialogListener,
    AboutDialogListener,
    ConfirmExitDialogListener
{
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: HomeViewModel by viewModel()
    private val themeViewModel: ThemeViewModel by viewModel()
    private val difficultyViewModel: DifficultyViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()

    private var gameRestartConfirmation = false
    private var isAuth = false
    private var isDialogShowing = false

    private lateinit var themeAdapter: ThemeAdapter

    // Регистрация обработчика результата для Google SignIn
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    authViewModel.signInWithGoogle(account)
                }
            } catch (e: ApiException) {
                // Обработка ошибок
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        navController = findNavController()

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        toolbar = binding.toolbar
        toolbar.title = null
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
//                R.id.action_leaderboard -> {
//                    navController.navigate(R.id.action_homeFragment_to_leaderboardFragment)
//                    true
//                }
                R.id.action_theme -> {
                    if (binding.themeContainer.visibility == View.GONE) {
                        showThemeContainer()
                    } else {
                        hideThemeContainer()
                    }
                    true
                }
                R.id.action_account -> {
                    navController.navigate(R.id.action_homeFragment_to_accountFragment)
//                    if (isAuth) {
//                    } else {
//                        val signInIntent = authViewModel.getGoogleSignInIntent()
//                        googleSignInLauncher.launch(signInIntent)
//                    }
                    true
                }
                R.id.action_guide -> {
                    navController.navigate(R.id.action_homeFragment_to_guideFragment)
                    true
                }
                else -> false
            }
        }

        return view
    }

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Здесь вы можете добавить свой код для обработки нажатия кнопки "Назад"
                if (binding.themeContainer.visibility == View.VISIBLE) {
                    hideThemeContainer()
                } else {
                    if (!isDialogShowing) {
                        isDialogShowing = true

                        val dialog = ConfirmExitDialog(
                            getString(R.string.exit),
                            getString(R.string.confirm_exit),
                            close = false
                        )
                        dialog.isCancelable = false
                        dialog.setListener(this@HomeFragment)
                        dialog.show(requireActivity().supportFragmentManager, "dialog_confirm_exit")
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        init()
    }

    private fun init() {
        difficultyViewModel.init()

        // Вызов метода loadGame() из экземпляра viewModel для загрузки сохраненной игры.
        viewModel.loadGame()

        binding.continueButton.visibility = View.GONE

        //
        val actuality = resources.getStringArray(R.array.actuality)
        val actualityId = (actuality.indices).random()
        val text = actuality[actualityId]
        binding.actualityTextView.text = text

        /*
        * Установка текста для difficultyTextView с помощью функции getName() из класса DifficultyUtils,
        * которая определяет уровень сложности игры на основе значения, полученного из viewModel.
        * */
        difficultyViewModel.difficultyLiveData.observe(viewLifecycleOwner) {
            binding.difficultyTextView.text = DifficultyUtils().getName(requireContext(), it)
        }

        // При нажатии на кнопку, уровень сложности игры уменьшается с помощью метода decrementDifficulty() из viewModel. Здесь также создается анимация slide_right
        binding.decrementDifficultyButton.setOnClickListener {
            difficultyViewModel.decrementDifficulty()
            // Загрузка сохраненной игры
            viewModel.loadGame()
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_right)
            binding.difficultyTextView.startAnimation(animation)
        }

        // При нажатии на кнопку, уровень сложности игры увеличивается с помощью метода incrementDifficulty() из viewModel. Здесь также создается анимация slide_left
        binding.incrementDifficultyButton.setOnClickListener {
            difficultyViewModel.incrementDifficulty()
            // Загрузка сохраненной игры
            viewModel.loadGame()
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left)
            binding.difficultyTextView.startAnimation(animation)
        }

        //
        binding.newGameButton.setOnClickListener {
            if (gameRestartConfirmation && !isDialogShowing) {
                isDialogShowing = true
                val dialog = GameRestartConfirmationDialog().apply {
                    isCancelable = false
                    setListener(this@HomeFragment)
                }
                dialog.show(requireActivity().supportFragmentManager, "dialog_auth")
            } else {
                navController.navigate(R.id.action_homeFragment_to_gameFragment)
            }
        }

        // Наблюдаем за authenticationState и обрабатываем изменения
        authViewModel.authenticationState.observe(requireActivity()) { state ->
            isAuth = false
            when (state) {
                is AuthenticationState.Authenticated -> {
                    // Пользователь успешно вошел, обновите интерфейс
                    println("USER-> ${authViewModel.getCurrentUser()?.displayName }")
                    authViewModel.getCurrentUser()?.let {
                        binding.signInButton.visibility = View.GONE
                        isAuth = true
                    }
                }
                is AuthenticationState.Unauthenticated -> {
                    // Ошибка входа или пользователь вышел из системы, обновите интерфейс
                    println("USER-> Ошибка входа или пользователь вышел из системы, обновите интерфейс")
                    binding.signInButton.text = "Google Sign In"
                }
                is AuthenticationState.Failed -> {
                    // Обработка ошибок, связанных с аутентификацией
                    val error = state.error
                    println("USER-> Обработка ошибок, связанных с аутентификацией")
                }
            }
        }

        //
        binding.signInButton.setOnClickListener {
            val signInIntent = authViewModel.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }

        /*
        * Добавление слушателя наблюдения за свойством hasSavedGameLiveData в viewModel,
        * который вызывается при изменении значения свойства.
        * Когда значение изменяется, устанавливается видимость кнопки continueButton
        * в зависимости от наличия сохраненной игры. Если сохраненная игра есть,
        * кнопка становится видимой, иначе - невидимой.
        * */
        viewModel.hasSavedGameLiveData.observe(viewLifecycleOwner) { hasSavedGame ->
            binding.signInButton.visibility = View.GONE
            if (hasSavedGame) {
                gameRestartConfirmation = true
                binding.continueButton.visibility = View.VISIBLE
                binding.continueButton.setOnClickListener {
                    val bundle = Bundle().apply { putBoolean("load_game", true) }
                    navController.navigate(R.id.action_homeFragment_to_gameFragment, bundle)
                }
            } else {
                gameRestartConfirmation = false
                binding.continueButton.visibility = View.GONE
            }
        }

        binding.settings.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        binding.about.setOnClickListener {
            if (!isDialogShowing) {
                isDialogShowing = true
                val dialog = AboutDialog()
                dialog.isCancelable = false
                dialog.setListener(this)
                dialog.show(requireActivity().supportFragmentManager, "dialog_about")
            }
        }

        setupThemeButton()
        setupThemeRecyclerView()
    }

    private fun setupThemeButton() {
        binding.themeButton.setOnClickListener {
            if (binding.themeContainer.visibility == View.GONE) {
                showThemeContainer()
            } else {
                hideThemeContainer()
            }
        }
    }

    private fun showThemeContainer() {
        binding.themeContainer.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right)
        binding.themeContainer.startAnimation(animation)
    }

    private fun hideThemeContainer() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_to_right)
        binding.themeContainer.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                binding.themeContainer.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun setupThemeRecyclerView() {
        val themes = listOf(
            Theme.LIGHT,
            Theme.DARK_GREY,
            Theme.DARK,
            Theme.GREEN,
            Theme.LEMONADE
        ).filterNot {
            it.name == themeViewModel.currentThemeName()
        }

        themeAdapter = ThemeAdapter(themes) { selectedTheme ->
            themeViewModel.saveTheme(selectedTheme)
            requireActivity().recreate()
        }

        binding.themeRecyclerView.adapter = themeAdapter
    }

    override fun confirmNewGameDialogOnClickOk() {
        isDialogShowing = false
        navController.navigate(R.id.action_homeFragment_to_gameFragment)
    }

    override fun confirmNewGameDialogOnClickCancel() {
        isDialogShowing = false
    }

    override fun aboutDialogOnClickCancel() {
        isDialogShowing = false
    }

    override fun authDialogOnClickCancel() {
        isDialogShowing = false
    }

    override fun confirmExitDialogOnClickOk() {
        isDialogShowing = false
        requireActivity().finish()
    }

    override fun confirmExitDialogOnClickCancel() {
        isDialogShowing = false
    }
}