package com.gabcode.compassabout.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabcode.compassabout.R
import com.gabcode.compassabout.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        val factory = AboutViewModelFactory()
        ViewModelProvider(this, factory)[AboutViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.fetchContent()

        setupView()
        setupObservers()
    }

    private fun setupView() {
        with(binding) {
            setupRecyclerView(tenthCharactersRecycler, LinearLayoutManager.HORIZONTAL)
            setupRecyclerView(wordOccurrencesRecycler, LinearLayoutManager.VERTICAL)

            startButton.setOnClickListener {
                viewModel.startProcessingData()
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, orientation: Int) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                orientation,
                false)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.wordOccurrences.collect { state ->
                handleStateForWordOccurrences(state)
            }
        }

        lifecycleScope.launch {
            viewModel.tenthCharacters.collect { data ->
                handleStateForTenthCharacters(data)
            }
        }
    }

    private fun handleStateForWordOccurrences(state: UIState<List<WordOccurrenceItem>>) {
        when (state) {
            is UIState.Loading -> {}
            is UIState.Success -> {
                val adapter = WordOccurrencesAdapter(state.data)
                binding.wordOccurrencesRecycler.adapter = adapter
            }
            is UIState.Error -> {}
            is UIState.Idle -> {}
        }
    }

    private fun handleStateForTenthCharacters(state: UIState<List<TenthCharacterItem>>) {
        when (state) {
            is UIState.Loading -> {}
            is UIState.Success -> {
                val adapter = TenthCharactersAdapter(state.data)
                binding.tenthCharactersRecycler.adapter = adapter
            }
            is UIState.Error -> {}
            is UIState.Idle -> {}
        }
    }
}
