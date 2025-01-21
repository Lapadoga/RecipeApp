package com.example.recipeapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.data.Ingredient
import com.example.recipeapp.data.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipeFragment is null")
    private var isFavorite = false

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
                "${getString(R.string.text_item_category_description)} ${recipe.title.lowercase()}"

            val favoriteRecipes = PreferencesUtils.getFavorites(context)
            val drawableId =
                if (favoriteRecipes.contains(recipe.id.toString())) {
                    isFavorite = true
                    R.drawable.ic_heart
                } else {
                    isFavorite = false
                    R.drawable.ic_heart_empty
                }
            ibFavorites.setImageResource(drawableId)
            ibFavorites.contentDescription = getString(R.string.text_favorites)
            ibFavorites.setOnClickListener {
                val favoriteRecipes = PreferencesUtils.getFavorites(context)
                val newDrawableId = if (isFavorite) {
                    favoriteRecipes.remove(recipe.id.toString())
                    isFavorite = false
                    R.drawable.ic_heart_empty
                } else {
                    favoriteRecipes.add(recipe.id.toString())
                    isFavorite = true
                    R.drawable.ic_heart
                }
                ibFavorites.setImageResource(newDrawableId)
                saveFavorites(favoriteRecipes)
            }
        }
    }

    private fun initRecycler(ingredients: List<Ingredient>, method: List<String>) {
        with(binding) {
            val dividerInset =
                rvIngredients.context.resources.getDimension(R.dimen.dimen_item_rv_recipe).toInt()

            val ingredientsDividerDecoration = MaterialDividerItemDecoration(
                rvIngredients.context,
                MaterialDividerItemDecoration.VERTICAL
            )
            ingredientsDividerDecoration.isLastItemDecorated = false
            ingredientsDividerDecoration.setDividerColorResource(
                rvIngredients.context,
                R.color.rv_divider
            )
            ingredientsDividerDecoration.dividerInsetStart = dividerInset
            ingredientsDividerDecoration.dividerInsetEnd = dividerInset
            val ingredientsAdapter = IngredientsAdapter(ingredients)
            rvIngredients.adapter = ingredientsAdapter
            rvIngredients.addItemDecoration(ingredientsDividerDecoration)

            val methodDividerDecoration = MaterialDividerItemDecoration(
                rvMethod.context,
                MaterialDividerItemDecoration.VERTICAL
            )
            methodDividerDecoration.isLastItemDecorated = false
            methodDividerDecoration.setDividerColorResource(rvMethod.context, R.color.rv_divider)
            methodDividerDecoration.dividerInsetStart = dividerInset
            methodDividerDecoration.dividerInsetEnd = dividerInset
            val methodAdapter = MethodAdapter(method)
            rvMethod.adapter = methodAdapter
            rvMethod.addItemDecoration(methodDividerDecoration)


            sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    portionSize.text = "$p1"
                    ingredientsAdapter.updateIngredients(p1)
                    ingredientsAdapter.notifyDataSetChanged()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    return
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    return
                }
            })
            sbPortions.progress = 1
        }
    }

    private fun saveFavorites(recipesId: Set<String>) {
        val context = context ?: return
        val sharedPrefs = context.getSharedPreferences(FAVORITES_FILE_KEY, Context.MODE_PRIVATE)

        with(sharedPrefs.edit()) {
            putStringSet(RECIPES_ID_KEY, recipesId)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FAVORITES_FILE_KEY = "com.example.recipeapp.favorites"
        const val RECIPES_ID_KEY = "favoriteRecipes"
    }
}