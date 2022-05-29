package es.uniovi.classicalscores

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import es.uniovi.classicalscores.R
import es.uniovi.classicalscores.databinding.FragmentFirstBinding
import es.uniovi.classicalscores.domain.ScoresListViewModel
import es.uniovi.classicalscores.model.Pieza
import es.uniovi.classicalscores.ui.PiezasListAdapter
import es.uniovi.classicalscores.ui.ScoresUIState
import java.io.File

const val PIEZA = "pieza"
const val PREFERENCES = "preferencias"
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private lateinit var listaPiezas: List<Pieza>
    private val scoresListAdapter : PiezasListAdapter = PiezasListAdapter {
        // Se introduce la pieza convertida a JSON en el bundle para pasarla al fragmento de detalles
        val bundle = Bundle().apply {
            putString(PIEZA, Gson().toJson(it))
        }
        // Se navega al fragmento de detalles y se pasa el bundle con la pieza
        findNavController().navigate(R.id.action_listFragment_to_detailsFragment, bundle)
    }
    private val scoresListViewModel : ScoresListViewModel = ScoresListViewModel()
    val intent = Intent(Intent.ACTION_VIEW)
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        scoresListViewModel.scoresUIStateObservable.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ScoresUIState.Success -> {
                    // Se obtiene la lista de piezas
                    listaPiezas = result.piezas.toList()
                    // Si habia algo en el campo de busqueda y se vuelve al fragmento queremos que
                    // se sigan mostrando solo las piezas que cumplan el criterio
                    if(binding.ETSearch.text.toString() != ""){
                        search(binding.ETSearch.text.toString())
                    }
                    // Si esta vacio se muestran todas
                    else scoresListAdapter.submitList(listaPiezas)
                }
                is ScoresUIState.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
        binding.recyclerView.adapter = scoresListAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.buttonSearch.setOnClickListener {
            // Manejador del boton de busqueda, misma funcionalidad que arriba
            if(binding.ETSearch.text.toString() != ""){
                search(binding.ETSearch.text.toString())
            }
            else scoresListAdapter.submitList(listaPiezas)
        }
        // Manejador del boton de favoritos
        binding.buttonFavs.setOnClickListener {
            // Se obtiene el fichero y se crea si no existe
            val file = File(activity?.filesDir,  FILENAME)
            if(!file.exists()) {
                file.createNewFile()
            }
            // Se lee el fichero obteniendo una lista con los titulos de los favoritos
            var lista = file.readText().split(",")
            val lista_aux = mutableListOf<Pieza>()
            // Se itera sobre la lista de todas las piezas
            listaPiezas.forEach {
                // Si uno de los titulos se encuentra en la lista de favoritos se añade a una lista auxiliar
                if(lista.contains(it.Title)){
                    lista_aux.add(it)
                }
            }
            // Se carga el adaptador con la lista auxiliar en la que estan los favoritos
            scoresListAdapter.submitList(lista_aux.toList())

        }
        return binding.root

    }

    // Esta funcion aplica el criterio de busqueda
    fun search(cadena: String) {
        var list = mutableListOf<Pieza>()
        // Si lo escrito en el cuadro de busqueda se encuentra en alguno de los campos de la pieza
        // se añade a una lista auxiliar
        listaPiezas.forEach{
            if (it.Title.contains(cadena, ignoreCase = true) or
                it.Composer.contains(cadena, ignoreCase = true) or
                it.CatNo.contains(cadena, ignoreCase = true) or
                it.Date.contains(cadena, ignoreCase = true) or
                it.Inst1.contains(cadena, ignoreCase = true) or
                it.Inst2.contains(cadena, ignoreCase = true) or
                it.Inst3.contains(cadena, ignoreCase = true) or
                it.Genre.contains(cadena, ignoreCase = true) or
                it.Subgenre.contains(cadena, ignoreCase = true) or
                it.Species.contains(cadena, ignoreCase = true) or
                it.Species2.contains(cadena, ignoreCase = true) or
                it.Nationality.contains(cadena, ignoreCase = true) or
                it.Key.contains(cadena, ignoreCase = true) or
                it.Modulation.contains(cadena, ignoreCase = true)) {
                list.add(it)
            }
            // Si es un numero (un año) se comprueba si esta entre los numeros del campo range
            if(!it.Range.equals("") and cadena.isDigitsOnly()){
                if((it.Range.split("-").get(0).toInt() <= cadena.toInt()) and
                   (it.Range.split("-").get(1).toInt() >= cadena.toInt())){
                    list.add(it)
                }
            }
            // Se carga el adaptador con la lista auxiliar en la que estan los que cumplen el criterio
            scoresListAdapter.submitList(list.toList())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}