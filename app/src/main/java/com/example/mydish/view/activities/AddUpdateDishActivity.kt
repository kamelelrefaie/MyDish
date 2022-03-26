package com.example.mydish.view.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mydish.R
import com.example.mydish.application.FavDishApplication
import com.example.mydish.databinding.ActivityAddUpdateDishBinding
import com.example.mydish.databinding.DialogCustomListBinding
import com.example.mydish.databinding.DialogCustomeImageSelectionBinding
import com.example.mydish.model.entities.FavDish
import com.example.mydish.utils.Constants
import com.example.mydish.utils.Constants.DISH_CATEGORY
import com.example.mydish.utils.Constants.DISH_COOKING_TIME
import com.example.mydish.utils.Constants.DISH_TYPE
import com.example.mydish.utils.Constants.dishCategories
import com.example.mydish.utils.Constants.dishCookTime
import com.example.mydish.utils.Constants.dishTypes
import com.example.mydish.view.adapters.CustomListItemAdapter
import com.example.mydish.view.viewmodel.FavDishViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.*


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private lateinit var mCustomListDialog: Dialog
    private var mFavDishDetails: FavDish? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModel.FavDishViewModelFactory((application as FavDishApplication).repository)
    }

    private var imagePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)) {
            mFavDishDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }
        setActionBar()

        mFavDishDetails?.let {
            imagePath = it.image
            Glide.with(this).load(imagePath).centerCrop().into(mBinding.ivDishImage)
            mBinding.etTitle.setText(it.title)
            mBinding.etType.setText(it.type)
            mBinding.etCategory.setText(it.category)
            mBinding.etIngredients.setText(it.ingredients)
            mBinding.etCookingTime.setText(it.cookingTime)
            mBinding.etDirection.setText(it.directionToCook)

            mBinding.btnAdd.text = "Edit Dish"

            mBinding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_vector_edit
                )
            )

        }

        mBinding.ivAddDishImage.setOnClickListener(this)
        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.btnAdd.setOnClickListener(this)

    }

    private fun setActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)

        if (mFavDishDetails != null) {
            supportActionBar?.title = "Edit Dish"
        } else {
            supportActionBar?.title = "Add Dish"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_add_dish_image -> {
                customImageSelectionDialog()

            }
            R.id.et_type -> {
                customItemDialog(
                    resources.getString(R.string.title_select_dish_type), dishTypes(),
                    DISH_TYPE
                )

            }
            R.id.et_category -> {
                customItemDialog(
                    resources.getString(R.string.title_select_dish_category), dishCategories(),
                    DISH_CATEGORY
                )

            }
            R.id.et_cooking_time -> {
                customItemDialog(
                    resources.getString(R.string.title_select_dish_cooking_time), dishCookTime(),
                    DISH_COOKING_TIME
                )

            }
            R.id.btn_add -> {
                val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                val type = mBinding.etType.text.toString().trim { it <= ' ' }
                val category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                val ingredients = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTime = mBinding.etCookingTime.text.toString().trim { it <= ' ' }
                val directions = mBinding.etDirection.text.toString().trim { it <= ' ' }
                when {
                    TextUtils.isEmpty(imagePath) ->
                        Toast.makeText(this, "select image please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(title) ->
                        Toast.makeText(this, "select title please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(type) ->
                        Toast.makeText(this, "select type please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(category) ->
                        Toast.makeText(this, "select category please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(ingredients) ->
                        Toast.makeText(this, "select ingredients please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(cookingTime) ->
                        Toast.makeText(this, "select cookingTime please", Toast.LENGTH_SHORT).show()
                    TextUtils.isEmpty(directions) ->
                        Toast.makeText(this, "select directions please", Toast.LENGTH_SHORT).show()
                    else -> {
                        var favouriteDish = false
                        var dishId = 0
                        var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL

                        mFavDishDetails?.let {
                            dishId = it.id
                            favouriteDish = it.favouriteDish
                            imageSource = it.imageSource
                        }

                        val favDish: FavDish = FavDish(
                            imagePath,
                            imageSource,
                            title,
                            type,
                            category,
                            ingredients,
                            cookingTime,
                            directions,
                            favouriteDish, dishId
                        )
                        if (dishId == 0) {
                            try {
                                mFavDishViewModel.insert(favDish)
                            } catch (E: Exception) {
                                Log.e("ERROR", "${E}")
                            }
                            Toast.makeText(
                                this,
                                "you have successfully added your favourite dish details",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            try {
                                mFavDishViewModel.insert(favDish)
                            } catch (E: Exception) {
                                Log.e("ERROR", "${E}")
                            }
                            Toast.makeText(
                                this,
                                "you have successfully edited your favourtite dish details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                        finish()
                    }

                }
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomeImageSelectionBinding =
            DialogCustomeImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                dispatchTakePictureIntent()
                            }
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {

                                val i = Intent()
                                i.type = "image/*"
                                i.action = Intent.ACTION_GET_CONTENT

                                startActivityForResult(
                                    Intent.createChooser(i, "Select Picture"),
                                    SELECT_PICTURE
                                )
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
            dialog.dismiss()

        }

    }

    private fun showRationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It seems that you turn off permission to enable it go to Application Settings")
            .setPositiveButton(
                "go to settings"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }

            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            DISH_TYPE -> {
                mCustomListDialog.dismiss()
                mBinding.etType.setText(item)
            }
            DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            DISH_COOKING_TIME -> {
                mCustomListDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Glide.with(this).load(imageBitmap).centerCrop().into(mBinding.ivDishImage)

            imagePath = saveImageToInternalStorage(imageBitmap)

            mBinding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_vector_edit
                )
            )
        }

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            Glide.with(this).load(selectedImageUri).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).listener(object :
                    RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource?.let {
                            val bitmap = resource.toBitmap()
                            imagePath = saveImageToInternalStorage(bitmap)
                        }
                        return false
                    }

                }).into(mBinding.ivDishImage)

            mBinding.ivAddDishImage.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_vector_edit
                )
            )


        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    private fun customItemDialog(title: String, itemsList: List<String>, selection: String) {
        mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this, null, itemsList, selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()


    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val SELECT_PICTURE = 2
        private const val IMAGE_DIRECTORY = "favDishImages"
    }
}