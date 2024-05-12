package com.example.studenthunt.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.squareup.picasso.Picasso
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.studenthunt.*
import com.example.studenthunt.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class fragment_home : Fragment() {
    private lateinit var student_list: RecyclerView
    lateinit var ref: DatabaseReference
    lateinit var sv: EditText
lateinit var homeview:View
lateinit var fab:FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview =inflater.inflate(R.layout.fragment_home, container, false)
fab = homeview.findViewById(R.id.floatingActionButton1)
        fab.setOnClickListener {
            val chat = Intent(this.activity, chat_student::class.java)
            startActivity(chat)
        }
        student_list =homeview.findViewById(R.id.students)
        student_list.layoutManager
        student_list.addItemDecoration(
                DividerItemDecoration(
                        this.activity,
                        DividerItemDecoration.VERTICAL
                )
        )

        sv =homeview.findViewById(R.id.search_student)
        sv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = sv.text.toString()
               search(text)
            }

            override fun afterTextChanged(s: Editable?) {
                val texta = sv.text.toString()
              search(texta)
                }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val  ref = FirebaseDatabase.getInstance().getReference("/Students")

        val option = FirebaseRecyclerOptions.Builder<student>()
                .setQuery(ref, student::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<student, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.student_card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: student) {
                val refid = getRef(position).key.toString()
                Log.d("refidsfd", refid)
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
                            val op = Intent(context, stu_profile::class.java)
                            op.putExtra("UIDS",stu_id)
                            startActivity(op)
                        }
                        holder.username.text = model.firstname
                        holder.uid.text= model.interesr
                        holder.lastname.text = model.lastname
                        Picasso.get().load(model.profileimageurl).placeholder(R.drawable.ic_launcher_background).error(
                                R.drawable.ic_launcher_foreground).into(holder.image)
                    }


                })
            }


        }
        //assigning adapter to recycler view
        this.student_list.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()
        return homeview
    }

  fun search(text : String){
        ref = FirebaseDatabase.getInstance().getReference("/Students")

        val query = ref.orderByChild("/firstname").startAt(text).endAt(text+"\uf8ff")


//initialize recycler view


        val option = FirebaseRecyclerOptions.Builder<student>()
                .setQuery(query,student::class.java).build()
//adapter
        val FirebaseRecyclerAdapter = object :  FirebaseRecyclerAdapter<student, CustomViewHodler>(option){
            //onCreate
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
                val layoutInflater = LayoutInflater.from(parent.context)
                val cellforrow = layoutInflater.inflate(R.layout.student_card, parent, false)
                return CustomViewHodler(cellforrow)
            }


            //onBind
            override fun onBindViewHolder(holder: CustomViewHodler, position: Int, model: student) {



                val refid = getRef(position).key.toString()
                ref.child(refid).addValueEventListener(object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            Log.d("Message", it.toString())
                        }

                       holder.itemView.setOnClickListener{
                            val user_id = getRef(position).key
                            val cp = Intent(context, stu_profile::class.java)
                            cp.putExtra("UID",user_id)
                            startActivity(cp)
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
        this.student_list.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()




    }


    class CustomViewHodler(v: View) : RecyclerView.ViewHolder(v){
        var username : TextView = v.findViewById(R.id.fn)!!
        var uid : TextView = v.findViewById(R.id.`in`)
        var lastname : TextView = v.findViewById(R.id.ln)
        var image : ImageView = v.findViewById(R.id.profile_student)
    }

    }

