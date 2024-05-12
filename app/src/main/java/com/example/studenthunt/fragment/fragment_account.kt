package com.example.studenthunt.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.studenthunt.R
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.studenthunt.Login_Logout
import com.example.studenthunt.editprofile_user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_emp_profile.*
import kotlinx.android.synthetic.main.fragment_account.*

class fragment_account : Fragment() {
private lateinit var homeview:View
lateinit var getuid:String
lateinit var sharedPreferences: SharedPreferences
    lateinit var nameuser:TextView
    lateinit var emailuser:TextView
    lateinit var numuser:TextView
    lateinit var uniuser:TextView
    lateinit var deptuser:TextView
    lateinit var locuser:TextView
    lateinit var pimage:ImageView
    lateinit var cvimage:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_account, container, false)
        val edit = homeview.findViewById<Button>(R.id.edit_button)
        edit.setOnClickListener {
            val edit2 = Intent(this.requireActivity(), editprofile_user::class.java)
            startActivity(edit2)

        }
        val logout = homeview.findViewById<TextView>(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPreferences = this.requireActivity().getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putBoolean("Key_Name", false)

            editor.apply()

            val login = Intent(this.activity,Login_Logout::class.java)
            startActivity(login)
            requireActivity().finish()

        }
        getuid = FirebaseAuth.getInstance().currentUser!!.uid
        Log.d("UID", getuid)
        nameuser = homeview.findViewById(R.id.name_account)
        emailuser = homeview.findViewById(R.id.email_account)
        locuser = homeview.findViewById(R.id.loc_account)
        uniuser = homeview.findViewById(R.id.uname_account)
        deptuser = homeview.findViewById(R.id.dept_account)
        numuser = homeview.findViewById(R.id.num_account)
        pimage = homeview.findViewById(R.id.pic_account)
        val reff = FirebaseDatabase.getInstance().reference.child("/Employees").child(getuid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val mobile = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val loc = snapshot.child("/city").value
//                val cv = snapshot.child("/coverimage").value
                val un = snapshot.child("/university_name").value
                val dept = snapshot.child("/university_uid").value
                if(un.toString().isEmpty()&&dept.toString().isEmpty()){
                    uniuser.text="Not yet Associated"
                    deptuser.text="Not yet Associated"
                    if(mobile.toString().isEmpty()) {
                        nameuser.text = first.toString() + " " + last.toString()
                        numuser.text = "#********#"
                        locuser.text = loc.toString()
                        emailuser.text = email.toString()
                        Picasso.get().load(Uri.toString()).into(pimage)
                                    //    Picasso.get().load(cv.toString()).into(coverimage_student)
                    }
                    else{
                        nameuser.text = first.toString() + " " + last.toString()
                        numuser.text = mobile.toString()
                        locuser.text = loc.toString()
                        emailuser.text = email.toString()
                        Picasso.get().load(Uri.toString()).into(pimage)
                        //                Picasso.get().load(cv.toString()).into(coverimage_student)
                    }
                }else{
                    if(mobile.toString().isEmpty()) {
                        nameuser.text = first.toString() + " " + last.toString()
                        numuser.text = "#********#"
                        locuser.text = loc.toString()
                        emailuser.text = email.toString()
                        uniuser.text=un.toString()
                        deptuser.text=dept.toString()
                        Picasso.get().load(Uri.toString()).into(pimage)
                        //                  Picasso.get().load(cv.toString()).into(coverimage_student)
                    }
                    else{
                        nameuser.text = first.toString() + " " + last.toString()
                        numuser.text = mobile.toString()
                        locuser.text = loc.toString()
                        emailuser.text = email.toString()
                        uniuser.text=un.toString()
                        deptuser.text=dept.toString()
                        Picasso.get().load(Uri.toString()).into(pimage)
                        //                Picasso.get().load(cv.toString()).into(coverimage_student)
                    }}



            }
        })

        return homeview
    }

}