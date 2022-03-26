package com.example.mydish.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mydish.R
import com.example.mydish.application.FavDishApplication
import com.example.mydish.databinding.FragmentDishDetailsBinding
import com.example.mydish.view.viewmodel.FavDishViewModel
import java.lang.Exception
import java.util.*


class DishDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var mBinding: FragmentDishDetailsBinding
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModel.FavDishViewModelFactory(repository = FavDishApplication().repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)



        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity()).load(args.dishDetails.image).centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Palette.from(resource!!.toBitmap()).generate { palette ->

                                mBinding.rlDishDetailMain.setBackgroundColor(
                                    palette?.vibrantSwatch?.rgb ?: 0
                                )

                            }
                            return false
                        }

                    })
                    .into(mBinding.ivDishImage)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            mBinding.tvTitle.text = args.dishDetails.title
            mBinding.tvType.text = args.dishDetails.type.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }

            mBinding.tvCategory.text = args.dishDetails.category
            mBinding.tvIngredients.text = args.dishDetails.ingredients
            mBinding.tvCookingDirection.text = args.dishDetails.directionToCook
            mBinding.tvCookingTime.text = resources.getString(
                R.string.lbl_estimate_cooking_time,
                args.dishDetails.cookingTime
            )
        }
        mBinding.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favouriteDish = !args.dishDetails.favouriteDish
            mFavDishViewModel.insert(args.dishDetails)
            if (args.dishDetails.favouriteDish) {
                Glide.with(this).load(R.drawable.ic_fav_selected).into(mBinding.ivFavoriteDish)
                Toast.makeText(requireActivity(), "added to favourites", Toast.LENGTH_SHORT).show()
            } else {
                Glide.with(this).load(R.drawable.ic_fav_unselected).into(mBinding.ivFavoriteDish)

            }
        }

        if (args.dishDetails.favouriteDish) {
            mBinding.ivFavoriteDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_fav_selected
                )
            )
        }
    }


}