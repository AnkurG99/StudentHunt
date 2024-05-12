package com.example.studenthunt.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.*
import com.example.studenthunt.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_emp.*
import kotlinx.android.synthetic.main.student_card.view.*


class fragment_chat : Fragment() {
    lateinit var e:String
lateinit var homeview:View
lateinit var emp:RecyclerView
lateinit var fab:FloatingActionButton
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_chat, container, false)
emp = homeview.findViewById(R.id.dept_chat)

fab=homeview.findViewById(R.id.floatingActionButton2)
        fab.setOnClickListener {
            val intent = Intent(this.activity, chat_emp::class.java)
            startActivity(intent)
        }
        adapter.setOnItemClickListener { item, view ->
            val intent  = Intent(this.activity, chat_log_emp::class.java)
            val row = item as latestmsg
            intent.putExtra("UID", row.user!!.uid)
            startActivity(intent)
        }
        listenforlatest()
        emp.layoutManager
        emp.adapter = adapter
        return homeview
    }
    class latestmsg(val Chatmsg: chatmsg) : Item<GroupieViewHolder>(){
        var user :model? = null
var user2:college?= null
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView19.text = Chatmsg.text
            val i: String
            if (Chatmsg.fromID == FirebaseAuth.getInstance().currentUser!!.uid) {
                i = Chatmsg.toID
            } else {
                i = Chatmsg.fromID
            }
Log.d("ERROR", i.toString())

         val reg= FirebaseDatabase.getInstance().getReference("/Employees/$i")
           reg.addValueEventListener(object :ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   val uid = snapshot.child("/uid").value
                   Log.d("REG", uid.toString())


                               user = snapshot.getValue(model::class.java)
                               viewHolder.itemView.fn.text = user?.firstname
                               viewHolder.itemView.ln.text = user?.lastname
                               Picasso.get().load(user?.profileimageurl).into(viewHolder.itemView.profile_student)
                           }
                           override fun onCancelled(error: DatabaseError) {
                           }
                       })




        }
        override fun getLayout(): Int {
            return R.layout.student_card
        }

    }
    val latestmap = HashMap<String, chatmsg>()
    private fun refresh(){
        adapter.clear()
        latestmap.values.forEach{
            adapter.add(latestmsg(it))
        }
    } val adapter = GroupAdapter<GroupieViewHolder>()
    private fun listenforlatest(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("/latest_messages_emp/$uid")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chats = snapshot.getValue(chatmsg::class.java) ?: return
                latestmap[snapshot.key!!] = chats
                refresh()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chats = snapshot.getValue(chatmsg::class.java) ?: return
                latestmap[snapshot.key!!] = chats
                refresh()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onStart() {
        super.onStart()


    }
}