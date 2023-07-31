package com.example.earningapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.Login
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlin.math.log


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    lateinit var uri: Uri
    var isexpand = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null)
            if (requestCode == 35 && data.data != null) {
                 uri= data.data!!
                Picasso.get().load(uri).into(binding.profileImage)
                }
            }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.save.setOnClickListener(View.OnClickListener {

            if (uri != null) {
                val reference: StorageReference = storage.getReference().child("ProfilePic")
                    .child(Firebase.auth.currentUser!!.uid)
                reference.putFile(uri).addOnSuccessListener {
                    reference.downloadUrl.addOnSuccessListener {
                        database.getReference().child("Users")
                            .child(Firebase.auth.currentUser!!.uid).child("profile")
                            .setValue(it.toString()).addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "image uploaded successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }

            }
        })


        binding.logout.setOnClickListener(View.OnClickListener {

            auth.signOut()
            auth.currentUser==null
            startActivity(Intent(context, Login::class.java))
            activity?.finish()
        })
        binding.expandbtn.setOnClickListener {
            if (isexpand) {
                binding.expandconstraintlayout.visibility = View.VISIBLE
                binding.expandbtn.setImageResource(R.drawable.arrowup)
                isexpand = false
            } else {
                binding.expandconstraintlayout.visibility = View.GONE
                binding.expandbtn.setImageResource(R.drawable.arrowdown)
                isexpand = true
            }
        }


        database.getReference().child("Users").child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        var users = snapshot.getValue<Users>(Users::class.java)
                        binding.name.text = users?.name
                        binding.nameup.text = users?.name
                        binding.email.text = users?.email
                        binding.password.text = users?.password
                        binding.age.text = users?.age
                        if (users?.profile != "") {
                            Picasso.get().load(users?.profile).placeholder(R.drawable.profile)
                                .into(binding.profileImage)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
//
                    }
                })


        binding.profileImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var intent = Intent()
                intent.setAction(Intent.ACTION_GET_CONTENT)
                intent.setType("image/*")
                startActivityForResult(intent, 35)
            }

        })
    }

    companion object {

    }
}