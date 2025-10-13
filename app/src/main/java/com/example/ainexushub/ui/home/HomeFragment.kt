package com.example.ainexushub.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.ainexushub.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.provideFactory(requireActivity().application)
    }

    private lateinit var toolsAdapter: AiToolAdapter
    private val recommendationAdapter = RecommendationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolsAdapter = AiToolAdapter(
            onTryClick = { openUrl(it.url) },
            onFavoriteClick = { tool ->
                viewModel.toggleFavorite(tool)
                Snackbar.make(binding.root, if (tool.isFavorite) "Quitado de favoritos" else "Guardado en favoritos", Snackbar.LENGTH_SHORT).show()
            }
        )

        binding.toolsRecyclerView.adapter = toolsAdapter
        binding.recommendationsRecyclerView.adapter = recommendationAdapter

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text?.toString().orEmpty())
        }

        viewModel.filteredTools.observe(viewLifecycleOwner) { list ->
            toolsAdapter.submitList(list)
        }

        viewModel.recommendations.observe(viewLifecycleOwner) { recommendationAdapter.submitList(it) }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.topAiTitle.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
