package com.mussayev.sudoku.ui.fragment.leaderboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.UserScore
import com.mussayev.sudoku.databinding.FragmentLeaderboardBinding
import com.mussayev.sudoku.ui.adapter.LeaderboardAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeaderboardFragment : Fragment() {
    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var navController: NavController
    private lateinit var toolbar: MaterialToolbar
    private val viewModel: LeaderboardViewModel by viewModel()

    override fun onDestroyView() {
        view?.animate()?.alpha(0f)?.setDuration(500)?.start()
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater)
        navController = findNavController()

        val view = binding.root

        view.alpha = 0f
        view.animate().alpha(1f).setDuration(500).start()

        toolbar = binding.toolbar
        toolbar.title = getString(R.string.leaderboard)
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
//        toolbar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.action_logout -> {
//                    viewModel.logout()
//                    val navOption = NavOptions.Builder().setPopUpTo(R.id.accountFragment, true).build()
//                    navController.navigate(R.id.action_accountFragment_to_homeFragment, null, navOption)
//                    true
//                }
//                else -> false
//            }
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        viewModel.getLeaderboard().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    binding.loading.visibility = View.GONE
                    binding.llLeaderboard.visibility = View.VISIBLE

                    val users = mutableListOf<UserScore>()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(UserScore::class.java)
                        user?.let { users.add(it) }
                        println("Leader ${user?.username}")
                    }
                    // сортируем пользователей по убыванию рейтинга
                    users.sortByDescending { it.rating }

                    val adapterRating = LeaderboardAdapter(
                        requireActivity(),
                        users,
                        object : LeaderboardAdapter.Callback {
                            override fun onItemClicked(item: UserScore) {

                            }
                        })
                    binding.leaderboard.apply {
                        layoutManager = GridLayoutManager(requireContext(), 1)
                        adapter = adapterRating
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибки
            }
        })
    }
}