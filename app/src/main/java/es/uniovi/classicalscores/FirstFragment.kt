package es.uniovi.classicalscores

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
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
        val bundle = Bundle().apply {
            putString(PIEZA, Gson().toJson(it))
        }
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
                    listaPiezas = result.piezas.toList()
                    if(binding.ETSearch.text.toString() != ""){
                        search(binding.ETSearch.text.toString())
                    }
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
            if(binding.ETSearch.text.toString() != ""){
                search(binding.ETSearch.text.toString())
            }
            else scoresListAdapter.submitList(listaPiezas)
        }
        binding.buttonFavs.setOnClickListener {
            val file = File(activity?.filesDir,  Telephony.Mms.Part.FILENAME)
            if(!file.exists()) {
                scoresListAdapter.submitList(listaPiezas)
            }
            else {
                var lista = file.readText().split(",")
                val lista_aux = mutableListOf<Pieza>()
                listaPiezas.forEach {
                    if(lista.contains(it.Title)){
                        lista_aux.add(it)
                    }
                }
                scoresListAdapter.submitList(lista_aux)
            }
        }
        return binding.root

    }

    fun search(cadena: String) {
        var list = mutableListOf<Pieza>()
        listaPiezas.forEach{
            if (it.Title.contains(cadena, ignoreCase = true) or
                it.Modulation.contains(cadena, ignoreCase = true) or
                it.CatNo.contains(cadena, ignoreCase = true) or
                it.Composer.contains(cadena, ignoreCase = true) or
                it.Date.contains(cadena, ignoreCase = true) or
                it.Genre.contains(cadena, ignoreCase = true) or
                it.Inst1.contains(cadena, ignoreCase = true) or
                it.Inst2.contains(cadena, ignoreCase = true) or
                it.Key.contains(cadena, ignoreCase = true) or
                it.Species.contains(cadena, ignoreCase = true)) {
                list.add(it)
            }
            if(!it.Range.equals("")){
                if((it.Range.split("-").get(0).toInt() <= cadena.toInt()) and (it.Range.split("-").get(1).toInt() >= cadena.toInt())){
                    list.add(it)
                }
            }
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