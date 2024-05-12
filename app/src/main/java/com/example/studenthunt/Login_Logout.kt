package com.example.studenthunt

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login__logout.*

class Login_Logout : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login__logout)
        forgotpass_ll.setOnClickListener {
            val build =   AlertDialog.Builder(this).setTitle("Reset Password")
                    .setMessage("Enter email to get reset password link")

            val v = layoutInflater.inflate(R.layout.pass_reset_dialog, null)
            val id = v.findViewById<EditText>(R.id.dialog)

            build.setView(v)
            build.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which -> Int

                forgotpassword(id)
                dialog.dismiss()
            })
            build.setNegativeButton("close", DialogInterface.OnClickListener { dialog, which -> Int
                dialog.dismiss()
            }).create().show()
        }

        textView10.setOnClickListener {
            val log = Intent(this, Register::class.java)
            startActivity(log)
            finish()
        }
        button_ll.setOnClickListener{
            if(email_ll.text.toString().isEmpty()){
                email_ll.error = "Enter Email"
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email_ll.text.toString()).matches()){
                email_ll.error = "Enter Valid Email"
            }
            else if(pass_ll.text.toString().isEmpty()){
                pass_ll.error = "Enter password"
            }else{
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email_ll.text.toString(),pass_ll.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                            val dash = Intent(this, Dashboard::class.java)
                            dash.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(dash)
                            finish()


                            sharedPreferences = getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("Email",email_ll.text.toString())
                            editor.putString("Pass", pass_ll.text.toString())
                            editor.putBoolean("Key_Name", true)

                            editor.commit()


                        }}
                }.addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                }

            }
        }
    }    fun forgotpassword(id: EditText){
        if (id.text.toString().isEmpty()) {
            id.error = "Enter Email"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(id.text.toString()).matches()) {
            id.error = "Enter Valid Email"

        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(id.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Check your mail for reset link", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                }
    }
}