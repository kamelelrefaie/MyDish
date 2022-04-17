package com.example.mydish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mydish.databinding.FragmentFavouriteDishesBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.view.activities.MainActivity
import com.example.mydish.view.adapters.FavDishAdapter
import com.example.mydish.view.viewmodel.FavDishViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouriteDishesFragment : Fragment() {

    private var _binding: FragmentFavouriteDishesBinding? = null
    lateinit var mFavDishViewModel: FavDishViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mFavDishViewModel = ViewModelProvider(requireActivity())[FavDishViewModel::class.java]

        _binding = FragmentFavouriteDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding?.recyclerView?.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter: FavDishAdapter = FavDishAdapter(this)
        _binding?.recyclerView?.adapter = adapter

        displayFavDishes(adapter)

        return root
    }

    private fun displayFavDishes(adapter: FavDishAdapter) {
        lifecycleScope.launch {
            mFavDishViewModel.favDishes.collect {
                if (it.isEmpty()) {
                    _binding?.recyclerView?.visibility = View.GONE
                    _binding?.textView?.visibility = View.VISIBLE

                    adapter.setDishList(it)
                } else {
                    _binding?.recyclerView?.visibility = View.VISIBLE
                    _binding?.textView?.visibility = View.GONE

                    adapter.setDishList(it)
                }
            }
        }
//liveDate version
        //        mFavDishViewModel.favDishes.observe(viewLifecycleOwner) {
//            if (it.isEmpty()) {
//                _binding?.recyclerView?.visibility = View.GONE
//                _binding?.textView?.visibility = View.VISIBLE
//
//                adapter.setDishList(it)
//            } else {
//                _binding?.recyclerView?.visibility = View.VISIBLE
//                _binding?.textView?.visibility = View.GONE
//
//                adapter.setDishList(it)
//            }
//
//        }
    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavouriteDishesFragmentDirections.actionFavouriteDishesToDishDetails(
                favDish
            )
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView()
        }
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