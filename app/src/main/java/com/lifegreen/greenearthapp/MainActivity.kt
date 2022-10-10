package com.lifegreen.greenearthapp

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.lifegreen.greenearthapp.activity.LoginActivity
import com.lifegreen.greenearthapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  var i =0;
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val navController = navHostFragment?.findNavController()
        val popUpMenu = PopupMenu(this, null)
        popUpMenu.inflate(R.menu.bottom_nav)
        binding.bottomBar.setupWithNavController(popUpMenu.menu, navController!!)
        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener {
                controller,
                destination,
                 arguments ->
            title = when (destination.id) {
                R.id.cartFragment -> getString(R.string.cart_fragment)
                R.id.moreFragment -> getString(R.string.more_fragment)
                else -> getString(R.string.app_name)
            }
        }
        )

        binding.bottomBar.onItemSelected = {
            when(it){
                0 -> {
                    i = 0;
                    navController.navigate(R.id.homeFragment)
                }
                1-> i=1
                2 -> i=2
            }
        }

        if(FirebaseAuth.getInstance().currentUser==null){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(i==0)
            finish()
    }
}