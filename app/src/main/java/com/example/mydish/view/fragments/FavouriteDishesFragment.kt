package com.example.mydish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mydish.application.FavDishApplication
import com.example.mydish.databinding.FragmentFavouriteDishesBinding
import com.example.mydish.model.remote.responses.FavDish
import com.example.mydish.view.activities.MainActivity
import com.example.mydish.view.adapters.FavDishAdapter
import com.example.mydish.view.viewmodel.DashboardViewModel
import com.example.mydish.view.viewmodel.FavDishViewModel

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
        val dashboardViewModel =
            ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        mFavDishViewModel = ViewModelProvider(requireActivity())[FavDishViewModel::class.java]

        _binding = FragmentFavouriteDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding?.recyclerView?.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter: FavDishAdapter = FavDishAdapter(this)
        _binding?.recyclerView?.adapter = adapter

        mFavDishViewModel.favDishes.observe(viewLifecycleOwner) {
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
        return root
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