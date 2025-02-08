package com.example.recipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.ui.recipes.recipesList.RecipesListFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipeFragment is null")
    private var isFavorite = false
    private val viewModel: RecipeViewModel by activityViewModels()
    private val methodAdapter: MethodAdapter = MethodAdapter(listOf())
    private val ingredientsAdapter: IngredientsAdapter = IngredientsAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipeId = arguments?.getInt(RecipesListFragment.RECIPE_KEY)

        recipeId?.let {
            viewModel.loadRecipe(recipeId)
        }

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initRecycler()
    }

    private fun initUI() {
        with(binding) {
            viewModel.currentRecipe.observe(viewLifecycleOwner) { currentState ->
                currentState.recipe?.let {
                    val recipe = currentState.recipe
                    val drawable = try {
                        val stream = view?.context?.assets?.open(recipe.imageUrl)
                        Drawable.createFromStream(stream, null)
                    } catch (e: IOException) {
                        null
                    }

                    tvRecipeTitle.text = recipe.title
                    ivRecipe.setImageDrawable(drawable)
                    ivRecipe.contentDescription =
                        "${getString(R.string.text_item_category_description)} ${recipe.title.lowercase()}"

                    val drawableId =
                        if (currentState.isFavorite) {
                            isFavorite = true
                            R.drawable.ic_heart
                        } else {
                            isFavorite = false
                            R.drawable.ic_heart_empty
                        }
                    ibFavorites.setImageResource(drawableId)
                }
            }
            ibFavorites.setOnClickListener {
                viewModel.onFavoritesClicked()
            }
        }
    }

    private fun initRecycler() {
        addRecyclerDecorations()
        with(binding) {
            rvMethod.adapter = methodAdapter
            rvIngredients.adapter = ingredientsAdapter

            sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.onSeekBarChange(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    return
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    return
                }
            })

            viewModel.currentRecipe.observe(viewLifecycleOwner) { currentState ->
                currentState.recipe?.let {
                    ingredientsAdapter.setDataSet(currentState.recipe.ingredients)
                    methodAdapter.setDataSet(currentState.recipe.method)
                    sbPortions.progress = currentState.portionSize
                    portionSize.text = "${currentState.portionSize}"
                    ingredientsAdapter.updateIngredients(currentState.portionSize)
                    ingredientsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addRecyclerDecorations() {
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
            rvIngredients.addItemDecoration(ingredientsDividerDecoration)

            val methodDividerDecoration = MaterialDividerItemDecoration(
                rvMethod.context,
                MaterialDividerItemDecoration.VERTICAL
            )
            methodDividerDecoration.isLastItemDecorated = false
            methodDividerDecoration.setDividerColorResource(rvMethod.context, R.color.rv_divider)
            methodDividerDecoration.dividerInsetStart = dividerInset
            methodDividerDecoration.dividerInsetEnd = dividerInset
            rvMethod.addItemDecoration(methodDividerDecoration)
        }
    }

    companion object {
        const val FAVORITES_FILE_KEY = "com.example.recipeapp.favorites"
        const val RECIPES_ID_KEY = "favoriteRecipes"
    }
}