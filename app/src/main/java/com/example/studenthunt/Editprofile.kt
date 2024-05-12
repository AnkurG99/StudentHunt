package com.example.studenthunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editprofile.*


class Editprofile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)
buttonfin_coll.setOnClickListener {
    uploadtodatabase()
    val acc = Intent(this, Dashboard::class.java)
    startActivity(acc)
    finish()
}
val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val reff = FirebaseDatabase.getInstance().reference.child("/College").child(uid)
        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val uri = snapshot.child("/profileimageurl").value
                val age = snapshot.child("/age").value
                val cit = snapshot.child("/city").value


                firstNamec.setText(first.toString())
                lastNamec.setText(last.toString())
                ageGetc.setText(age.toString())

                Picasso.get().load(uri.toString()).placeholder(R.drawable.account).error(R.mipmap.ic_launcher).into(updatephoto)

                cityc.setText(cit.toString())

            }
        })
    }
    private fun uploadtodatabase(){

        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/College/$uid")

        ref.child("/firstname").setValue(firstNamec.text.toString())
        ref.child("/lastname").setValue(lastNamec.text.toString())
        ref.child("/age").setValue(ageGetc.text.toString())
        ref.child("/city").setValue(cityc.text.toString())


                .addOnSuccessListener {
                    Toast.makeText(this, "DATA UPDATED", Toast.LENGTH_LONG).show()
                    Log.d("Main", "Fianlly save a users")
                }
                .addOnFailureListener{
                    Log.d("Main", "Message ${it.message}")
                }

    }
}