package com.example.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.databinding.FragmentFavoritesBinding
import com.example.recipeapp.ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FavoritesFragment is null")
    private val viewModel: FavoritesViewModel by activityViewModels()
    private val favoritesAdapter: RecipesListAdapter = RecipesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        viewModel.loadFavoriteRecipes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        viewModel.currentFavoriteRecipes.observe(viewLifecycleOwner) { currentState ->
            favoritesAdapter.setDataSet(currentState.recipes)
            with(binding) {
                if (currentState.recipes.isEmpty()) {
                    rvFavorites.visibility = View.GONE
                    tvNoFavorites.visibility = View.VISIBLE
                } else {
                    rvFavorites.visibility = View.VISIBLE
                    tvNoFavorites.visibility = View.GONE
                    favoritesAdapter.notifyDataSetChanged()
                }
            }
        }
        favoritesAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        binding.rvFavorites.adapter = favoritesAdapter
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }
}