package com.example.studenthunt.fragment

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.*
import com.example.studenthunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.squareup.picasso.Picasso


class fragment_department : Fragment() {
lateinit var homeview: View
 lateinit var expand:ConstraintLayout
 lateinit var press: Button
 lateinit var card:CardView
    lateinit var expand2:ConstraintLayout
    lateinit var press2: Button
    lateinit var cuid:String
    private lateinit var nameto:String
    lateinit var list2:RecyclerView
    lateinit var ref2:DatabaseReference
    lateinit var card2:CardView
    lateinit var refg:DatabaseReference
    private lateinit var add:RecyclerView
private lateinit var ref:DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_department, container, false)
cuid  =FirebaseAuth.getInstance().currentUser!!.uid
list2  = homeview.findViewById(R.id.recyclerView2)
        expand = homeview.findViewById(R.id.expand)
        press = homeview.findViewById(R.id.press)
card = homeview.findViewById(R.id.cardView)
add= homeview.findViewById(R.id.recycler_emp)

        expand2 = homeview.findViewById(R.id.expand2)
        press2 = homeview.findViewById(R.id.press2)
        card2 = homeview.findViewById(R.id.cardView2)
        val ref = FirebaseDatabase.getInstance().getReference("/College/$cuid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
val data1 = snapshot.child("/firstname").value
                val data2 = snapshot.child("/lastname").value
                nameto = data1.toString()+" "+data2.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
    }
}
        press2.setOnClickListener {
            if(expand2.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(card2,  AutoTransition())
                expand2.visibility = View.VISIBLE
                press2.setBackgroundResource(R.drawable.shlogon)
            }
            else{
                TransitionManager.beginDelayedTransition(card2,  AutoTransition())
                expand2.visibility = View.GONE
                press2.setBackgroundResource(R.drawable.shlogo)
            }
        }

        return homeview


    } fun search(text : String){
        ref = FirebaseDatabase.getInstance().getReference("/Employees")

        val query = ref.orderByChild("/email").startAt(text).endAt(text+"\uf8ff")


//initialize recycler view


        val option = FirebaseRecyclerOptions.Builder<model>()
                .setQuery(query, model::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<model, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: model) {



                val refid = getRef(position).key.toString()
                ref.child(refid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            Log.d("Message", it.toString())
                        }

                        holder.itemView.setOnClickListener{
                            val user_id = getRef(position).key
                            addusertocollege(user_id.toString(),holder)

                        }
                        holder.username.text = model.firstname
                        holder.uid.text= model.email
                        holder.lastname.text =model.lastname
                        Picasso.get().load(model.profileimageurl).placeholder(R.drawable.ic_launcher_background).error(
                                R.drawable.ic_launcher_foreground).into(holder.image)
                    }


                })
            }


        }
        //assigning adapter to recycler view
        this.add.layoutManager
        this.add.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()




    }

    override fun onStart() {
        super.onStart()

    ref = FirebaseDatabase.getInstance().getReference("/Employees")
 val option = FirebaseRecyclerOptions.Builder<model>()
                .setQuery(ref, model::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<model, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: model) {
                val refid = getRef(position).key.toString()
                Log.d("refidsfd", refid)

                ref.child(refid).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            Log.d("Message", it.toString())
                        }

                        holder.itemView.setOnClickListener{
                            val stu_id = getRef(position).key
addusertocollege(stu_id.toString(),holder)
                        }
                        holder.username.text = model.firstname
                        holder.lastname.text = model.lastname
                        holder.uid.text = model.email
                        Picasso.get().load(model.profileimageurl).placeholder(R.drawable.ic_launcher_background).error(
                                R.drawable.ic_launcher_foreground).into(holder.image)
                    }


                })
            }


        }

        //assigning adapter to recycler view
        this.add.layoutManager
        this.add.adapter = FirebaseRecyclerAdapter
     FirebaseRecyclerAdapter.startListening()






}
    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.textView2fn)!!
        var uid : TextView = v.findViewById(R.id.textView3in)
        var lastname : TextView = v.findViewById(R.id.textView20ln)
        var image : ImageView = v.findViewById(R.id.imageView)
        var added:TextView  = v.findViewById(R.id.textView3)
    }

private fun addusertocollege(getid:String , holder:CustomViewHodler)
{
    val reff = FirebaseDatabase.getInstance().getReference("/Employees/$getid")
    reff.addListenerForSingleValueEvent(object :ValueEventListener
    {
        override fun onDataChange(snapshot: DataSnapshot) {
      val  data = snapshot.getValue(model::class.java)
            val usertoadd = model(
                    getid,
                    data!!.firstname,
                    data.lastname,
                    data.email,
                    data.number,
                    data.age,
                    data.city,
                    data.profileimageurl,
                    "Admission",
                    "YES",
                    nameto.toString(),
                    cuid
            )
            refg = FirebaseDatabase.getInstance().getReference("/College/").child(cuid).child("/department/$getid")
            refg.setValue(usertoadd)
                    .addOnSuccessListener {
                        Log.d("Main", "Fianlly save a users")
                    }
                    .addOnFailureListener{
                        Log.d("Main", "Message ${it.message}")
                    }
            reff.child("/university_name").setValue(nameto)
            reff.child("/university_uid").setValue(cuid)
            holder.added.visibility  =View.VISIBLE
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })

}
}
