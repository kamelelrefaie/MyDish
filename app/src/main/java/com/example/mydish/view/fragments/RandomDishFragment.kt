package com.example.mydish.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mydish.databinding.FragmentRandomDishBinding
import com.example.mydish.model.entities.RandomDish
import com.example.mydish.view.viewmodel.NotificationsViewModel
import com.example.mydish.view.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private var _binding: FragmentRandomDishBinding? = null
    private lateinit var mRandomDishViewModel: RandomDishViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        mRandomDishViewModel.getRandomRecipeFromAPI()
        return root
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(this) {
            it?.let {

            }
        }

        mRandomDishViewModel.randomDishLoadingError.observe(this) {
            it?.let {

            }
        }

        mRandomDishViewModel.loadRandomDish.observe(this) {

        }
    }

    private fun setRandomDishResponseUI(recipe: RandomDish.Recipe) {
        Glide.with(this).load(recipe.image).centerCrop().into(_binding!!.ivDishImage)
          _binding!!.tvTitle.text = recipe.title

        var dishType: String = "other"
        if(recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            _binding!!.tvType.text = dishType
        }

        _binding!!.tvCategory.text = "other"
        var ingredients =""
        for(value in recipe.extendedIngredients){
            if(ingredients.isEmpty()){
             ingredients = value.original
            }else{
                ingredients += ", \n" + value.original
            }
        }
        _binding!!.tvIngredients.text = ingredients

        // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _binding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            _binding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}