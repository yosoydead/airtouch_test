package com.example.tentativatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup the navigation controller to use my navigation graph
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        //this will link the fragments and their routes and will display the fragments label as the title bar
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    //this function takes care of the up button press event
    //navigates backwards and the app is minimized when the home fragment is on top
            //and the back button is pressed
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, null)
    }
}
