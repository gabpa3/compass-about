package com.gabcode.compassabout.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabcode.compassabout.R
import com.gabcode.compassabout.databinding.FragmentAboutListBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass to represent the list of items based on About content.
 * Use the [AboutListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutListFragment : Fragment() {

    private var _binding: FragmentAboutListBinding? = null
    private val binding get() = _binding!!


    private var wordOccurrencesAdapter: WordOccurrencesAdapter? = null
    private var tenthCharactersAdapter: TenthCharactersAdapter? = null

    private val sharedViewModel by activityViewModels<AboutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()
    }

    private fun setupView() {
        with(binding) {
            setupRecyclerView(aboutTenthCharactersRecycler, LinearLayoutManager.HORIZONTAL)
            setupRecyclerView(aboutWordOccurrencesRecycler, LinearLayoutManager.VERTICAL)

            aboutStartFab.setOnClickListener {
                sharedViewModel.startProcessingData()
            }
        }
        initViewStates()
    }

    private fun initViewStates() {
        with(binding) {
            aboutTenthCharactersGroup.visibility = View.GONE
            aboutWordOccurrencesGroup.visibility = View.GONE
            aboutProgressGroup.visibility = View.VISIBLE
            aboutStartFab.isEnabled = false
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, orientation: Int) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@AboutListFragment.context,
                orientation,
                false)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            sharedViewModel.wordOccurrences.collect { state -> handleStateForWordOccurrences(state) }
        }

        lifecycleScope.launch {
            sharedViewModel.tenthCharacters.collect { data -> handleStateForTenthCharacters(data) }
        }
    }

    private fun handleStateForWordOccurrences(state: UIState<List<WordOccurrenceItem>>) {
        when (state) {
            is UIState.Loading -> initViewStates()
            is UIState.Success -> {
                val adapter = WordOccurrencesAdapter(state.data)
                binding.aboutWordOccurrencesRecycler.adapter = adapter

                binding.aboutProgressGroup.visibility = View.GONE
                binding.aboutWordOccurrencesGroup.visibility = View.VISIBLE
            }
            is UIState.Error -> showError(state.throwable.message)
            is UIState.Idle -> {}
        }
    }

    private fun handleStateForTenthCharacters(state: UIState<List<TenthCharacterItem>>) {
        when (state) {
            is UIState.Success -> {
                val adapter = TenthCharactersAdapter(state.data)
                binding.aboutTenthCharactersRecycler.adapter = adapter

                binding.aboutTenthCharactersGroup.visibility = View.VISIBLE
                binding.aboutStartFab.isEnabled = true
            }
            is UIState.Error -> showError(state.throwable.message)
            is UIState.Loading, is UIState.Idle -> {}
        }
    }

    private fun showError(message: String?) {
        val errorMessage = message ?: getString(R.string.error_message)
        Toast.makeText(this.context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordOccurrencesAdapter = null
        tenthCharactersAdapter = null
        _binding = null
    }

    companion object {

        fun newInstance() = AboutListFragment()
    }
}
