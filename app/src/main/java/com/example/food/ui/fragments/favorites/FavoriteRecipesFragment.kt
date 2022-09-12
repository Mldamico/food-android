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
import com.example.food.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private  val mAdapter:FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter()}
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite_recipes, container, false)
        setupRecyclerView(view.favoritesRecipesRecyclerView)
        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner) { favoritesEntity ->
            mAdapter.setData(favoritesEntity)
        }
        return view
    }

    private fun setupRecyclerView(recyclerView: RecyclerView){
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager  = LinearLayoutManager(requireContext())
    }


}