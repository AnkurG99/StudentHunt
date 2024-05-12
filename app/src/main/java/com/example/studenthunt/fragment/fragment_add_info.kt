package com.example.studenthunt.fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.R
import com.example.studenthunt.course
import com.example.studenthunt.model
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeoutException


class fragment_add_info : Fragment() {
lateinit var homeview:View
lateinit var press:Button
lateinit var expand:ConstraintLayout
lateinit var card:CardView
lateinit var desc:EditText
lateinit var courses:RecyclerView
lateinit var uid2:String
lateinit var sv:EditText
lateinit var text2:String
lateinit var update:Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_add_info, container, false)
val uid = FirebaseAuth.getInstance().currentUser!!.uid
       press = homeview.findViewById(R.id.press3)
        expand = homeview.findViewById(R.id.expand)
        card = homeview.findViewById(R.id.cardView)
        uid2 = uid
        update=homeview.findViewById(R.id.button_update)
        update.setOnClickListener {
            Update()
        }
        press.setOnClickListener {
            if(expand.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(card,  AutoTransition())
                expand.visibility = View.VISIBLE
                press.setBackgroundResource(R.drawable.shlogon)
            }
            else{
                TransitionManager.beginDelayedTransition(card,  AutoTransition())
                expand.visibility = View.GONE
                press.setBackgroundResource(R.drawable.shlogo)
            }}

            desc = homeview.findViewById(R.id.desc_coll_up)
            courses= homeview.findViewById(R.id.recyclerView_courses)
            sv = homeview.findViewById(R.id.editTextTextPersonName5)
//text2 = desc.text.toString()
val ref = FirebaseDatabase.getInstance().getReference("/Employees/$uid")
            ref.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val e = snapshot.child("/employee").value
                    val u= snapshot.child("/university_uid").value
                    if (e == "YES"){
                        uid2 = u.toString()
                    }else{
                        uid2 = uid
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        return homeview

}private fun Update(){
        val ref2 = FirebaseDatabase.getInstance().getReference("/College/$uid2")
        ref2.child("/description").setValue(desc.text.toString())

    }

    override fun onStart() {
        super.onStart()

       val  ref = FirebaseDatabase.getInstance().getReference("/Courses/courses")
        val option = FirebaseRecyclerOptions.Builder<course>()
            .setQuery(ref, course::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<course, CustomViewHodler>(option) {
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
                    Log.d("refidsfd", refid)

                    ref.child(refid).addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach {
                                Log.d("Message", it.toString())
                            }

                            holder.itemView.setOnClickListener {
                                val stu_id = getRef(position).key
                                addcoursetocollege(stu_id.toString(), holder)
                                holder.add.visibility = View.VISIBLE
                            }
                            holder.username.text = model.id

                        }


                    })
                }



        }

        //assigning adapter to recycler view
        this.courses.layoutManager
        this.courses.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()


    }
    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.textView17)!!
        var add :TextView = v.findViewById(R.id.textView7)

    }
    private fun addcoursetocollege(getid:String, holder:CustomViewHodler){
        val ref1 = FirebaseDatabase.getInstance().getReference("/Courses/courses/$getid")
        ref1.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val coursetoadd= snapshot.getValue(course::class.java)
                val coursegetadd = course(coursetoadd!!.id,coursetoadd.marks)

                val ref = FirebaseDatabase.getInstance().getReference("/College/$uid2").child("/courses/$getid")
                ref.setValue(coursegetadd)
                    .addOnSuccessListener {
                        Log.d("Main", "Fianlly save a users")
                    }
                    .addOnFailureListener{
                        Log.d("Main", "Message ${it.message}")
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

}