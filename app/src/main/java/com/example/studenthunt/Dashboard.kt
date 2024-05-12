package com.example.studenthunt

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_makeprofile_user.*
import kotlinx.android.synthetic.main.nav_header_main.*


class Dashboard : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("Employees").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                 val e = snapshot.child("/employee").value
val fn = snapshot.child("/firstname").value
                val ln = snapshot.child("/lastname").value
                val un = snapshot.child("/university_name").value
                val piu  = snapshot.child("/profileimageurl").value
                if (e == "YES"){
                    hideItem()
                    namtoshow.text = fn.toString() + " " + ln.toString()
                    emailtoshow.text = un.toString()
                    Picasso.get().load(piu.toString()).into(imageViewtoshow)
                }
                else{
                    hideItem2()
                    val reg = FirebaseDatabase.getInstance().getReference("/College").child(uid)
                    reg.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val fn1 = snapshot.child("/firstname").value
                            val ln1 = snapshot.child("/lastname").value
                            val un1 = snapshot.child("/tagline").value
                            val piu1  = snapshot.child("/profileimageurl").value
                            namtoshow.text = fn1.toString() + " " + ln1.toString()
                            emailtoshow.text = un1.toString()
                            Picasso.get().load(piu1.toString()).into(imageViewtoshow)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

        })


        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_department,R.id.nav_chat, R.id.nav_add_info,R.id.nav_customize, R.id.nav_profile, R.id.nav_profile_college), drawerLayout)
        this.setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    private fun hideItem()
    {
        val navigationView:NavigationView = findViewById(R.id.nav_view)
        val nav_Menu: Menu = navigationView.menu
        nav_Menu.findItem(R.id.nav_profile_college).isVisible = false
        nav_Menu.findItem(R.id.nav_department).isVisible = false
    }
    private fun hideItem2()
    {
        val navigationView:NavigationView = findViewById(R.id.nav_view);
        val nav_Menu:Menu = navigationView.menu;
        nav_Menu.findItem(R.id.nav_profile).isVisible = false;
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}