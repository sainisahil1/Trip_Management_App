package io.sahil.tripmanagementapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.navigation.NavigationBarView
import io.sahil.tripmanagementapp.R
import io.sahil.tripmanagementapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBottomNavView()


    }


    private fun initViews(){
        //attach fragment
    }



    private fun initBottomNavView(){
        activityMainBinding.bottomNavBar.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){

                    R.id.home -> goBack()

                    R.id.trips -> {}//attach trip fragment

                }
                return true
            }
        })
    }

    private fun goBack(){
        if (supportFragmentManager.backStackEntryCount > 0){
            //Trip fragment is attached
            //Pop the fragment
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activityMainBinding.bottomNavBar.selectedItemId = R.id.home
        }
    }

    override fun onBackPressed() {
        goBack()
    }




}