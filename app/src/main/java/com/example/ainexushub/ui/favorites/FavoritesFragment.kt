package com.example.ainexushub.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ainexushub.databinding.FragmentFavoritesBinding
import com.example.ainexushub.ui.home.AiToolAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels {
        FavoritesViewModel.provideFactory(requireActivity().application)
    }

    private lateinit var favoritesAdapter: AiToolAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesAdapter = AiToolAdapter(
            onTryClick = { openUrl(it.url) },
            onFavoriteClick = { tool -> viewModel.removeFavorite(tool) }
        )

        binding.favoritesRecyclerView.adapter = favoritesAdapter

        viewModel.favorites.observe(viewLifecycleOwner) { items ->
            favoritesAdapter.submitList(items)
            binding.emptyStateText.isVisible = items.isEmpty()
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
