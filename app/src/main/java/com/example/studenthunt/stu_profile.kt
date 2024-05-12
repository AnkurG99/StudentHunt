package com.example.studenthunt

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_stu_profile.*

class stu_profile : AppCompatActivity() {
    lateinit var getuid:String
    lateinit var uidstu:String
    lateinit var ref:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stu_profile)

        getuid = intent.extras?.get("UIDS").toString()

        val reff = FirebaseDatabase.getInstance().getReference().child("/Students").child(getuid)
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
                val interest = snapshot.child("/interesr").value
                val cv = snapshot.child("/coverimage").value
if(mobile.toString().isEmpty()) {
    name_student.text = first.toString() + " " + last.toString()
    mobile_student.text = "#********#"
    city_student.text = loc.toString()
    interst_student.text = interest.toString()
    email_student.text = email.toString()
    Picasso.get().load(Uri.toString()).into(pic_stu_profile)
    Picasso.get().load(cv.toString()).into(coverimage_student)
}
                else{
    name_student.text = first.toString() + " " + last.toString()
    mobile_student.text = mobile.toString()
    city_student.text = loc.toString()
    interst_student.text = interest.toString()
    email_student.text = email.toString()
    Picasso.get().load(Uri.toString()).into(pic_stu_profile)
    Picasso.get().load(cv.toString()).into(coverimage_student)
                }

//Student name in bar
                supportActionBar?.title = first.toString() + " " + last.toString()

            }
        })
chat.setOnClickListener {
val chat = Intent(this,chat_log::class.java)
    chat.putExtra("UID", getuid)
    startActivity(chat)

}



}
    }