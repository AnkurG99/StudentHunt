package com.example.studenthunt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_makeprofile.*
import kotlinx.android.synthetic.main.activity_makeprofile_user.*
import kotlinx.android.synthetic.main.activity_makeprofile_user.button2
import java.util.*

class Makeprofile : AppCompatActivity() {
    lateinit var e:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeprofile)
        val user = FirebaseAuth.getInstance().currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("College").child(user)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                 e = snapshot.child("/email").value as String


            }

        })
        button2_coll.setOnClickListener {
            Log.d("Main", "Try to upload image")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        buttoncon2.setOnClickListener {
            if(firstName.text.toString().isEmpty()){
                firstName.error= "Enter First Name"
            }else if(lastName.text.toString().isEmpty()){
                lastName.error = "Enter Last Name"
            }else if(age_org.text.toString().isEmpty()){
                age_org.error = "Enter Age"
            }else if(city_college.text.toString().isEmpty()){
                city_college.error = "Enter City"
            }
            else{

                    //  Uri.parse("android.resource://$packageName/${R.drawable.shlogo}")

                    uploadimage()


                    val dash = Intent(this, makeprofile_college_2::class.java)
                    dash.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(dash)
                    finish()


            }
        }
    }
    private var selectedPhotoUris: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Main", "Photo was selected")

            selectedPhotoUris = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUris)
            selectphoto2.setImageBitmap(bitmap)
            button2_coll.alpha = 0f
        }
    }
    private fun uploadimage(){

        if(selectedPhotoUris != null) {
            val filename = UUID.randomUUID()
            val ref = FirebaseStorage.getInstance().getReference("/images/${filename}")
            ref.putFile(selectedPhotoUris!!)
                    .addOnSuccessListener { it ->
                        Log.d("Main", "Succesfully Uploaded image : ${it.metadata?.path}")

                        ref.downloadUrl.addOnSuccessListener {

                            Log.d("Main", "File Location $it")
                            uploadtodatabase(it.toString())

                        }
                    }
        }
        else {
            val uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/college-hunt-aab15.appspot.com/o/images%2Fperson-icon-male-user-profile-avatar-vector-18833568.jpg?alt=media&token=0f764ff1-8d3e-40cd-998e-e3f60ce84d3c")
            uploadtodatabase(profileimageUrl = uri.toString())

        }
    }
    private fun uploadtodatabase(profileimageUrl:String){

        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/College/$uid")
val user = college("",city_college.text.toString(),uid,firstName.text.toString(),lastName.text.toString(),e,"",age_org.text.toString(),profileimageUrl,"","","","","","")
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Main", "Fianlly save a users")
                }
                .addOnFailureListener{
                    Log.d("Main", "Message ${it.message}")
                }

    }
}
