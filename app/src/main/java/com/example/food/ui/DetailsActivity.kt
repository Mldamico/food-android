package com.example.food.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.food.R
import com.example.food.adapters.PagerAdapter
import com.example.food.data.database.entities.FavoritesEntity
import com.example.food.ui.fragments.ingredients.IngredientsFragment
import com.example.food.ui.fragments.instructions.InstructionsFragment
import com.example.food.ui.fragments.overview.OverviewFragment
import com.example.food.util.Constants
import com.example.food.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var recipeSaved= false
    private var savedRecipeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")
        val resultBundle = Bundle()
        print(args.result.image)
        Log.d("DetailActivity", args.result.toString())
        resultBundle.putParcelable(Constants.RECIPE_RESULT, args.result)
        val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        } else if(item.itemId == R.id.save_to_favorites_menu && !recipeSaved){
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (savedRecive in favoritesEntity) {
                    if (savedRecive.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecive.id
                        recipeSaved = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0, args.result)
        mainViewModel.insertFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        recipeSaved = true

    }

    private fun removeFromFavorites(item: MenuItem){
        val favoritesEntity = FavoritesEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favorites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}