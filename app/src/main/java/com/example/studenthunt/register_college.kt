package com.example.studenthunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.fragment.fragment_home
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register_college.*

class register_college : AppCompatActivity() {
    lateinit var college_uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_college)
college_uid = "abd"
       rediogroup.setOnCheckedChangeListener { group, checkedId ->
           if (checkedId == R.id.radioButton1){college_uid = "AMITY1"}
           if (checkedId == R.id.radioButton2){college_uid = "PDM1"}
           if (checkedId == R.id.radioButton3){college_uid = "MDU1"}
           if (checkedId == R.id.radioButton4){college_uid = "SHARDHA1"}
       }

        pass_condition.setOnClickListener {
            val builder = AlertDialog.Builder(this).setTitle("Conditions For Password")
            val view = layoutInflater.inflate(R.layout.conditions,null)
            builder.setView(view).create().show()
        }
        reg_college2.setOnClickListener{
            performregister()

        }
    }fun performregister(){
        val email = email_college.text.toString()
        val pass = pass_college.text.toString()
        Log.d("Main","adfksjidf:$email")
        if (email.isEmpty()){
            email_college.error = "Enter Email"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_college.error = "Enter Valid Email"
        }
        else if(pass.isEmpty()){
            pass_college.error = "Enter Password"

        }else{
            if(pass.length < 8){
                pass_college.error = "Minimum Length 8"
            }else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener {
                            if (!it.isSuccessful)
                                return@addOnCompleteListener
                            //else if success

                            FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                                    ?.addOnCompleteListener {
                                        if (it.isSuccessful) {

                                            Toast.makeText(
                                                    this,
                                                    "Registered Succesfully Please check your email for verification",
                                                    Toast.LENGTH_LONG
                                            ).show()
                                            val mp = Intent(this, Login_college::class.java)
                                            mp.putExtra("collegeUID", college_uid)
                                            mp.flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(mp)
                                            finish()
                                        }
                                        else
                                        {
                                            Toast.makeText(this,"FAILEED", Toast.LENGTH_LONG).show()
                                        }}
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                        }
            }
        }
    }


}