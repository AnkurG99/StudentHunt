package com.example.studenthunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sign_in_reg.setOnClickListener{
            val ll = Intent(this, Login_Logout::class.java)
            startActivity(ll)
            finish()
        }
        condition.setOnClickListener {
            val builder = AlertDialog.Builder(this).setTitle("Conditions For Password")
            val view = layoutInflater.inflate(R.layout.conditions,null)
            builder.setView(view).create().show()
        }
        registerButton.setOnClickListener{
            performregister()

        }
        reg_college.setOnClickListener {
            val mp = Intent(this,register_college::class.java)
            startActivity(mp)
            finish()
        }
    }
    private fun performregister(){
        val email = email_reg.text.toString()
        val pass = pass_reg.text.toString()
        Log.d("Main","adfksjidf:$email")
        if (email.isEmpty()){
            email_reg.error = "Enter Email"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_reg.error = "Enter Valid Email"
        }
        else if(pass.isEmpty()){
            pass_reg.error = "Enter Password"

        }else{
            if(pass.length < 8){
                pass_reg.error = "Minimum Length 8"
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
                                    val mp = Intent(this, Login::class.java)
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
