package com.example.shopingofmine.ui.activity


import android.app.AlertDialog
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.shopingofmine.R
import com.example.shopingofmine.databinding.ActivityMainBinding
import com.example.shopingofmine.datastore.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var isDarkMode: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        themeSetInit()
        connectionCheckInit()
        topAppBarInit()
        bottomNavigationInit()
        keepSplashScreen()

    }

    private fun connectionCheckInit() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val alertDialog: AlertDialog? = this@MainActivity.let {
            AlertDialog.Builder(it)
        }.setMessage("لطفا اتصال اینترنت را بررسی کنید.")
            ?.setTitle("خطا")
            ?.create()
        alertDialog?.setCancelable(false)
        alertDialog?.setCanceledOnTouchOutside(false)
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                 alertDialog?.dismiss()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread { alertDialog?.show() }
            }
        }
        val connectivityManager = ContextCompat.getSystemService(this, ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun bottomNavigationInit() {
        val bottomNavigationView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun keepSplashScreen() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isDarkMode != null) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun themeSetInit() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.preferences.collect { theme ->
                    if (AppCompatDelegate.getDefaultNightMode() != theme.value) {
                        AppCompatDelegate.setDefaultNightMode(theme.value)
                    }
                    isDarkMode = theme == Theme.NIGHT
                }
            }
        }
    }

    private fun topAppBarInit() {
        binding.topAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.options -> {
                    navController.navigate(R.id.optionsFragment, bundleOf("darkMode" to isDarkMode!!))
                    true
                }
                R.id.cart -> {
                    Toast.makeText(this, "cart clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}