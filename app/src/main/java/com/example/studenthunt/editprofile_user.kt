package com.example.studenthunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editprofile_user.*

class editprofile_user : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile_user)


        buttoncone.setOnClickListener{

            uploadtodatabase()
            val acc = Intent(this, Dashboard::class.java)
            startActivity(acc)
            finish()
        }
        val user  = FirebaseAuth.getInstance().currentUser!!.uid

        val reff = FirebaseDatabase.getInstance().reference.child("/Employees").child(user)
        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val info = snapshot.child("/email").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val m = snapshot.child("/number").value
                val uri = snapshot.child("/profileimageurl").value
                val age = snapshot.child("/age").value
                val cit = snapshot.child("/city").value

                mobilee.setText(m.toString())
                firstNamee.setText(first.toString())
                lastNamee.setText(last.toString())
                ageGete.setText(age.toString())
                emaile.setText(info.toString())
                Picasso.get().load(uri.toString()).placeholder(R.drawable.account).error(R.mipmap.ic_launcher).into(updatephoto)
                intereste.setText(cit.toString())

            }
        })

    }
    private fun uploadtodatabase(){

        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/Employees/$uid")

        ref.child("/firstname").setValue(firstNamee.text.toString())
        ref.child("/lastname").setValue(lastNamee.text.toString())
        ref.child("/age").setValue(ageGete.text.toString())
        ref.child("/interesr").setValue(intereste.text.toString())
        ref.child("/number").setValue(mobilee.text.toString())


                .addOnSuccessListener {
                    Toast.makeText(this, "DATA UPDATED", Toast.LENGTH_LONG).show()
                    Log.d("Main", "Fianlly save a users")
                }
                .addOnFailureListener{
                    Log.d("Main", "Message ${it.message}")
                }

    }


}