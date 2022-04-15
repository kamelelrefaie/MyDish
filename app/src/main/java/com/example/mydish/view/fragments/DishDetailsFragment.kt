package com.example.mydish.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mydish.R
import com.example.mydish.databinding.FragmentDishDetailsBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.utils.Constants
import com.example.mydish.view.viewmodel.FavDishViewModel
import java.lang.Exception
import java.util.*


class DishDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var mFavDishDetail: FavDish? = null
    private lateinit var mBinding: FragmentDishDetailsBinding
    lateinit var mFavDishViewModel: FavDishViewModel

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_details_item_share -> {
                val type = "text/plain"
                val subject = "Check out this dish recipe"
                var extraText = ""
                val shareWith = "Share With"

                mFavDishDetail?.let {
                    var image = ""
                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }


                    var cookingInstructions = ""

                    // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(
                            it.directionToCook,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        @Suppress("DEPRECATION")
                        cookingInstructions = Html.fromHtml(it.directionToCook).toString()
                    }

                    extraText =
                        "$image \n" +
                                "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                                "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                                "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."

                }


                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        mFavDishViewModel = ViewModelProvider(requireActivity())[FavDishViewModel::class.java]

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()

        mFavDishDetail = args.dishDetails
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