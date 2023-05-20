package com.mussayev.sudoku.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.mussayev.sudoku.R
import com.mussayev.sudoku.data.model.Theme
import com.mussayev.sudoku.ui.custom.SquareButton

class ThemeAdapter(
    private val themes: List<Theme>,
    private val onThemeSelected: (Theme) -> Unit
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return ThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]
        holder.bind(theme, onThemeSelected)
    }

    override fun getItemCount(): Int = themes.size

    class ThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val themeButton: SquareButton = itemView.findViewById(R.id.themeButton)

        fun bind(theme: Theme, onThemeSelected: (Theme) -> Unit) {

            when (theme) {
                Theme.LIGHT -> {
                    themeButton.setBackgroundResource(R.drawable.icon_theme_light)
                }
                Theme.DARK_GREY -> {
                    themeButton.setBackgroundResource(R.drawable.icon_theme_dark_grey)
                }
                Theme.DARK -> {
                    themeButton.setBackgroundResource(R.drawable.icon_theme_dark)
                }
                Theme.GREEN -> {
                    themeButton.setBackgroundResource(R.drawable.icon_theme_green)
                }
                Theme.LEMONADE -> {
                    themeButton.setBackgroundResource(R.drawable.icon_theme_lemonade)
                }
            }

            themeButton.setOnClickListener { onThemeSelected(theme) }
        }
    }
}
