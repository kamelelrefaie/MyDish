package com.example.mydish.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mydish.R
import com.example.mydish.databinding.DialogCustomRandomBinding
import com.example.mydish.databinding.FragmentRandomDishBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.model.remote.responses.Recipe
import com.example.mydish.utils.Constants
import com.example.mydish.view.viewmodel.FavDishViewModel
import com.example.mydish.view.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private var _binding: FragmentRandomDishBinding? = null
    lateinit var mRandomDishViewModel: RandomDishViewModel
    private lateinit var mProgressDialog: Dialog
    lateinit var mFavDishViewModel: FavDishViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(requireActivity())[RandomDishViewModel::class.java]
        mRandomDishViewModel.getRandomDishFromAPI()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mFavDishViewModel = ViewModelProvider(requireActivity())[FavDishViewModel::class.java]


        binding.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomDishFromAPI()
        }

        randomDishViewModelObserver()


        return root
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())
        var dialogCustomRandomBinding: DialogCustomRandomBinding =
            DialogCustomRandomBinding.inflate(layoutInflater)
        mProgressDialog.setContentView(dialogCustomRandomBinding.root)
        mProgressDialog.show()

    }

    private fun hideCustomProgressDialog() {
        mProgressDialog.dismiss()
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner) {
            it?.let {
                setRandomDishResponseUI(it.recipes[0])
                if (_binding!!.srlRandomDish.isRefreshing) {
                    _binding!!.srlRandomDish.isRefreshing = false
                }
            }
        }

        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                if (_binding!!.srlRandomDish.isRefreshing) {
                    _binding!!.srlRandomDish.isRefreshing = false
                }

            }
        }

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner) {
            if (!_binding!!.srlRandomDish.isRefreshing && it)
                showCustomProgressDialog()
            else hideCustomProgressDialog()

        }
    }

    private fun setRandomDishResponseUI(recipe: Recipe) {
        Glide.with(this).load(recipe.image).centerCrop().into(_binding!!.ivDishImage)
        _binding!!.tvTitle.text = recipe.title

        var dishType: String = "other"
        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            _binding!!.tvType.text = dishType
        }

        _binding!!.tvCategory.text = "other"
        var ingredients = ""
        for (value in recipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
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

        _binding!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        _binding!!.ivDishImage.setImageDrawable(
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_fav_unselected)
        )
        var addToFav = false

        _binding!!.ivFavoriteDish.setOnClickListener {

            if (addToFav) {
                Toast.makeText(
                    requireActivity(),
                    "you have already added your to dish to favourtie",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                addToFav = true
                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )




                mFavDishViewModel.insert(randomDishDetails)


                // TODO Step 9: Once the dish is inserted you can acknowledge user by Toast message as below and also update the favorite image by selected.
                // START
                _binding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_fav_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.title_favourite_dishes),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}