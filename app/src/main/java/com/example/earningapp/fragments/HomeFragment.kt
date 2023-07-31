package com.example.earningapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.earningapp.R
import com.example.earningapp.R.layout
import com.example.earningapp.adapter.CategoryAdapter
import com.example.earningapp.databinding.FragmentHomeBinding
import com.example.earningapp.models.CategoryModel
import com.example.earningapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.lang.Integer.getInteger


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    var playerwinAmount = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        arrayList.add(CategoryModel(R.drawable.maths, "Math"))
        arrayList.add(CategoryModel(R.drawable.science, "Science"))
        arrayList.add(CategoryModel(R.drawable.socialscience1, "Social Politics"))
        arrayList.add(CategoryModel(R.drawable.eng, "English"))
        arrayList.add(CategoryModel(R.drawable.javas, "Java"))
        arrayList.add(CategoryModel(R.drawable.his,"History"))
        arrayList.add(CategoryModel(R.drawable.synonms,"English Synonms"))
        arrayList.add(CategoryModel(R.drawable.antonynms,"English Antonyms"))
    }

    var arrayList = ArrayList<CategoryModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database.getReference().child("PlayWinAmount").child(Firebase.auth.currentUser!!.uid)
            .child("Value")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
//
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
                    }
                })

        var adapter: CategoryAdapter = CategoryAdapter(this.arrayList, requireContext())
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            activity?.resources!!.getInteger(R.integer.number_of_grid_items)
        )
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
    }

}