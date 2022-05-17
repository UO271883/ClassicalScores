package es.uniovi.classicalscores.ui

import android.graphics.Bitmap
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.uniovi.classicalscores.R
import es.uniovi.classicalscores.databinding.PieceListItemBinding
import es.uniovi.classicalscores.domain.ScoresListViewModel
import es.uniovi.classicalscores.model.Pieza

class PiezasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemBinding = PieceListItemBinding.bind(itemView)
    val picasso = Picasso.get()
    fun bind(pieza: Pieza) {
        itemBinding.TvTitle.text = pieza.Title
        itemBinding.TvComposer.text = pieza.Composer
        picasso.load("http://orion.atc.uniovi.es/~arias/json/Compositores/" + pieza.ComposerPic)
            .into(itemBinding.imageView)
    }
}