package com.example.studenthunt

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login_college.*

class Login_college : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_college)
        rpc.setOnClickListener {
            val build =   AlertDialog.Builder(this).setTitle("Reset Password")
                .setMessage("Enter email to get reset password link")

            val v = layoutInflater.inflate(R.layout.pass_reset_dialog, null)
            val id = v.findViewById<EditText>(R.id.dialog)

            build.setView(v)
            build.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which -> Int
                forgotpassword(id)
            })
            build.setNegativeButton("close", DialogInterface.OnClickListener { dialog, which -> Int
                dialog.dismiss()
            }).create().show()
        }
        registerc.setOnClickListener {
            val reg = Intent(this, Register::class.java)
            startActivity(reg)
            finish()
        }


        loginButtonc.setOnClickListener {

            performlogincollege()

        }
    }   fun forgotpassword(id: EditText){
        if (id.text.toString().isEmpty()) {
            id.error = "Enter Email"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(id.text.toString()).matches()){
            id.error = "Enter Valid Email"


            FirebaseAuth.getInstance().sendPasswordResetEmail(id.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Check your mail for reset link", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun performlogincollege(){
        if (email_loginc.text.toString().isEmpty()) {
            email_loginc.error = "Enter Email"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email_loginc.text.toString()).matches()){
            email_loginc.error = "Enter Valid Email"
        }
        else if (Pass_loginc.text.toString().isEmpty()) {
            Pass_loginc.error = "Enter Password"
        } else {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email_loginc.text.toString(),
                Pass_loginc.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {

                    if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                        uploadtodbcollege()

                        val dash = Intent(this,Makeprofile::class.java)
                        dash.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(dash)
                        finish()
                        emailch = email_loginc.text.toString()
                        sharedPreferences =
                            getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("Email", email_loginc.text.toString())
                        editor.putString("Pass", Pass_loginc.text.toString())
                        editor.putBoolean("Key_Name", true)

                        editor.commit()

                    } else {
                        Toast.makeText(this, "Verify Email", Toast.LENGTH_LONG).show()
                    }


                }
            }.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
            }

        }

    }fun uploadtodbcollege(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/College/$uid")

        val user = college("","",uid,"","",email_loginc.text.toString(),"","","","","","","","","")
        ref.setValue(user)
            .addOnSuccessListener {
                val getcollege:String  = intent.extras?.get("collegeUID").toString()

                Log.d("Main", "Fianlly save a users")
                if (getcollege != "abd")
                {    val ref = FirebaseDatabase.getInstance().getReference("/College")
                ref.child(getcollege).removeValue()}
            }
            .addOnFailureListener{
                Log.d("Main", "Message ${it.message}")
            }
    }
}