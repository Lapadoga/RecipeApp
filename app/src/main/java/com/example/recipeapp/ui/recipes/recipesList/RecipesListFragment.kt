package com.example.recipeapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentListRecipesBinding
import com.example.recipeapp.ui.categories.CategoriesListFragment

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipesListFragment is null")
    private val viewModel: RecipesListViewModel by activityViewModels()
    private val recipesAdapter: RecipesListAdapter = RecipesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        val categoryId = requireArguments().getInt(CategoriesListFragment.CATEGORY_ID_KEY)
        viewModel.loadCategory(categoryId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        with(binding) {
            viewModel.currentRecipes.observe(viewLifecycleOwner) { currentState ->
                recipesAdapter.setDataSet(currentState.recipes)
                ivRecipesHeader.setImageDrawable(currentState.categoryImage)
                ivRecipesHeader.contentDescription =
                    "${getString(R.string.text_item_category_description)} ${currentState.categoryTitle}"
                tvRecipesHeader.text = currentState.categoryTitle
            }
            recipesAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
            rvRecipes.adapter = recipesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(RECIPE_KEY to recipeId)
        findNavController().navigate(R.id.action_recipesListFragment_to_recipeFragment, bundle)
    }

    companion object {
        const val RECIPE_KEY = "RECIPE_ID"
    }
}