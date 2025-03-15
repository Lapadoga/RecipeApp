package com.example.recipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.data.repositories.RecipesRepository
import com.example.recipeapp.databinding.FragmentListCategoriesBinding
import kotlinx.serialization.json.Json

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for CategoriesListFragment is null")
    private val viewModel: CategoriesListViewModel by activityViewModels()
    private val categoriesAdapter: CategoriesListAdapter = CategoriesListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        viewModel.loadCategories()

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
        viewModel.currentCategories.observe(viewLifecycleOwner) { currentState ->
            categoriesAdapter.setDataSet(currentState.categories)
        }
        with(binding) {
            rvCategories.adapter = categoriesAdapter
            categoriesAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    openRecipesByCategoryId(categoryId)
                }
            })
        }
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = viewModel.currentCategories.value?.categories?.find { it.id == categoryId }
        if (category == null)
            Toast.makeText(context, RecipesRepository.ERROR_TEXT, Toast.LENGTH_SHORT).show()
        else
            findNavController().navigate(
                CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                    Json.encodeToString(category)
                )
            )
    }
}