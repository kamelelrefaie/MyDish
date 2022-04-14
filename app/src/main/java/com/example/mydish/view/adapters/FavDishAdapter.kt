package com.example.mydish.view.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mydish.R
import com.example.mydish.databinding.ItemDishLayoutBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.utils.Constants
import com.example.mydish.view.activities.AddUpdateDishActivity
import com.example.mydish.view.fragments.AllDishesFragment
import com.example.mydish.view.fragments.FavouriteDishesFragment

class FavDishAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemDishLayoutBinding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(fragment.layoutInflater, parent, false)
        return ViewHolder(itemDishLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        holder.tvDish.text = dish.title
        Glide.with(fragment).load(dish.image).into(holder.tvDishImage)


        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment) {
                fragment.dishDetails(dish)
            }
            if (fragment is FavouriteDishesFragment) {
                fragment.dishDetails(dish)
            }
        }

        holder.ibMore.setOnClickListener {

            val popupMenu = PopupMenu(fragment.context, holder.ibMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adatper, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {
                    val intent =
                        Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)
                } else if (it.itemId == R.id.action_delete_dish) {
                    if (fragment is AllDishesFragment) {
                        fragment.deleteDish(dish)
                    }
                }
                true
            }

            popupMenu.show()
        }

        if (fragment is AllDishesFragment) {
            holder.ibMore.visibility = View.VISIBLE
        }
        if (fragment is FavouriteDishesFragment) {
            holder.ibMore.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun setDishList(favDishList: List<FavDish>) {
        dishes = favDishList
        notifyDataSetChanged()
    }

    class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        var tvDish = view.tvDishTitle
        var tvDishImage = view.ivDishImage
        var ibMore = view.ibMore


    }
}