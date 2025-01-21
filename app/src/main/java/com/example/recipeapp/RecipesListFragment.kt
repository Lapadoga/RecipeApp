package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.data.STUB
import com.example.recipeapp.databinding.FragmentListRecipesBinding
import java.io.IOException

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for RecipesListFragment is null")
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(requireArguments()) {
            categoryId = getInt(CategoriesListFragment.CATEGORY_ID_KEY)
            categoryName = getString(CategoriesListFragment.CATEGORY_NAME_KEY)
            categoryImageUrl = getString(CategoriesListFragment.CATEGORY_IMAGE_KEY)
        }
        val context = view.context
        val drawable = try {
            val stream = context?.assets?.open(categoryImageUrl!!)
            Drawable.createFromStream(stream, null)
        } catch (e: IOException) {
            initRecycler()
            null
        }

        binding.ivRecipesHeader.setImageDrawable(drawable)
        binding.ivRecipesHeader.contentDescription =
            "${getString(R.string.text_item_category_description)} $categoryName"
        binding.tvRecipesHeader.text = categoryName

        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val dataSet = STUB.getRecipesByCategoryId(categoryId)
        val adapter = RecipesListAdapter(dataSet)
        adapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        binding.rvRecipes.adapter = adapter
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

    companion object {
        const val RECIPE_KEY = "ARG_RECIPE"
    }
}