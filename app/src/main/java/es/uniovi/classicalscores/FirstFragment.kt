package es.uniovi.classicalscores

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.uniovi.classicalscores.databinding.FragmentFirstBinding
import es.uniovi.classicalscores.domain.ScoresListViewModel
import es.uniovi.classicalscores.ui.PiezasListAdapter
import es.uniovi.classicalscores.ui.ScoresUIState

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val scoresListAdapter : PiezasListAdapter = PiezasListAdapter()
    private val scoresListViewModel : ScoresListViewModel = ScoresListViewModel()
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
                    scoresListAdapter.submitList(result.piezas.toList())
                }
                is ScoresUIState.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
                is ScoresUIState.Loading -> {

                }
            }
        }
        binding.recyclerView.adapter = scoresListAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}