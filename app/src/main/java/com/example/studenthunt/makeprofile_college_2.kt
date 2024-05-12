package com.example.studenthunt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_makeprofile.*
import kotlinx.android.synthetic.main.activity_makeprofile_college_2.*
import java.util.*

class makeprofile_college_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_makeprofile_college_2)


        button2_cv.setOnClickListener {
            Log.d("Main", "Try to upload image")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        buttonfin.setOnClickListener {
            if(tagline.text.toString().isEmpty()){
                tagline.error= "Enter tagline"
            }
            else{

                //  Uri.parse("android.resource://$packageName/${R.drawable.shlogo}")

                uploadimage()


                val dash = Intent(this, Dashboard::class.java)
                dash.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(dash)
                finish()


            }
        }
    }  private var selectedPhotoUris: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("Main", "Photo was selected")

            selectedPhotoUris = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUris)
            selectphoto23.setImageBitmap(bitmap)
            button2_cv.alpha = 0f
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
            uploadtodatabase(coverimageurl = uri.toString())

        }
    }
    private fun uploadtodatabase(coverimageurl:String){

        val uid = FirebaseAuth.getInstance().currentUser?.uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/College/$uid")
ref.child("/tagline").setValue(tagline.text.toString())
        ref.child("/web").setValue((web_coll.text.toString()))
        ref.child("/number").setValue(mobile_coll.text.toString())
        ref.child("/coverimageurl").setValue(coverimageurl)


    }
}