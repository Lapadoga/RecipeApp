package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.RecipesListFragment.Companion.RECIPE_KEY
import com.example.recipeapp.data.STUB
import com.example.recipeapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FavoritesFragment is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val favoriteRecipes = HashSet(PreferencesUtils.getFavorites(context).map { it.toInt() })
        val dataSet = STUB.getRecipesByIds(favoriteRecipes)
        val adapter = RecipesListAdapter(dataSet)
        adapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        binding.rvFavorites.adapter = adapter
    }

    private fun initUI() {
        val favoriteRecipes = PreferencesUtils.getFavorites(context)
        if (favoriteRecipes.size == 0) {
            with(binding) {
                rvFavorites.visibility = View.GONE
                tvNoFavorites.visibility = View.VISIBLE
            }
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            val recipe = STUB.getRecipeById(recipeId)
            val bundle = bundleOf(RECIPE_KEY to recipe)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}