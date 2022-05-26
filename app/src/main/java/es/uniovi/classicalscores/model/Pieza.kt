package es.uniovi.classicalscores.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

data class Pieza(
    val Title : String,
    val Composer : String,
    val ComposerPic : String,
    val CatNo : String,
    val Date : String,
    val Range : String,
    val Inst1 : String,
    val Inst2 : String,
    val Inst3 : String,
    val Genre : String,
    val Subgenre : String,
    val Species : String,
    val Species2 : String,
    val Nationality : String,
    val Key : String,
    val Modulation : String,
    val Folder : String,
    val Filename : String,
    val Score : String
) {

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Pieza>() {
        override fun areItemsTheSame(oldItem: Pieza, newItem: Pieza): Boolean {
            return oldItem.Title == newItem.Title
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Pieza, newItem: Pieza): Boolean {
            return oldItem ==  newItem
        }
    }
}