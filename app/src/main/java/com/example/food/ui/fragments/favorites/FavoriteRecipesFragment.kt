package com.example.food.ui.fragments.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.adapters.FavoriteRecipesAdapter
import com.example.food.databinding.FragmentFavoriteRecipesBinding
import com.example.food.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private val mAdapter:FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity())}
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter
        setupRecyclerView(binding.favoritesRecipesRecyclerView)
//        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favoritesEntity ->
//            mAdapter.setData(favoritesEntity)
//        }
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager  = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}