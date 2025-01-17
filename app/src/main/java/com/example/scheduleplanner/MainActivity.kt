
package com.example.scheduleplanner

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.scheduleplanner.fragment.CalendarFragment
import com.example.scheduleplanner.fragment.ContactFragment
import com.example.scheduleplanner.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val FRAGMENT_HOME = 0
    private val FRAGMENT_CALENDAR = 1
    private val FRAGMENT_CONTACT = 2

    private var mCurrentFragment = FRAGMENT_HOME
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mBottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        mBottomNavigationView = findViewById(R.id.bottom_navigation)
        mDrawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        replaceFragment(HomeFragment())
        navigationView.menu.findItem(R.id.home).isChecked = true
        mBottomNavigationView.menu.findItem(R.id.bottom_home).isChecked = true

        mBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    openHomeFragment()
                    navigationView.menu.findItem(R.id.home).isChecked = true
                }
                R.id.bottom_calendar -> {
                    openCalendarFragment()
                    navigationView.menu.findItem(R.id.calendar).isChecked = true
                }
                R.id.bottom_contact -> {
                    openContactFragment()
                    navigationView.menu.findItem(R.id.contact).isChecked = true
                }
            }
            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                openHomeFragment()
                mBottomNavigationView.menu.findItem(R.id.bottom_home).isChecked = true
            }
            R.id.calendar -> {
                openCalendarFragment()
                mBottomNavigationView.menu.findItem(R.id.bottom_calendar).isChecked = true
            }
            R.id.contact -> {
                openContactFragment()
                mBottomNavigationView.menu.findItem(R.id.bottom_contact).isChecked = true
            }
            R.id.logout -> {
                logOut()
            }
        }

        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun openHomeFragment() {
        if (mCurrentFragment != FRAGMENT_HOME) {
            replaceFragment(HomeFragment())
            mCurrentFragment = FRAGMENT_HOME

        }
    }

    private fun openCalendarFragment() {
        if (mCurrentFragment != FRAGMENT_CALENDAR) {
            replaceFragment(CalendarFragment())
            mCurrentFragment = FRAGMENT_CALENDAR
        }
    }

    private fun openContactFragment() {
        if (mCurrentFragment != FRAGMENT_CONTACT) {
            replaceFragment(ContactFragment())
            mCurrentFragment = FRAGMENT_CONTACT
        }
    }

    private fun logOut() {
        // Here, you can clear any session or authentication state if needed (e.g., shared preferences)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish() // Close the current activity
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
    }
}