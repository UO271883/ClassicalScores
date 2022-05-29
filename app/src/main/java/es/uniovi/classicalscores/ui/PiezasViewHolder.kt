package es.uniovi.classicalscores.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import es.uniovi.classicalscores.R
import es.uniovi.classicalscores.databinding.PieceListItemBinding
import es.uniovi.classicalscores.domain.ScoresListViewModel
import es.uniovi.classicalscores.model.Pieza

class PiezasViewHolder(itemView: View, private val onItemClicked: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

    val itemBinding = PieceListItemBinding.bind(itemView)
    val picasso = Picasso.get()
    init {
        itemView.setOnClickListener{
            // Se obtiene la posicion del adaptador sobre la que se ha pulsado
            onItemClicked(adapterPosition)
        }
    }
    fun bind(pieza: Pieza) {
        with(itemBinding){
            TvTitle.text = pieza.Title
            TvComposer.text = pieza.Composer
            // Se carga una imagen en un ImageView haciendo uso de la libreria Picasso
            picasso.load("http://orion.atc.uniovi.es/~arias/json/Compositores/" + pieza.ComposerPic)
                .into(imageView)
            // Se crean los manejadores de los botones que abriran la partitura en PDF o un video
            viewScoreButton.setOnClickListener {
                val value = "http://orion.atc.uniovi.es/~arias/json/Partituras/" + pieza.Score
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                it.context.startActivity(intent)
            }
            buttonPlayVideo2.setOnClickListener{
                val value = pieza.Video
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                it.context.startActivity(intent)
            }
        }
    }
}