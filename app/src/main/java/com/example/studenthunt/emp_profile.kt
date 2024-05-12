package com.example.studenthunt

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_emp_profile.*
import kotlinx.android.synthetic.main.activity_stu_profile.*

class emp_profile : AppCompatActivity() {
    lateinit var getuid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emp_profile)
        getuid = intent.extras?.get("UIDS").toString()

        val reff = FirebaseDatabase.getInstance().getReference().child("/Employee").child(getuid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            @SuppressLint("SetTextI18n")
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
                    uname_emp.text="Not yet Associated"
                dept_emp.text="Not yet Associated"}else{
                if(mobile.toString().isEmpty()) {
                    name_emp.text = first.toString() + " " + last.toString()
                    num_emp.text = "#********#"
                    loc_emp.text = loc.toString()
                    email_emp.text = email.toString()
                    uname_emp.text=un.toString()
                    dept_emp.text=dept.toString()
                    Picasso.get().load(Uri.toString()).into(pic_emp)
  //                  Picasso.get().load(cv.toString()).into(coverimage_student)
                }
                else{
                    name_emp.text = first.toString() + " " + last.toString()
                    num_emp.text = mobile.toString()
                    loc_emp.text = loc.toString()
                    email_emp.text = email.toString()
                    uname_emp.text=un.toString()
                    dept_emp.text=dept.toString()
                    Picasso.get().load(Uri.toString()).into(pic_emp)
    //                Picasso.get().load(cv.toString()).into(coverimage_student)
                }}



            }
        })
        chat.setOnClickListener {
            val chat = Intent(this, chat_log::class.java)
            chat.putExtra("UID", getuid)
            startActivity(chat)

        }

    }
}
