package com.example.recipeapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipeFragment is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(RecipesListFragment.RECIPE_KEY, Recipe::class.java)
        else
            arguments?.getParcelable(RecipesListFragment.RECIPE_KEY)

        recipe?.let {
            val context = view.context

            initUI(recipe, context)
            initRecycler(recipe.ingredients, recipe.method)
        }
    }

    private fun initUI(recipe: Recipe, context: Context) {
        val drawable = try {
            val stream = context.assets?.open(recipe.imageUrl)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            null
        }

        with(binding) {
            tvRecipeTitle.text = recipe.title
            ivRecipe.setImageDrawable(drawable)
            ivRecipe.contentDescription =
                "${R.string.text_item_category_description} ${recipe.title.lowercase()}"
        }
    }

    private fun initRecycler(ingredients: List<Ingredient>, method: List<String>) {
        with(binding) {
            val ingredientsDividerDecoration = MaterialDividerItemDecoration(
                rvIngredients.context,
                MaterialDividerItemDecoration.VERTICAL
            )
            ingredientsDividerDecoration.isLastItemDecorated = false
            ingredientsDividerDecoration.setDividerColorResource(
                rvIngredients.context,
                R.color.rv_divider
            )
            rvIngredients.adapter = IngredientsAdapter(ingredients)
            rvIngredients.addItemDecoration(ingredientsDividerDecoration)

            val methodDividerDecoration = MaterialDividerItemDecoration(
                rvMethod.context,
                MaterialDividerItemDecoration.VERTICAL
            )
            methodDividerDecoration.isLastItemDecorated = false
            methodDividerDecoration.setDividerColorResource(rvMethod.context, R.color.rv_divider)
            rvMethod.adapter = MethodAdapter(method)
            rvMethod.addItemDecoration(methodDividerDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}