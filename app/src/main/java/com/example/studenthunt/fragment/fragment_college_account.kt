package com.example.studenthunt.fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.Editprofile
import com.example.studenthunt.Login_Logout
import com.example.studenthunt.R
import com.example.studenthunt.course
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.paging.FirebaseDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class fragment_college_account : Fragment() {
    private lateinit var homeview:View
private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sv:EditText
    private lateinit var cp :ImageView
    private lateinit var pp:ImageView
    private lateinit var name:TextView
    private lateinit var web:TextView
    private lateinit var phone:TextView
    private lateinit var loc:TextView
    private lateinit var courseslist:RecyclerView
    private lateinit var desc:TextView
    private lateinit var uid:String
    private lateinit var tag:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         homeview = inflater.inflate(R.layout.fragment_college_account, container, false)
val logout = homeview.findViewById<TextView>(R.id.logout_coll);logout.setOnClickListener {
    FirebaseAuth.getInstance().signOut()
    sharedPreferences = this.requireActivity().getSharedPreferences("MYPREF", Activity.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("Key_Name", false)
    editor.apply()
    val login = Intent(this.activity, Login_Logout::class.java)
    startActivity(login)
    requireActivity().finish() }
        val edit = homeview.findViewById<Button>(R.id.button_college);edit.setOnClickListener { 
            val edit2= Intent(this.activity,Editprofile::class.java)
            startActivity(edit2)
        }
        sv = homeview.findViewById(R.id. editTextTextPersonName3)
        cp = homeview.findViewById(R.id. cv_college)
        pp = homeview.findViewById(R.id. getpic_college)
        tag = homeview.findViewById(R.id.textView8)
        name = homeview.findViewById(R.id. name_college)
        web = homeview.findViewById(R.id. web_college)
        phone= homeview.findViewById(R.id. phone_college)
        courseslist = homeview.findViewById(R.id. show_courses)
        desc = homeview.findViewById(R.id. desc_college)
        loc = homeview.findViewById(R.id.loc_college)
         uid = FirebaseAuth.getInstance().currentUser!!.uid
        val reff = FirebaseDatabase.getInstance().getReference("/College").child(uid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val webc = snapshot.child("/web").value
                val first = snapshot.child("/firstname").value
                val last = snapshot.child("/lastname").value
                val mobile = snapshot.child("/number").value
                val Uri = snapshot.child("/profileimageurl").value
                val locc = snapshot.child("/city").value
               val cv = snapshot.child("/coverimageurl").value
              val descp = snapshot.child("/description").value
                val tagline = snapshot.child("/tagline").value


                    if(mobile.toString().isEmpty()) {
desc.text= descp.toString()
                        web.text  = webc.toString()
                        name.text = first.toString()+ " " + last.toString()
                        phone.text = mobile.toString()
                        loc.text = locc.toString()
                        tag.text= tagline.toString()


                        Picasso.get().load(Uri.toString()).into(pp)
                                          Picasso.get().load(cv.toString()).into(cp)
                    }
                    else{

                        desc.text= descp.toString()
                        web.text  = webc.toString()
                        name.text = first.toString()+ " " + last.toString()
                        phone.text = mobile.toString()
                        loc.text = locc.toString()
                        tag.text= tagline.toString()


                        Picasso.get().load(Uri.toString()).into(pp)
                        Picasso.get().load(cv.toString()).into(cp)

                    }




            }
        })


        return homeview
    }

    override fun onStart() {
        super.onStart()
      val  ref = FirebaseDatabase.getInstance().getReference("/College").child(uid).child("/courses")
        val option = FirebaseRecyclerOptions.Builder<course>()
            .setQuery(ref, course::class.java).build()
//adapter
        val FirebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<course, CustomViewHodler>(option) {
                //onCreate
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): CustomViewHodler {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val cellforrow = layoutInflater.inflate(R.layout.card_course, parent, false)
                    return CustomViewHodler(cellforrow)
                }


                //onBind
                override fun onBindViewHolder(
                    holder: CustomViewHodler,
                    position: Int,
                    model: course
                ) {
                    val refid = getRef(position).key.toString()
                    ref.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach {
                                Log.d("Message", it.toString())
                            }

                            holder.username.text = model.id

                        }

                    })
                }


            }
        //assigning adapter to recycler view
        courseslist.layoutManager
        this.courseslist.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()
    } class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        var username: TextView = v.findViewById(R.id.textView17)

    }

}