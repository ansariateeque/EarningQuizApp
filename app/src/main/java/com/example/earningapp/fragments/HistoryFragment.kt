package com.example.earningapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earningapp.R
import com.example.earningapp.adapter.HistoryAdapter
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.models.HistoryModel
import com.example.earningapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class HistoryFragment : Fragment() {

    lateinit var binding: FragmentHistoryBinding
    lateinit var database: FirebaseDatabase
    lateinit var arrayList: ArrayList<HistoryModel>
    var playerwinAmount = 0L
    lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        arrayList = ArrayList()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        database.getReference().child("ForHistory").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        arrayList.clear()
                        for (snapshot in snapshot.children) {
                            var historyModel: HistoryModel? =
                                snapshot.getValue<HistoryModel>(HistoryModel::class.java)
                            if (historyModel != null) {
                                arrayList.add(historyModel)
                                Log.d(
                                    "Tage",
                                    "onDataChange: ${historyModel.rupees} ${historyModel.timeanddate}"
                                )
                                arrayList.reverse()
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                })


        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryAdapter(arrayList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        binding.coinwithdrawal1.setOnClickListener(
            View.OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(requireActivity().supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )
        binding.coinwithdrawal2.setOnClickListener(
            View.OnClickListener {
                var withdrawal = Withdrawal()
                withdrawal.show(requireActivity().supportFragmentManager, "Text")
                withdrawal.enterTransition

            }
        )

        database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid)
            .child("Value")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    playerwinAmount = snapshot.value as Long
                    binding.coinwithdrawal2.text = playerwinAmount.toString()
                }
            })
        database.getReference().child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var users = snapshot.getValue<Users>(Users::class.java)
                        binding.name.text = users?.name
                        if (users?.profile != "") {
                            Picasso.get().load(users?.profile).placeholder(R.drawable.profile)
                                .into(binding.profileImage)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    //
                    }
                })

    }

    companion object {

    }
}