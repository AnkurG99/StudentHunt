package com.example.studenthunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_chat_student.*
import kotlinx.android.synthetic.main.student_card.view.*

class chat_student : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_student)
        students_chat.adapter = adapter
        students_chat.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val intent  = Intent(this, chat_log::class.java)
            val row = item as latestmsg
       intent.putExtra("UID", row.user!!.uid.toString())
            startActivity(intent)
        }
listenforlatest()
    }
    class latestmsg(val Chatmsg:chatmsg) : Item<GroupieViewHolder>(){
        var user :student? = null

        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView19.text = Chatmsg.text
            val i:String
            if (Chatmsg.fromID == FirebaseAuth.getInstance().currentUser!!.uid)
            {i=Chatmsg.toID}
            else{i=Chatmsg.fromID}
            val ref = FirebaseDatabase.getInstance().getReference("/Students/$i")
            ref.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                  user = snapshot.getValue(student::class.java)
                    viewHolder.itemView.fn.text =user?.firstname
                    viewHolder.itemView.ln.text = user?.lastname
                    Picasso.get().load(user?.profileimageurl).into(viewHolder.itemView.profile_student)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }

        override fun getLayout(): Int {
            return R.layout.student_card
        }

    }
    val latestmap = HashMap<String,chatmsg>()
    private fun refresh(){
        adapter.clear()
        latestmap.values.forEach{
            adapter.add(latestmsg(it))
        }
    }
    private fun listenforlatest(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest_messages/$uid")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chats = snapshot.getValue(chatmsg::class.java)?: return
            latestmap[snapshot.key!!] = chats
            refresh()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chats = snapshot.getValue(chatmsg::class.java)?: return
                latestmap[snapshot.key!!] = chats
                refresh()  }
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
    val adapter = GroupAdapter<GroupieViewHolder>()

}