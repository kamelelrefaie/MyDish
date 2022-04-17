package com.example.mydish.view.activities

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.mydish.R
import com.example.mydish.databinding.ActivityMainBinding
import com.example.mydish.model.notitifcation.NotifyWorker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startWork()
        Timber.d("kamel")
        val navView: BottomNavigationView = binding.navView

        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes,
                R.id.navigation_favourite_dishes,
                R.id.navigation_random_dish
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        navView.setupWithNavController(mNavController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null)
    }

    fun hideBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
        binding.navView.visibility = View.VISIBLE

    }

    private fun createConstraints() =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).build()

    private fun createWorkRequest() =
        PeriodicWorkRequest.Builder(NotifyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(createConstraints()).build()

    private fun startWork() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "notify worker",
            ExistingPeriodicWorkPolicy.KEEP,
            createWorkRequest()
        )
    }
}