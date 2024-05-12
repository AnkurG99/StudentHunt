package com.example.studenthunt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.*
import com.example.studenthunt.fragment.fragment_chat
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_emp.*

class chat_emp : AppCompatActivity() {
lateinit var togo:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_emp)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
       togo = "chat_log_emp"
        val reff = FirebaseDatabase.getInstance().getReference("/Employees").child(uid)
        reff.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              val  e = snapshot.child("/university_uid").value.toString()
          val em = snapshot.child("/employee")
                var ref:DatabaseReference
if (em.equals("YES")){
    ref = FirebaseDatabase.getInstance().getReference("/College").child(e).child("/department")
    togo = "chat_log_emp"

}
                else{
    ref = FirebaseDatabase.getInstance().getReference("/College").child(uid).child("/department")
togo = "chta_log_coll"
}
                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            Log.d("CHECK",it.toString())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                val option = FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(ref, model::class.java).build()
//adapter
                val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<model,CustomViewHodler>(option){
                    //onCreate
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                        val layoutInflater = LayoutInflater.from(parent.context)
                        val cellforrow = layoutInflater.inflate(R.layout.card, parent, false)
                        return CustomViewHodler(cellforrow)
                    }


                    //onBind
                    override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: model) {
                        val refid = getRef(position).key.toString()
                        Log.d("refi", refid)
                        if(refid == uid){
                            holder.setIsRecyclable(false);
                            holder.itemView.setVisibility(View.GONE)
                            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)

                            return;
                        }else
                        {
                            holder.setIsRecyclable(true);
                            holder.itemView.setVisibility(View.VISIBLE)
                            // holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                        }

                        ref.child(refid).addValueEventListener(object: ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach{
                                    Log.d("Message", it.toString())
                                }

                                holder.itemView.setOnClickListener{
                                    val stu_id = getRef(position).key
                                    val op = Intent(this@chat_emp, togo::class.java)
                                    op.putExtra("UID",stu_id)
                                    startActivity(op)
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
  this@chat_emp.emp.adapter = FirebaseRecyclerAdapter
                FirebaseRecyclerAdapter.startListening()}

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


}
    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.textView2fn)!!
        var uid : TextView = v.findViewById(R.id.textView3in)
        var lastname : TextView = v.findViewById(R.id.textView20ln)
        var image : ImageView = v.findViewById(R.id.imageView)
    }
}