package io.sahil.tripmanagementapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.ActivityMainBinding
import io.sahil.tripmanagementapp.ui.fragments.HomeFragment
import io.sahil.tripmanagementapp.ui.fragments.TripsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavView()
        initViews()

    }


    private fun initViews(){
        supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }



    private fun initBottomNavView(){
        activityMainBinding.bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                R.id.nav_trips -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, TripsFragment(), TripsFragment::class.java.simpleName)
                        .addToBackStack(TripsFragment::class.java.simpleName)
                        .commit()
                }

            }
            true
        }
    }

    private fun goBack(){
        if (supportFragmentManager.backStackEntryCount > 0){
            //Trip fragment is attached
            //Pop the fragment
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activityMainBinding.bottomNavBar.selectedItemId = R.id.nav_home
        }
    }

    override fun onBackPressed() {
        goBack()
    }




}