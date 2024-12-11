package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for CategoriesListFragment is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val categoriesList = STUB.getCategories()
        val categoriesAdapter = CategoriesListAdapter(categoriesList)
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId, categoriesList)
            }
        })
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun openRecipesByCategoryId(categoryId: Int, categoriesList: List<Category>) {
        val currentCategory = categoriesList.find { it.id == categoryId } ?: return
        val categoryName = currentCategory.title
        val categoryImageUrl = currentCategory.imageUrl
        val bundle = bundleOf(
            CATEGORY_ID_KEY to categoryId,
            CATEGORY_NAME_KEY to categoryName,
            CATEGORY_IMAGE_KEY to categoryImageUrl,
        )
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    companion object {
        const val CATEGORY_ID_KEY = "ARG_CATEGORY_ID"
        const val CATEGORY_NAME_KEY = "ARG_CATEGORY_NAME"
        const val CATEGORY_IMAGE_KEY = "ARG_CATEGORY_IMAGE_URL"
    }
}