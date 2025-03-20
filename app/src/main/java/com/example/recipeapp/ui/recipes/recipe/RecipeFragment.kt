package com.example.recipeapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipeFragment is null")
    private var isFavorite = false
    private val viewModel: RecipeViewModel by activityViewModels()
    private val methodAdapter: MethodAdapter = MethodAdapter(listOf())
    private val ingredientsAdapter: IngredientsAdapter = IngredientsAdapter(listOf())
    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipeId = recipeFragmentArgs.recipeId
        viewModel.loadRecipe(recipeId)

        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
    }

    private fun initUI() {
        addRecyclerDecorations()
        with(binding) {
            rvMethod.adapter = methodAdapter
            rvIngredients.adapter = ingredientsAdapter

            val seekBarChangeListener =
                PortionSeekBarListener { viewModel.onSeekBarChange(sbPortions.progress) }
            sbPortions.setOnSeekBarChangeListener(seekBarChangeListener)

            viewModel.currentRecipe.observe(viewLifecycleOwner) { currentState ->
                currentState.recipe?.let {
                    val recipe = currentState.recipe

                    tvRecipeTitle.text = recipe.title
                    Glide.with(this@RecipeFragment)
                        .load(currentState.recipeImageUrl)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_error)
                        .into(ivRecipe)
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

                    ingredientsAdapter.setDataSet(currentState.recipe.ingredients)
                    methodAdapter.setDataSet(currentState.recipe.method)
                    sbPortions.progress = currentState.portionSize
                    portionSize.text = "${currentState.portionSize}"
                    ingredientsAdapter.updateIngredients(currentState.portionSize)
                    ingredientsAdapter.notifyDataSetChanged()
                    methodAdapter.notifyDataSetChanged()
                }
            }
            ibFavorites.setOnClickListener {
                viewModel.onFavoritesClicked()
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

private class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        return
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        return
    }
}