package es.uniovi.classicalscores.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import es.uniovi.classicalscores.R
import es.uniovi.classicalscores.model.Pieza

class PiezasListAdapter(private val onNameSelected: (Pieza) -> Unit): ListAdapter<Pieza, PiezasViewHolder>(Pieza.DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PiezasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.piece_list_item, parent, false )
        return PiezasViewHolder(view) {
            onNameSelected(getItem(it))
        }
    }

    override fun onBindViewHolder(holder: PiezasViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}