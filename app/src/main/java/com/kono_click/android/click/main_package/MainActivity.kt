package com.kono_click.android.click.main_package

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kono_click.android.click.R


class MainActivity : AppCompatActivity() {
    lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        navController = this.findNavController(R.id.my_nav_host_fragment)

//        val navController = this.findNavController(R.id.my_nav_host_fragment)
       // NavigationUI.setupActionBarWithNavController(this, navController)


    }
    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                   // or View.SYSTEM_UI_FLAG_FULLSCREEN // remove this if you want title bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}