package com.mussayev.sudoku.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mussayev.sudoku.data.model.Cell
import java.util.concurrent.TimeUnit

class StringUtils {

    fun boardToString(board: Array<IntArray>): String {
        val gson = Gson()
        return gson.toJson(board)
    }

    fun stringToBoard(boardString: String): Array<IntArray> {
        val gson = Gson()
        val type = object : TypeToken<Array<IntArray>>() {}.type
        return gson.fromJson(boardString, type)
    }

    fun boardCellsToString(board: List<Cell>): String {
        val gson = Gson()
        return gson.toJson(board)
    }

    fun stringToBoardCells(boardString: String): MutableList<Cell> {
        val gson = Gson()
        val type = object : TypeToken<MutableList<Cell>>() {}.type
        return gson.fromJson(boardString, type)
    }

    companion object {
        fun link(context: Context, link: String, title: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(link)
            val chooser = Intent.createChooser(intent, title)
            context.startActivity(chooser)
        }

        fun millisToTime(timeMillis: Long): String {
            val hours = TimeUnit.MILLISECONDS.toHours(timeMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMillis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis) % 60

            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        fun secondsToTime(seconds: Long): String {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60

            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, secs)
            } else {
                String.format("%d:%02d:%02d", hours, minutes, secs)
                //String.format("%02d:%02d", minutes, secs)
            }
        }
    }
}