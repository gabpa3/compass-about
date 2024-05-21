package com.gabcode.compassabout.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.gabcode.compassabout.R
import com.gabcode.compassabout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        val factory = AboutViewModelFactory()
        ViewModelProvider(this, factory)[AboutViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.fetchContent()

        savedInstanceState?.getString(CURRENT_ROUTE)?.let { route ->
            navigate(NavigatorRoute.valueOf(route))
        } ?: navigate(NavigatorRoute.HOME)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(binding.mainContent.id, fragment)
            setReorderingAllowed(true)
        }
    }

    override fun navigate(route: NavigatorRoute) {
        when (route) {
            NavigatorRoute.HOME -> loadFragment(HomeFragment.newInstance())
            NavigatorRoute.ABOUT -> loadFragment(AboutListFragment.newInstance())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val currentFragment = supportFragmentManager.fragments.firstOrNull()
        if (currentFragment is AboutListFragment) {
            outState.putString(CURRENT_ROUTE, NavigatorRoute.ABOUT.name)
        }
    }

    companion object {
        private const val CURRENT_ROUTE = "current_route"
    }
}
