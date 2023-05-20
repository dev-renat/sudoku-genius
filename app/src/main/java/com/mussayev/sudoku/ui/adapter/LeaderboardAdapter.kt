package com.mussayev.sudoku.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.UserScore
import com.mussayev.sudoku.databinding.ItemLeaderboardBinding

class LeaderboardAdapter(
    private val context: Context,
    private val users: List<UserScore>,
    private val callback: Callback
) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemLeaderboardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLeaderboardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        val user = users[position]

        val number = position + 1

        view.displayName.text = user.username
        view.ratingTextView.text = user.rating.toString()

        view.number.text = number.toString()
        /*view.displayName.text = user.name
        view.level.text = context.getString(R.string.user_level, user.level)

        when (number) {
            1 -> {
                //view.llAvatar.setBackgroundResource(R.drawable.round_background_avatar_top_1)
            }
            2 -> {
                //view.llAvatar.setBackgroundResource(R.drawable.round_background_avatar_top_2)
            }
            3 -> {
                //view.llAvatar.setBackgroundResource(R.drawable.round_background_avatar_top_3)
            }
            else -> {
                //view.llAvatar.setBackgroundResource(R.drawable.round_background_avatar)
            }
        }*/

        user.photo.let {
            Glide.with(context)
                .load(user.photo)
                .placeholder(R.drawable.icon_account_circle) // Здесь можно указать изображение-заглушку, которое будет отображаться до загрузки фотографии пользователя
                .error(R.drawable.icon_account_outline) // Здесь можно указать изображение, которое будет отображаться в случае ошибки загрузки
                .circleCrop()
                .into(view.photo)
        }

        callback.onItemClicked(user)
    }

    override fun getItemCount(): Int = users.size

    interface Callback {
        fun onItemClicked(item: UserScore)
    }
}