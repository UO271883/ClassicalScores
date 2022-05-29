package es.uniovi.classicalscores

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings.PluginState
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.uniovi.classicalscores.R
import es.uniovi.classicalscores.databinding.FragmentDetailsBinding
import es.uniovi.classicalscores.model.Pieza
import java.io.File


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val FAVS = "favoritos"
const val DEFAULT_FAVS = ""
/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private var pieza: Pieza? = null
    val picasso = Picasso.get()
    var lista_favoritos = ""
    val folder = activity?.filesDir

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pieza = Gson().fromJson(it.getString(PIEZA), Pieza::class.java)
        }
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        with(binding) {
            picasso.load("http://orion.atc.uniovi.es/~arias/json/Compositores/" + pieza?.ComposerPic)
                .into(IVCompositor)
            TVTitulo.text = pieza?.Title
            TVCompositor.text = pieza?.Composer
            TVGenre.text = pieza?.Genre
            if(pieza?.Inst2 != "") {
                TVInstr.text = "${pieza?.Inst1}, ${pieza?.Inst2}"
            } else TVInstr.text = pieza?.Inst1
            TVKey.text = pieza?.Key
            if (!pieza?.Date.equals("")) {
                TVYear.text = pieza?.Date
            } else TVYear.text = pieza?.Range
            if(pieza?.Modulation != ""){
                TVMod.visibility = TextView.VISIBLE
                TVMod2.visibility = TextView.VISIBLE
                TVMod.text = pieza?.Modulation
            }
            if(pieza?.Species != ""){
                TVSpecie.visibility = TextView.VISIBLE
                TVSpecie2.visibility = TextView.VISIBLE
                TVSpecie.text = pieza?.Species
            }
            TVPais.text = pieza?.Nationality
            buttonViewScore.setOnClickListener {
                val value = "http://orion.atc.uniovi.es/~arias/json/Partituras/" + pieza?.Score
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                it.context.startActivity(intent)
            }

            val file = File(activity?.filesDir,  FILENAME)
            if(!file.exists()) {
                file.createNewFile()
                System.out.println(file.exists())
            }
            if(file.readText().split(",").contains(TVTitulo.text)){
                buttonAddFav.setImageResource(android.R.drawable.btn_star_big_off)
            }
            buttonAddFav.setOnClickListener{
                System.out.println(file.path + " " + file.absolutePath + " " + file.exists())
                lista_favoritos = file.readText()
                val mutableList: MutableList<String> = lista_favoritos.split(",").toMutableList()
                if(lista_favoritos.split(",").contains(TVTitulo.text)){
                    mutableList.removeAt(lista_favoritos.split(",").indexOf(TVTitulo.text))
                    lista_favoritos = ""
                    mutableList.forEach {
                        lista_favoritos+=",$it"
                    }
                    file.writeText(lista_favoritos.drop(1))
                    buttonAddFav.setImageResource(android.R.drawable.ic_input_add)
                    Snackbar.make(binding.root, R.string.eliminadoFav, Snackbar.LENGTH_LONG).show()
                }
                else {
                    lista_favoritos += ",${TVTitulo.text}"
                    file.writeText(lista_favoritos)
                    buttonAddFav.setImageResource(android.R.drawable.btn_star_big_off)
                    Snackbar.make(binding.root, R.string.añadidoFav, Snackbar.LENGTH_LONG).show()
                }
            }
            buttonPlayVideo.setOnClickListener{
                val value = pieza?.Video
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                it.context.startActivity(intent)
            }
        }
        return binding.root
    }
}