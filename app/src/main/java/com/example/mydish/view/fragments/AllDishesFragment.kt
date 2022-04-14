package com.example.mydish.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydish.R
import com.example.mydish.databinding.DialogCustomListBinding
import com.example.mydish.databinding.FragmentAllDishesBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.utils.Constants
import com.example.mydish.view.activities.AddUpdateDishActivity
import com.example.mydish.view.activities.MainActivity
import com.example.mydish.view.adapters.CustomListItemAdapter
import com.example.mydish.view.adapters.FavDishAdapter
import com.example.mydish.view.viewmodel.FavDishViewModel

class AllDishesFragment : Fragment() {

    private var _binding: FragmentAllDishesBinding? = null
    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var mCustomListDialog: Dialog

    lateinit var  mFavDishViewModel: FavDishViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        mFavDishViewModel = ViewModelProvider(requireActivity())[FavDishViewModel::class.java]
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.recyclerView?.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this)
        _binding?.recyclerView?.adapter = mFavDishAdapter

        displayAllDishes()

    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            AllDishesFragmentDirections.actionAllDishesToDishDetails(
                favDish
            )
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView()
        }
    }

    fun deleteDish(dish: FavDish) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete Dish")
            .setMessage(resources.getString(R.string.msg_delete_dish_dialog, dish.title))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(resources.getString(R.string.lbl_yes)) { dialog, _ ->
                mFavDishViewModel.delete(dish)
                Toast.makeText(
                    requireActivity(),
                    "Deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }.setNegativeButton(resources.getString(R.string.lbl_no)) { dialog, _ ->
                dialog.dismiss()
            }

        alertDialog.show()
    }

    private fun filterDishesListDialog() {
        mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0, Constants.ALL_ITEMS)

        val adapter =
            CustomListItemAdapter(requireActivity(),this, dishTypes, Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

     fun filterSelection(filterItemSelection: String) {
        mCustomListDialog.dismiss()

        if (filterItemSelection == Constants.ALL_ITEMS) {
            displayAllDishes()
        } else {
           mFavDishViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner){
               if (it.isEmpty()) {
                   _binding?.recyclerView?.visibility = View.GONE
                   _binding?.textView?.visibility = View.VISIBLE

                   mFavDishAdapter.setDishList(it)
               } else {
                   _binding?.recyclerView?.visibility = View.VISIBLE
                   _binding?.textView?.visibility = View.GONE

                   mFavDishAdapter.setDishList(it)
               }
           }
        }
    }

    private fun displayAllDishes() {
        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                _binding?.recyclerView?.visibility = View.GONE
                _binding?.textView?.visibility = View.VISIBLE

                mFavDishAdapter.setDishList(it)
            } else {
                _binding?.recyclerView?.visibility = View.VISIBLE
                _binding?.textView?.visibility = View.GONE

                mFavDishAdapter.setDishList(it)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
            }
            R.id.action_filter_dishes -> {
                filterDishesListDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}